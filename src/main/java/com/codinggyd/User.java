package com.codinggyd;

import com.codinggyd.utils.ValidatorUtils;
import com.codinggyd.validator.EnumStringValid;
import com.codinggyd.validator.SexType;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class User {

    @EnumStringValid(message = "性别类型输入错误", enumClass = SexType.class)
    private String sex;

    @NotNull(message = "name不能为空！")
    private String name;

    @Digits(integer = 1,fraction = 10,message = "年龄不能大于10")
    private Integer age;

    public void setName(String name) {this.name = name;}

    public String getName() {return name;}

    public void setAge(Integer age) {this.age = age;}

    public Integer getAge() {return age;}

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setAge(9);
        user.setName("xx");
        user.setSex("1");
        ValidatorUtils.validate(user);
    }
}
