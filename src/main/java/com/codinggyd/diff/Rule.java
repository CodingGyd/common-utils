package com.codinggyd.diff;

import com.fasterxml.jackson.databind.JsonNode;

public class Rule {
    /**
     * ruleType=NUMERIC, rule无内容;
     * ruleType=DIFF_BUT_SAME, rule是一个表达式，比如1=2=3,4=5
     * ruleType=EXPECTED_GAP, rule可以是Double或date类型，根据fieldDataType的不同来确定
     */
    protected String rule;

    public boolean satisfy(JsonNode originalNode, JsonNode presentNode) {
        return true;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
