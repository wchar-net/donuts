package net.wchar.donuts.sys.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期工具类
 *
 * @author Elijah
 */
public class DateValidatorUtils {
    // 正则表达式检查日期格式
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    private static final String DATE_PATTERN_FORMAT = "yyyy-MM-dd";


    //失败返回 null
    public static LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || !dateStr.matches(DATE_PATTERN)) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_FORMAT);
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
