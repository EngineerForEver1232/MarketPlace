package com.pedpo.pedporent.widget.calendar.interfaceCalendar;


import android.view.View;

import com.pedpo.pedporent.widget.calendar.vo.DayData;


public interface OnItemCalendarClickLister {


    public void onItemCalendarClickListenerRange(View view, DayData dayData,
                                                 boolean rangeBoolean,
                                                 long startJalali,
                                                 String StringDateJalali
                                                 );

}
