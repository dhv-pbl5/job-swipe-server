package org.dhv.pbl5server.common_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@NoArgsConstructor
public class CommonUtils {
    /*
     * Json util
     */
    public static String convertToJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T decodeJson(String json, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T decodeJson(Map<String, Object> map, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(map, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> decodeJson(List<Map<String, Object>> array, Class<T> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return array.stream().map(e -> mapper.convertValue(e, type)).toList();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Object> convertObjectToMap(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * String util
     */
    public static boolean isEmptyOrNullString(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmptyOrNullString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Convert snake case
     *
     * @param input string
     * @return String type snake case
     */
    public static String convertToSnakeCase(String input) {
        if (isEmptyOrNullString(input)) return input;
        return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Convert camel case
     *
     * @param input string
     * @return String type camel case
     */
    public static String convertToCamelCase(String input) {
        if (isEmptyOrNullString(input)) return input;
        return input.replaceAll(
            "_([a-z])",
            String.valueOf(
                Character.toUpperCase(
                    input.charAt(input.indexOf("_") + 1))));
    }

    /*
     * List util
     */
    public static boolean isEmptyOrNullList(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmptyOrNullList(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isList(Object obj) {
        return obj != null && (obj.getClass().isArray() || obj instanceof Collection);
    }

    /*
     * Object util
     */
    public static <T> T getValueOrNull(T value) {
        return Objects.requireNonNullElse(value, null);
    }

    /*
     * YAML file util
     */
    public Map<String, Object> getValueFromYAMLFile(String nameFile) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameFile);
        return yaml.load(inputStream);
    }

    /*
     * Log util
     */
    public static void logError(String method, String uri, String error) {
        String content = STR."\{method}/\{uri} - Error: \{error}";
        log.error(content);
    }

    /*
     * Date time util
     */
    public static Timestamp getCurrentTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }
}
