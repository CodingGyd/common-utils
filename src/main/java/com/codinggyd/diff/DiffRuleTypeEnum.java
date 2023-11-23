package com.codinggyd.diff;

import java.util.Arrays;

public enum DiffRuleTypeEnum {
    NUMERIC(0, "数值比对"),
    EXPECTED_GAP(1, "误差范围"),
    IGNORE(2, "忽略字段"),
    SORT(3, "排序字段"),
    DIFF_BUT_SAME(4, "强制相等");

    private int code;
    private String desc;

    DiffRuleTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getCode() {
        return this.code;
    }

    public static DiffRuleTypeEnum of(int type) {
        return Arrays.stream(values())
                .filter(o -> o.code == type)
                .findFirst()
                .orElse(null);
    }

}
