package com.codinggyd.diff;


import java.util.Map;

/**
 * Created by jwq on 2019/9/6.
 */
public class RuleFactory {

    public static Rule getRule(DiffRuleTypeEnum diffRuleType, String rule, String fieldDataType, Map<String, String> extra) {
        switch (diffRuleType) {
            case EXPECTED_GAP:
                ExpectedGapRule rule1 = new ExpectedGapRule();
                rule1.setFieldDataType(fieldDataType);
                rule1.setExtra(extra);
                rule1.setRule(rule);
                return rule1;
            case DIFF_BUT_SAME:
                DiffButSameRule rule2 = new DiffButSameRule();
                rule2.setRule(rule);
                return rule2;
            case NUMERIC:
                NumericRule rule3 = new NumericRule();
                rule3.setRule(rule);
                return rule3;
            default:
                return new Rule();
        }
    }
}