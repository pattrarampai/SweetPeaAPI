package com.sweetpeatime.sweetpeatime.common.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtil {
    private DateUtil(){}
    public static Date getDate(LocalDate localDate){
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(systemTimeZone);
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        if(date == null){
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
