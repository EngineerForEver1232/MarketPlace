package com.pedpo.pedporent.widget.calendar.adapter.jalali;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pedpo.pedporent.widget.calendar.fragments.FragmentMonth_Jalali;
import com.pedpo.pedporent.widget.calendar.utils.CalendarUtil;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.vo.DateData;
import com.pedpo.pedporent.widget.calendar.vo.MonthData;
import com.pedpo.pedporent.widget.calendar.vo.jalali.DateDataJalali;


public class AdapterCalendar_Jalali extends FragmentPagerAdapter {


    public AdapterCalendar_Jalali(FragmentManager fm ) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        int year = CalendarUtil.position2Year(position);
        int month =(int) CalendarUtil.position2Month(position);

        int yearJalaly = CalendarUtil.position2YearJalaly(position);
        int monthJalaly =(int) CalendarUtil.position2MonthJalaly(position);

        FragmentMonth_Jalali fragment = new FragmentMonth_Jalali();

        MonthData monthData = new MonthData(
                new DateData(year, month, month / 2),
                new DateDataJalali(yearJalaly,monthJalaly,monthJalaly/2)
        );
        fragment.setData(monthData);
        fragment.setDataJalaly(new JalaliCalendar(yearJalaly,monthJalaly,monthJalaly/2));

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
