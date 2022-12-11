package edu.xjtlu.cpt403.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static Date parseDate(String dataString) {
        return parseDate(dataString, DATE_FORMAT);
    }


    public static Date parseDate(String dataString, String dateFormat) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat(dateFormat).parse(dataString);
        } catch (ParseException e) {}
        return date;
    }
    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
        String dayStr = sdf.format(date);
        return dayStr;
    }
}
