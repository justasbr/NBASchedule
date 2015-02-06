package com.example.android.nbaschedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by justas on 06/02/15.
 */
public class Utility {
    public static String getCurrentETtime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return (dateFormat.format(new Date()));
    }
}
