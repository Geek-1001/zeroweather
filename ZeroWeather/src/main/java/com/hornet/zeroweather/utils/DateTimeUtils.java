package com.hornet.zeroweather.utils;

import java.util.Calendar;

/**
 * Created by Ahmed on 24.12.13.
 */
public class DateTimeUtils {

// #MARK - Constants

    private static final int INTERVAL_MINUTE_TO_UPDATE = 30;

// #MARK - Methods

    public static boolean isUpdateTimeRecently(Calendar currentTime, Calendar updateTime){
        for(int i = 0; i < INTERVAL_MINUTE_TO_UPDATE; i++){
            updateTime.add(Calendar.MINUTE, 1);
        }
        if(currentTime.compareTo(updateTime) == 1){
            return false;
        }
        return true;
    }

    public static Calendar getCurrentTimeCalendar(){
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static String getCurrentTimeString(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static Calendar getCalendarFromString(String time){
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }



}
