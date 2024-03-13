package org.dhv.pbl5server.common_service.utils;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {
    public static void error(String method, String uri, String error) {
        String content = STR."\{method}/\{uri} - Error: \{error}";
        log.error(content);
    }

    private LogUtils() {
    }
}
