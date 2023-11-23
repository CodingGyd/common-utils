package com.codinggyd.diff;

public enum JsonRowResultEnum {
    SAME(0, "same"),
    ADD(1, "add"),
    DELETE(2, "delete"),
    DIFF(3, "diff"),
    IGNORE(4, "ignore");

    private int value;
    private String name;

    JsonRowResultEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
