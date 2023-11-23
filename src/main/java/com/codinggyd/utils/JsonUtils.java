package com.codinggyd.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


 public class JsonUtils {

    private JsonUtils() {
        throw new UnsupportedOperationException();
    }


    public static JsonNode jsonStr2JsonNode(String json) throws Exception {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper objectMapper = JsonUtils2ObjectMapper.OBJECT_MAPPER_CONTAINS_NULL;
        return objectMapper.readTree(json);
    }


    public static String object2JsonString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            ObjectMapper objectMapper = JsonUtils2ObjectMapper.OBJECT_MAPPER_WRITE_DATE_AS_TIMESTAMPS;
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return "";
        }
    }

}
