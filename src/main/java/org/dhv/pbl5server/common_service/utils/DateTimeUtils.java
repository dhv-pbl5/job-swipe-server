package org.dhv.pbl5server.common_service.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeUtils {

    public static Timestamp getCurrentDateTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    private DateTimeUtils() {
    }
}
