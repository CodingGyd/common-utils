package com.codinggyd.diff;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;

/**
 * 数值不同，但是认为一致
 */

public class DiffButSameRule extends Rule {

    private final static String COMBINATION_SEPARATOR = ",";

    private final static String VALUE_SEPARATOR = "=";

    public static void main(String[] args) {
        List<String> values = Arrays.asList("{\\\"preferentialCrowdPeoples\\\":[]}={\\\"preferentialCrowdPeoples\\\":null}".split(VALUE_SEPARATOR));

        System.out.println(values);

    }

    @Override
    public boolean satisfy(JsonNode originalNode, JsonNode presentNode) {
        String original = originalNode.toString();
        String present = presentNode.toString();
        if (originalNode.equals(presentNode)) {
            return true;
        }
        List<String> equalValues = Arrays.asList(rule.split(COMBINATION_SEPARATOR));
        for (String equalValue : equalValues) {
            List<String> values = Arrays.asList(equalValue.split(VALUE_SEPARATOR));
            if (values.contains(original) && values.contains(present)) {
                return true;
            }
        }
        return false;
    }

}
