package org.chervyakovsky.multithreading.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getTime(){
        String time;
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        time = format.format(dateNow);
        return time;
    }
}
