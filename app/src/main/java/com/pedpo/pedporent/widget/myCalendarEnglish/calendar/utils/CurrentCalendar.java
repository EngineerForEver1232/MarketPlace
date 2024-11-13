package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils;


import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DateData;

import java.util.Calendar;

public class CurrentCalendar {
    public static DateData getCurrentDateData(){
        Calendar calendar = Calendar.getInstance();

        return new DateData(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }

}

