package com.codinggyd.diff;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

/**
 * 直接转成数字进行比对，比如IntNode和DoubleNode的比较
 */

public class NumericRule extends Rule {

    @Override
    public boolean satisfy(JsonNode originalNode, JsonNode presentNode) {
        BigDecimal original = BigDecimal.valueOf(originalNode.asDouble());
        BigDecimal present = BigDecimal.valueOf(presentNode.asDouble());

        return original.equals(present);
    }
}
