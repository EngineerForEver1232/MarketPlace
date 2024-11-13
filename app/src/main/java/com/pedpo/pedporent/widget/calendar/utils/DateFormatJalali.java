package com.pedpo.pedporent.widget.calendar.utils;

import com.pedpo.pedporent.widget.calendar.vo.DayData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatJalali {

    private static DateFormatJalali dateFormatJalali = new DateFormatJalali();

    public static DateFormatJalali newInstance()
    {
        return dateFormatJalali;
    }

    public String formatDate(DayData dayData)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JalaliCalendar jalaliCalendar = dayData.getJalaliCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.set(jalaliCalendar.getYear(),jalaliCalendar.getMonth()-1,jalaliCalendar.getDay());
        String dateStringJalali = sdf.format(new Date(calendar.getTimeInMillis()));

        return dateStringJalali;
    }

    public String formatDateJalali(Calendar calendarStart)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarStart.get(Calendar.YEAR),calendarStart.get(Calendar.MONTH)-1,calendarStart.get(Calendar.DAY_OF_MONTH));
        String dateStringJalali = sdf.format(new Date(calendar.getTimeInMillis()));
        return dateStringJalali;
    }

}
