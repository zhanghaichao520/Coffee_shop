package edu.xjtlu.cpt403.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Date parseDate(String dataString) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dataString);
        } catch (ParseException e) {}
        return date;
    }
}
