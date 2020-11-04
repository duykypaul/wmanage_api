package com.duykypaul.wmanage_api.common;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date calculateExpirationDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
