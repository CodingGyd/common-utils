package com.codinggyd.utils;

import com.codinggyd.annotation.CNDesc;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CompareFieldUtils {
    /**
     * 比较两个相同类实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     * @param obj1 进行属性比较的对象1
     * @param obj2 进行属性比较的对象2
     * @param ignoreArr 选择忽略比较的属性数组
     * @return 属性差异比较结果map
     */
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, String[] ignoreArr) {
        try{
            Map<String, List<Object>> map = new HashMap<>();
            List<String> ignoreList = null;
            if(ignoreArr != null && ignoreArr.length > 0){
                // array转化为list
                ignoreList = Arrays.asList(ignoreArr);
            }
            //只比较基本类型、包装类型、String类型
            String[] types = {"java.lang.Integer",
                    "java.lang.Double",
                    "java.lang.Float",
                    "java.lang.Long",
                    "java.lang.Short",
                    "java.lang.Byte",
                    "java.lang.Boolean",
                    "java.lang.Character",
                    "java.lang.String",
                    "int","double","long","short","byte","boolean","char","float"};
            List<String> typeList=Arrays.asList(types);

            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = obj1.getClass();
                // 获取object的属性描述(有get、set才叫属性)
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,Object.class).getPropertyDescriptors();
                Field[] fields = clazz.getDeclaredFields();
                List<Object> list;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getWriteMethod()==null) {
                        continue;
                    }

                    String name = pd.getName();// 属性名
                    if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    //只比较基本类型、包装类型、String类型
                    if (!typeList.contains(pd.getPropertyType().getName())) {
                        continue;
                    }

                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(obj1);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(obj2);

                    if(o1 == null && o2 == null){
                        continue;
                    }

                    //字符序列处理，null和""默认相等
                    if (o1 instanceof CharSequence||o2 instanceof CharSequence){
                        if ((o1==null&&"".equals(o2))||(o2==null&&"".equals(o1))) {
                            continue;
                        }
                    }

                    if (o1 != null && o2 != null&&o1.equals(o2)) {
                        continue;
                    }

                    list = new ArrayList<>();
                    if (o1==null||"".equals(o1)) {
                        list.add("空值");
                    }else {
                        list.add(o1);
                    }
                    if (o2==null||"".equals(o2)) {
                        list.add("空值");
                    }else {
                        list.add(o2);
                    }
                    //获得注解值
                    for (Field field : fields) {
                        if(name.equals(field.getName())&&field.isAnnotationPresent(CNDesc.class)){
                            name=field.getAnnotation(CNDesc.class).value();
                            break;
                        }
                    }
                    map.put(name, list);
                }
            }
            return map;
        }catch(Exception e){
            //e.printStackTrace();
            return null;
        }
    }

}
