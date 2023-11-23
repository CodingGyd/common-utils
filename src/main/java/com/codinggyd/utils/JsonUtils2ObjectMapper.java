package com.codinggyd.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



public class JsonUtils2ObjectMapper {

    private JsonUtils2ObjectMapper() {
        throw new UnsupportedOperationException();
    }

    public static final ObjectMapper OBJECT_MAPPER_WRITE_DATE_AS_TIMESTAMPS;
    public static final ObjectMapper OBJECT_MAPPER_CONTAINS_NULL;
    public static final ObjectMapper OBJECT_MAPPER_CONTAINS_NULL_USE_ANNOTATION;

    public static final ObjectMapper OBJECT_MAPPER_CONTAINS_NOT_NULL;
    public static final ObjectMapper OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION;

    static {
        OBJECT_MAPPER_WRITE_DATE_AS_TIMESTAMPS = new ObjectMapper();
        OBJECT_MAPPER_WRITE_DATE_AS_TIMESTAMPS.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_WRITE_DATE_AS_TIMESTAMPS.configure(MapperFeature.USE_ANNOTATIONS, false);

        OBJECT_MAPPER_CONTAINS_NULL = new ObjectMapper();
        OBJECT_MAPPER_CONTAINS_NULL.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_CONTAINS_NULL.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER_CONTAINS_NULL.configure(MapperFeature.USE_ANNOTATIONS, false);

        OBJECT_MAPPER_CONTAINS_NULL_USE_ANNOTATION = new ObjectMapper();
        OBJECT_MAPPER_CONTAINS_NULL_USE_ANNOTATION.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_CONTAINS_NULL_USE_ANNOTATION.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER_CONTAINS_NULL_USE_ANNOTATION.configure(MapperFeature.USE_ANNOTATIONS, true);

        OBJECT_MAPPER_CONTAINS_NOT_NULL = new ObjectMapper();
        OBJECT_MAPPER_CONTAINS_NOT_NULL.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_CONTAINS_NOT_NULL.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER_CONTAINS_NOT_NULL.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_CONTAINS_NOT_NULL.configure(MapperFeature.USE_ANNOTATIONS, false);

        OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION = new ObjectMapper();
        OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_CONTAINS_NOT_NULL_USE_ANNOTATION.configure(MapperFeature.USE_ANNOTATIONS, true);
    }
}
