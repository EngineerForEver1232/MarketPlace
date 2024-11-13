package com.pedpo.pedporent.widget.calendar.adapter.miladi;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.fragments.FragmentMonth;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.CalendarUtil;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DateData;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.MonthData;


public class AdapterCalendar_Miladi extends FragmentPagerAdapter  {


    public AdapterCalendar_Miladi(FragmentManager fm ) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        int year = CalendarUtil.position2Year(position);
        int month =(int) CalendarUtil.position2Month(position);
//        int day = CalendarUtil.position2Day(position);

        FragmentMonth fragment = new FragmentMonth();

        MonthData monthData = new MonthData(new DateData(year, month, month / 2));
        fragment.setData(monthData);

//        MarkedDates.getInstance().refreshServices();


        return fragment;
    }



    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
