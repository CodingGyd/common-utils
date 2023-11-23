package com.codinggyd.diff;


import java.util.Map;


public class DiffRule {

    private DiffRuleTypeEnum diffRuleType;

    private String field;

    private String fieldDataType;

    private Map<String, String> extra;

    private String rule;

    public DiffRuleTypeEnum getDiffRuleType() {
        return diffRuleType;
    }

    public void setDiffRuleType(DiffRuleTypeEnum diffRuleType) {
        this.diffRuleType = diffRuleType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldDataType() {
        return fieldDataType;
    }

    public void setFieldDataType(String fieldDataType) {
        this.fieldDataType = fieldDataType;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
