package org.dhv.pbl5server.common_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.common_service.model.ErrorResponse;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class ErrorUtils {

    private static final String ERROR_FILE = "errors.yml";
    private static final String VALIDATION_FILE = "validations.yml";

    public static String convertToJSONString(Object ob) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(ob);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String handleFieldName(String fieldName) {
        String index = fieldName.substring(fieldName.indexOf("[") + 1, fieldName.indexOf("]"));
        return fieldName.replaceAll(index, "");
    }

    /**
     * Convert camel case to snake case
     *
     * @param input string type camel case
     * @return String type snake case
     */
    public static String convertToSnakeCase(String input) {
        return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }


    /**
     * Extract exception error
     *
     * @param error String error
     * @return ErrorResponse
     */
    @SuppressWarnings("unchecked")
    public static ErrorResponse getExceptionError(String error) {
        CommonUtils readYAMLFileUtil = new CommonUtils();
        Map<String, Object> errors = readYAMLFileUtil.getValueFromYAMLFile(ERROR_FILE);
        Map<String, Object> objError = (Map<String, Object>) errors.get(error);
        String code = (String) objError.get("code");
        String message = (String) objError.get("message");
        return new ErrorResponse(code, message);
    }

    /**
     * Extract validation error
     *
     * @param resource  file error
     * @param fieldName field error
     * @param error     String error
     * @return ErrorResponse
     */
    @SuppressWarnings("unchecked")
    public static ErrorResponse getValidationError(String resource, String fieldName, String error) {
        if (fieldName.contains("[")) {
            fieldName = handleFieldName(fieldName);
        }
        CommonUtils readYAMLFileUtil = new CommonUtils();
        Map<String, Object> errors = readYAMLFileUtil.getValueFromYAMLFile(VALIDATION_FILE);
        Map<String, Object> fields = (Map<String, Object>) errors.get(resource);
        Map<String, Object> objErrors = (Map<String, Object>) fields.get(fieldName);
        Map<String, Object> objError = (Map<String, Object>) objErrors.get(error);
        if (objError == null)
            new ErrorResponse("ERR_SER0101", "objError is null");
        String code = (String) Objects.requireNonNull(objError).get("code");
        String message = (String) objError.get("message");
        return new ErrorResponse(code, message);
    }

    private ErrorUtils() {
    }
}
