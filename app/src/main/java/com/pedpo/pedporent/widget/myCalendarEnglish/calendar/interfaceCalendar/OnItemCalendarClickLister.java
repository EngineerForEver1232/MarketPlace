package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.interfaceCalendar;


import android.view.View;

import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DayData;

import java.util.Date;


public interface OnItemCalendarClickLister {


    public void onItemCalendarClickListenerRange(View view, DayData dayData,
                                                 Date startJalali,
                                                 String StringDateJalali
                                                 );

}
