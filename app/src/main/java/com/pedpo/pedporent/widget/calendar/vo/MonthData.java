package com.pedpo.pedporent.widget.calendar.vo;


import android.graphics.Color;
import android.util.Log;

import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.vo.jalali.DateDataJalali;

import java.util.ArrayList;
import java.util.Calendar;


public class MonthData {
    private DateData date;
    private DateDataJalali dateDataJalali;
    private Calendar calendar;

    private int startDay;
    private int totalDay;
    private int lastMonth;
    private int lastMonthTotalDay;
    private int lastMonthTotalDayJalali;
    //    private JalaliCalendar jalaliCalendar ;
    private int totalDayJalaly = 0;
    private int startDayJalaly = 0;

    public int getLastMonthTotalDay() {
        return lastMonthTotalDay;
    }

    public void setLastMonthTotalDay(int lastMonthTotalDay) {
        this.lastMonthTotalDay = lastMonthTotalDay;
    }

    private ArrayList<DayData> content;
    private boolean hasTitle;

    public MonthData(DateData dateData, DateDataJalali dateDataJalali) {
//        this.jalaliCalendar = jalaliCalendar;
        this.dateDataJalali = dateDataJalali;

        date = dateData;
        calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay());
        content = new ArrayList<DayData>();
        this.hasTitle = hasTitle;
//        initHeadToTail();
//        initArray();
        initHeadToTailJalali();
        initArrayJalali();
    }

    private void initHeadToTail() {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.clear();
        //DATE=1 for day_of_week in line for start Sunday
        tmpCal.set(date.getYear(), date.getMonth() - 1, 0);
        totalDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        startDay = tmpCal.get(Calendar.DAY_OF_WEEK) - 1;
        Log.e("calendarMonth", "initHeadToTail: " + startDay + " , " + totalDay);
        totalDay = totalDay + startDay;
        // in if vase meghdare dehi lastmounthtotalday estefade mishavad
        if (date.getMonth() - 1 > 0) {
            // akharin mah manzor : mahe jari ya haman mahi k alan dar an hastim : injs 2ts ksm karde ta dar lastmounthtotaldy az an estefade konad
            lastMonth = date.getMonth() - 2;
            tmpCal.set(date.getYear(), lastMonth, 1);
            lastMonthTotalDay = tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            // akharin mah manzor : mahe jari ya haman mahi k alan dar an hastim : inja b halate aval yani mahe jari barmigardanad
            lastMonth += 1;
        } else {
            lastMonth = 12;
            tmpCal.set(date.getYear() - 1, 11, 1);
            lastMonthTotalDay = tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
    }

    private void initHeadToTailJalali() {

        JalaliCalendar jalaliCalendar2 = new JalaliCalendar(dateDataJalali.getYear(), dateDataJalali.getMonth(), 1);
//        ir.huri.jcal.JalaliCalendar jalaliCalendarNew = new ir.huri.jcal.JalaliCalendar(dateDataJalali.getYear(),dateDataJalali.getMonth(),1);

        //tedad rozhae month
        totalDayJalaly = new JalaliCalendar(dateDataJalali.getYear(), dateDataJalali.getMonth(), dateDataJalali.getDay()).getMonthLength();
        if (jalaliCalendar2.getDayOfWeek() == 7)
            startDayJalaly = 0;
        else
            startDayJalaly = jalaliCalendar2.getDayOfWeek();

        Log.e("calendarJalali",
                "(" + dateDataJalali.getYear() + "/" + dateDataJalali.getMonth() + "/" + dateDataJalali.getDay() +
                        ")( day of week : " + dateDataJalali.getDayString() +
                        ")( jalali :" + jalaliCalendar2.getYear() + "/" + jalaliCalendar2.getMonth() + "/" + jalaliCalendar2.getDay() + "=" + jalaliCalendar2.getDayOfWeek() +
//                ")( jalali New  :" + jalaliCalendarNew.getYear()+"/"+jalaliCalendarNew.getMonth()+"/"+jalaliCalendarNew.getDay()+"="+jalaliCalendarNew.getDayOfWeek()+
                        ")( totalDay : " + totalDayJalaly);

        totalDayJalaly = totalDayJalaly + startDayJalaly;

//        if (jalaliCalendar2.getMonth() - 1 > 0) {
//            // akharin mah manzor : mahe jari ya haman mahi k alan dar an hastim : injs 2ts ksm karde ta dar lastmounthtotaldy az an estefade konad
//            lastMonth = jalaliCalendar2.getMonth() - 2;
//            jalaliCalendar2.set(jalaliCalendar2.getYear(), lastMonth, 1);
//            lastMonthTotalDayJalali = jalaliCalendar2.getMonthLength();
//            // akharin mah manzor : mahe jari ya haman mahi k alan dar an hastim : inja b halate aval yani mahe jari barmigardanad
//            lastMonth += 1;
//        } else {
//            lastMonth = 12;
//            jalaliCalendar2.set(jalaliCalendar2.getYear() - 1, 11, 1);
//            lastMonthTotalDayJalali = jalaliCalendar2.getMonthLength();
//        }
    }

    private void initArray() {
        JalaliCalendar jalaliCalendar = new JalaliCalendar();

        DayData addDate;
        for (int i = 0; i < totalDay; i++) {
            if (i < startDay) {
                addDate = new DayData(new DateData(date.getYear(),
                        lastMonth, 0)
                );
                addDate.setTextColor(Color.GRAY);
                addDate.setTextSize(0);
                content.add(addDate);
                Log.e("calendarMonth", "initArray: i = " + i + " , " + startDay);
                continue;
            }

            Log.e("clandarIf", "calenar : i = " + i + " , " + (i + 1 - startDay) + " , month : " + jalaliCalendar.getMonth() + " , " + jalaliCalendar.getMonthLength());
            addDate = new DayData(new DateData(date.getYear(),
                    date.getMonth(),
                    i + 1 - startDay));
            addDate.setTextColor(Color.BLACK);
            addDate.setTextSize(1);
            content.add(addDate);
        }
    }



    private void initArrayJalali() {
        DayData addDate;
        for (int i = 0; i < totalDayJalaly; i++) {
            if (i < startDayJalaly) {
                addDate = new DayData(new DateData(0, 0, 0), new JalaliCalendar(0, 0, 0));
                addDate.setTextColor(Color.GRAY);
                addDate.setTextSize(0);
                content.add(addDate);
                Log.e("calendarJalali", " i : " + i + "  initArrayJalali: " + startDayJalaly);
                continue;
            }

            addDate = new DayData(
                    new DateData(date.getYear(),
                            date.getMonth(),
                            i + 1 - startDay));


//            addDate.setDataJalali(new DateDataJalali(
//                    dateDataJalali.getYear(),
//                    dateDataJalali.getMonth(),
//                    i + 1 - startDayJalaly));

//            addDate.setDayJalali(i + 1 - startDayJalaly);
            addDate.setJalaliCalendar(new JalaliCalendar(
                    dateDataJalali.getYear(),
                    dateDataJalali.getMonth(),
                    i + 1 - startDayJalaly));
            addDate.setTextColor(Color.BLACK);
            addDate.setTextSize(1);
            content.add(addDate);
        }
    }

    //    private void initArray(){
////        if (hasTitle){
////            for (int i = 0;i < 7;i++){
////                content.add(new TitleData(new DateData(0,0,i+1)));
////            }
////        }
//        DayData addDate;
//        for(int i = 0;i < totalDay+7;i++){
//            if(i < startDay) {
//                addDate = new DayData(new DateData(date.getYear(),
//                        lastMonth,
//                        lastMonthTotalDay - (startDay- i)+1));
//                addDate.setTextColor(Color.GRAY);
//                addDate.setTextSize(0);
//                content.add(addDate);
//                continue;
//            }
//            if((i >= totalDay) && (i % 7 !=0)){
//                // Maybe move them to DateData class.
//                boolean happyNewYear = false;
//                int nextYear, nextMonth;
//                happyNewYear = date.getMonth() == 12;
//                nextMonth = happyNewYear ? 1 : date.getMonth() + 1;
//                nextYear = happyNewYear ? date.getYear() + 1 : date.getYear();
//
//                addDate = new DayData(new DateData(nextYear,
//                        nextMonth,
//                        i - totalDay + 1));
//                addDate.setTextColor(Color.GRAY);
//                addDate.setTextSize(0);
//                content.add(addDate);
//                continue;
//            }else if((i >= totalDay) && (i % 7 ==0)){
//                return;
//            }
//            addDate = new DayData(new DateData(
//                    date.getYear(),
//                    date.getMonth(),
//                    i + 1 - startDay));
//            addDate.setTextColor(Color.BLACK);
//            addDate.setTextSize(1);
//            content.add(addDate);
//        }
//    }
    public void travelTo(DateData date) {
        this.date = date;
        calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, 1);
        totalDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        initHeadToTail();

        content.clear();
        initArray();
    }
    public void travelToJalali(DateDataJalali dataJalali) {
        this.dateDataJalali = dataJalali;
//        calendarJalaliThis = Calendar.getInstance();
//        calendarJalaliThis.set(dateDataJalali.getYear(), dateDataJalali.getMonth() - 1, 1);
        totalDayJalaly = new JalaliCalendar(dateDataJalali.getYear(), dateDataJalali.getMonth(), dateDataJalali.getDay()).getMonthLength();

        JalaliCalendar jalaliCalendar2 = new JalaliCalendar(dateDataJalali.getYear(), dateDataJalali.getMonth(), 1);

        if (jalaliCalendar2.getDayOfWeek() == 7)
            startDayJalaly = 0;
        else
            startDayJalaly = jalaliCalendar2.getDayOfWeek();

        initHeadToTailJalali();

        content.clear();
        initArrayJalali();
    }

    public ArrayList getData() {
        return content;
    }

    public DateData getDate() {
        return date;
    }
}
