package com.codinggyd.diff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.commons.collections.MapUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 预期差距内比对算一致
 */
public class ExpectedGapRule extends Rule {

    protected String fieldDataType;

    protected Map<String, String> extra;
    @Override
    public boolean satisfy(JsonNode originalNode, JsonNode presentNode) {
        if ("date".equalsIgnoreCase(fieldDataType)) {
            return dateSatisfy(originalNode, presentNode);
        }

        double gap = Double.parseDouble(rule);
        double original = originalNode.asDouble();
        double present = presentNode.asDouble();
        return BigDecimal.valueOf(original)
                .subtract(BigDecimal.valueOf(present))
                .abs()
                .doubleValue() <= gap;
    }

    /**
     * 如果是date类型，则使用此方法进行校验
     *
     * @param originalNode
     * @param presentNode
     * @return
     */
    public boolean dateSatisfy(JsonNode originalNode, JsonNode presentNode) {
        if (originalNode.getNodeType() == JsonNodeType.NULL && presentNode.getNodeType() == JsonNodeType.NULL) {
            return true;
        }

        Date origin = null;
        Date present = null;
        if (originalNode.getNodeType() == JsonNodeType.NUMBER && presentNode.getNodeType() == JsonNodeType.NUMBER) {
            origin = new Date(originalNode.asLong());
            present = new Date(presentNode.asLong());
        }
        if (originalNode.getNodeType() == JsonNodeType.STRING && presentNode.getNodeType() == JsonNodeType.STRING) {
            DateParser dateParser = new DateParser();
            if (MapUtils.isNotEmpty(extra) && extra.containsKey("format")) {
                String format = extra.get("format");
                dateParser.addDateFormat(new SimpleDateFormat(format));
            }

            String originText = originalNode.asText();
            origin = dateParser.deserializeToDate(originText);
            present = dateParser.deserializeToDate(presentNode.asText());
        }

        if (origin != null && present != null) {

            long duration = Math.abs(origin.getTime() - present.getTime());

            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);

            return diffInSeconds <= Long.parseLong(rule);
        }

        return false;
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
}
