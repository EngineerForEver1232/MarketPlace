package com.pedpo.pedporent.widget.calendar.utils;

import android.util.Log;


import java.util.Calendar;

public class ConvertTimeTo {

    private static ConvertTimeTo convertTimeTo = new ConvertTimeTo();

    public static ConvertTimeTo getInstance() {
        return convertTimeTo;
    }

    public void CovertTimeToZero(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public long convertToLong(JalaliCalendar jalaliCalendar) {
        if (jalaliCalendar == null)
            return 0;

        String year = String.format("%04d", jalaliCalendar.getYear());
        String month = String.format("%02d", jalaliCalendar.getMonth());
        String day = String.format("%02d", jalaliCalendar.getDay());
        long initDate = Long.parseLong(year + "" +
                month + "" +
                day + "");

//        Log.e("rangeCalendarTestClick", "convertToInt: " + jalaliCalendar.toString() + " = " + initDate + " && " + jalaliCalendar.getMonth() + " && " + jalaliCalendar.getDay());
        return initDate;
    }


    public int convertJalaliStringToInt(String str, String typeReturn) {
        if (str.isEmpty())
            return 0;
        String[] strArray = str.split("-");

        if (typeReturn.equals(AppContents.YEAR))
        {
            return Integer.parseInt(strArray[0]);
        }
        else if (typeReturn.equals(AppContents.MONTH))
            return Integer.parseInt(strArray[1]);
        else
            return Integer.parseInt( strArray[2]);
    }

}
