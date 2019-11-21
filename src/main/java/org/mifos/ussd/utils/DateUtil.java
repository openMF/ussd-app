package org.mifos.ussd.utils;


import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

import java.util.Date;

/**
 * Created by Antony on 8/8/2016.
 */
public class DateUtil {

    public static int getAge(Date date) {
        int age = 0;
        LocalDateTime localDate = new LocalDateTime(date);
        LocalDateTime now = LocalDateTime.now();

        Minutes minutes = Minutes.minutesBetween(localDate, now);
        age = minutes.getMinutes();

        return age;
    }
}
