package com.codinggyd.validator;

/**
 * @ClassName SexType
 * @Description 性别类型
 * @Author guoyading
 * @Date 2023/7/17 10:37
 * @Version 1.0
 */
public enum SexType implements EnumValidate<String> {
    MAN("1001","男"),
    WOMAN("1002","女"),
    UN_KNOW("1003","未知")
    ;

    SexType(String code, String name){
        this.code = code;
        this.name = name;
    }
    private String code;
    private String name;

    public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    @Override
    public boolean existValidate(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }

        for (SexType testEnum : SexType.values()) {
            if (testEnum.getCode().equalsIgnoreCase(value.trim())) {
                return true;
            }
        }
        return false;
    }
}
