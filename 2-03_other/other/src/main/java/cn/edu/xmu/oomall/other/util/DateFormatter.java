package cn.edu.xmu.oomall.other.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/16 14:50
 **/
public class DateFormatter {
    public static LocalDateTime zoned2Local(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }
}
