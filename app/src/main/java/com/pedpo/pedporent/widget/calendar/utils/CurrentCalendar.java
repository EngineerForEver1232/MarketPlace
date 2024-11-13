package com.pedpo.pedporent.widget.calendar.utils;


import com.pedpo.pedporent.widget.calendar.vo.DateData;
import com.pedpo.pedporent.widget.calendar.vo.jalali.DateDataJalali;

import java.util.Calendar;

public class CurrentCalendar {
    public static DateData getCurrentDateData(){
        Calendar calendar = Calendar.getInstance();

        return new DateData(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }

    public static JalaliCalendar getCurrentJalali()
    {
        JalaliCalendar jalaliCalendar = new JalaliCalendar();

        return jalaliCalendar;
    }
    public static DateDataJalali getCurrentDateDataJalali()
    {
        JalaliCalendar jalaliCalendar = new JalaliCalendar();

        return new DateDataJalali(
                jalaliCalendar.getYear(),
                jalaliCalendar.getMonth(),
                jalaliCalendar.getDay());
    }

}

