package com.ecommerce.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * JSON utility methods using Jackson ObjectMapper.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    /**
     * Convert object to JSON string.
     */
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON", e);
            throw new IllegalArgumentException("JSON serialization failed", e);
        }
    }

    /**
     * Convert object to pretty-printed JSON string.
     */
    public static String toPrettyJson(Object object) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON", e);
            throw new IllegalArgumentException("JSON serialization failed", e);
        }
    }

    /**
     * Parse JSON string to object.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON to object", e);
            throw new IllegalArgumentException("JSON deserialization failed", e);
        }
    }

    /**
     * Parse JSON string to object with TypeReference (for generics).
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON to object", e);
            throw new IllegalArgumentException("JSON deserialization failed", e);
        }
    }

    /**
     * Safely parse JSON string, returning Optional.
     */
    public static <T> Optional<T> fromJsonSafe(String json, Class<T> clazz) {
        try {
            return Optional.ofNullable(OBJECT_MAPPER.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize JSON: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get the shared ObjectMapper instance.
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
