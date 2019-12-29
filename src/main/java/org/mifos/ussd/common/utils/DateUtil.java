package org.mifos.ussd.common.utils;


import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import java.util.Date;

/**
 * Created by Antony on 8/8/2016.
 */
public class DateUtil {

    public static int getAgeSeconds(Date date) {
        int age;
        LocalDateTime localDate = new LocalDateTime(date);
        LocalDateTime now = LocalDateTime.now();

        Seconds seconds = Seconds.secondsBetween(localDate, now);
        age = seconds.getSeconds();

        return age;
    }
}
