package com.codinggyd.utils;

import com.codinggyd.User;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BeanMapUtils
 * @Description TODO
 * @Author guoyading
 * @Date 2023/7/9 14:46
 * @Version 1.0
 */
public class BeanMapUtils {
    public static <T> Map<String, ?> beanToMap(T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap<>();

        beanMap.forEach((key, value) -> {
            map.put(String.valueOf(key), value);
        });
        return map;
    }

    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz)
            throws IllegalAccessException, InstantiationException {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public static <T> Map<String, String> beanToMapStr(T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, String> map = new HashMap<>();

        beanMap.forEach((key, value) -> {
            map.put(String.valueOf(key), String.valueOf(value));
        });
        return map;
    }

    public static <T> List<Map<String, ?>> objectsToMaps(List<T> objList) {
        List<Map<String, ?>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, ?> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    public static <T> List<T> mapsToObjects(List<Map<String, ?>> maps, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<String, ?> map = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                T bean = mapToBean(map, clazz);
                list.add(bean);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        testBeanToMap();
        System.out.println("========");
        testMapToBean();
    }
    public static void testBeanToMap() {
        User user = new User();
        user.setName("testName");
        user.setAge(18);
        Map<String, ?> map = BeanMapUtils.beanToMap(user);
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));

    }

    public static void testMapToBean() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 18);
        map.put("name", "testName");
        try {
            User userVo = BeanMapUtils.mapToBean(map, User.class);
            System.out.println(userVo.getName());
            System.out.println(userVo.getAge());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
