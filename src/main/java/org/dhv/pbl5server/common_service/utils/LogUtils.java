// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {
    private static final String ARROW = "===>";

    /*
     * Error log
     */
    public static void error(String prefix, Exception e) {
        log.error("{} {} {}", getPrefix(prefix), ARROW, getErrorString(e));
        e.printStackTrace();
    }

    public static void error(String prefix, String msg, Exception e) {
        log.error("{} {} {} {}", getPrefix(prefix), ARROW, msg, getErrorString(e));
        e.printStackTrace();
    }

    public static void error(String prefix, String msg) {
        log.error("{} {} {}", getPrefix(prefix), ARROW, msg);
    }

    public static void error(String prefix, String msg, Object... args) {
        log.error("{} {} {}" + " {}".repeat(args.length), getPrefix(prefix), ARROW, msg, args);
    }

    /*
     * Info log
     */
    public static void info(String prefix, String msg) {
        log.info("{} {} {}", getPrefix(prefix), ARROW, msg);
    }

    public static void info(String prefix, String msg, Object... args) {
        log.info("{} {} {}" + " {}".repeat(args.length), getPrefix(prefix), ARROW, msg, args);
    }

    private static String getPrefix(String prefix) {
        return CommonUtils.isEmptyOrNullString(prefix) ? "PREFIX" : prefix;
    }

    private static String getErrorString(Exception e) {
        return CommonUtils.isEmptyOrNullString(e.getMessage()) ? e.toString() : e.getMessage();
    }

    private LogUtils() {
    }
}
