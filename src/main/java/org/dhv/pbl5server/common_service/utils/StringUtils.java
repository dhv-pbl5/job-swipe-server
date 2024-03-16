package org.dhv.pbl5server.common_service.utils;

public class StringUtils {

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmptyOrNull(String str) {
        return !isEmptyOrNull(str);
    }

    private StringUtils() {
    }
}
