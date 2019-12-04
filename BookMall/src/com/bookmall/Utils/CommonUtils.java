package com.bookmall.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
    public static String localDateTimeTransformString(LocalDateTime localDateTime) {
        String dateTimeStr = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateTimeStr = LocalDateTime.now(ZoneId.of("+8")).format(formatter);
        return dateTimeStr;
    }
}
