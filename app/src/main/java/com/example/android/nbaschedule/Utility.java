package com.example.android.nbaschedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by justas on 06/02/15.
 */
public class Utility {

    private static final int TIMEZONE_CHANGE = 5;
    public static String getCurrentETtime(int hours){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return (dateFormat.format(cal.getTime()));
    }
    public static int getTimezoneChange(){
        return TIMEZONE_CHANGE;
    }



    public static String formFriendlyDate(String dateStr) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("MM dd");
        try {

            Date inputDate = sdfInput.parse(dateStr);
            Date nowDate = new Date();
            String nowDateStr = sdfInput.format(nowDate);
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.
            if (dateStr.equals(nowDateStr)) {
                return "Tonight";
            } else {
                // If the date is set for tomorrow, the format is "Tomorrow".
                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.DATE, 1);
                String tomorrowStr = sdfInput.format(cal.getTime());
                if (dateStr.equals(tomorrowStr)) {
                    return "Tomorrow";
                }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
