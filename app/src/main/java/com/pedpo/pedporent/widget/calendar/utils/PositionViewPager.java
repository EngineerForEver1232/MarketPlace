package com.pedpo.pedporent.widget.calendar.utils;

import android.content.Context;
import android.util.Log;



import java.util.Calendar;
import java.util.Date;

public class PositionViewPager {

    private static PositionViewPager positionViewPager = new PositionViewPager();

    public static PositionViewPager getInstance() {
        return positionViewPager;
    }

    public int position(Context context) {
        PrefManager prefManager = new PrefManager(context);
        /**
         * vase inke zamani k tarikh payan khali hast vali tarikh now por hast mohasebat kharab mishavad
         * ya bayad tarikh now ham khali shavad
         * ya vaghti tarikh payan khali hast mohasebeyi anjam nashode va 0 return shavad
         */
        if (prefManager.getFormatEnd_Jalali().isEmpty())
            return -1;

        int thisYear = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.YEAR);
        int thisMonth = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.MONTH);

        int endYear = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getFormatEnd_Jalali(), AppContents.YEAR);
        int endMonth = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getFormatEnd_Jalali(), AppContents.MONTH);

        int year = Math.abs(endYear - thisYear);
        int mount = (endMonth);

        int result;
        if (year == 0)
            result = (endMonth) - thisMonth;
        else
            result = (12 - thisMonth) + ((year - 1) * 12) + mount;

        return result;
    }


    public int positionStart(Context context) {
        PrefManager prefManager = new PrefManager(context);

        if (prefManager.getFormatStart_Jalali().isEmpty())
            return -1;

        int thisYear = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.YEAR);
        int thisMonth = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.MONTH);

        int startYear = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getFormatStart_Jalali(), AppContents.YEAR);
        int startMonth = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getFormatStart_Jalali(), AppContents.MONTH);

        int year = Math.abs(startYear - thisYear);
        int mount = startMonth;

        int result;
        if (year == 0)
            result = (startMonth) - thisMonth;
        else
            result = (12 - thisMonth) + ((year - 1) * 12) + mount;

        return result;
    }


    public int positionMonthDatepicker(Context context,int yearPicker , int monthPicker) {
        PrefManager prefManager = new PrefManager(context);
        /**
         * vase inke zamani k tarikh payan khali hast vali tarikh now por hast mohasebat kharab mishavad
         * ya bayad tarikh now ham khali shavad
         * ya vaghti tarikh payan khali hast mohasebeyi anjam nashode va 0 return shavad
         */

        int thisYear = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.YEAR);
        int thisMonth = ConvertTimeTo.getInstance().convertJalaliStringToInt(prefManager.getNowDateString(), AppContents.MONTH);

        int endYear = yearPicker;
        int endMonth = monthPicker;

        int year = Math.abs(endYear - thisYear);
        int mount = (endMonth);

        int result;
        if (year == 0)
            result = (endMonth) - thisMonth;
        else
            result = (12 - thisMonth) + ((year - 1) * 12) + mount;

        return result;
    }

    public int positionMiladiEnd(Context context) {
        PrefManager prefManager = new PrefManager(context);
        Calendar calendarNowDate = Calendar.getInstance();

        Calendar calendarEnd = Calendar.getInstance();
        Date date = new Date();
        date.setTime(prefManager.getRange2_Miladi());

        calendarEnd.set(Calendar.YEAR,date.getYear());
        calendarEnd.set(Calendar.MONTH,date.getMonth());
        calendarEnd.set(Calendar.DAY_OF_MONTH,date.getDay());


        int year = Math.abs(calendarEnd.get(Calendar.YEAR) - calendarNowDate.get(Calendar.YEAR));
        int mount = (calendarEnd.get(Calendar.MONTH) );

//        Log.e("positionForTrevlo", prefManager.getRange2()+" "+
//                date.getYear()+" year: now " + calendarNowDate.get(Calendar.YEAR)+
//                " end "+calendarEnd.get(Calendar.YEAR));
//        Log.e("positionForTrevlo", "month: " + mount);

        int result;
        if (year == 0)
            result = (calendarEnd.get(Calendar.MONTH) ) - calendarNowDate.get(Calendar.MONTH);
        else
            result = (12 - calendarNowDate.get(Calendar.MONTH)) + ((year - 1) * 12) + mount;

        return result;
    }

    public int positionMiladiStart(Context context) {
        PrefManager prefManager = new PrefManager(context);
        Calendar calendarNowDate = Calendar.getInstance();

        Calendar calendarStart = Calendar.getInstance();
        Date date = new Date();
        date.setTime(prefManager.getRange1_Miladi());
        calendarStart.set(Calendar.YEAR,date.getYear());
        calendarStart.set(Calendar.MONTH,date.getMonth());
        calendarStart.set(Calendar.DAY_OF_MONTH,date.getDay());

        int year = Math.abs(calendarStart.get(Calendar.YEAR) - calendarNowDate.get(Calendar.YEAR));
        int mount = (calendarStart.get(Calendar.MONTH) );

        Log.e("positionForTrevlo", "position: " + calendarStart.get(Calendar.MONTH) + "     -     " + calendarNowDate.get(Calendar.MONTH) + " month = " + mount);

        int result;
        if (year == 0)
            result = (calendarStart.get(Calendar.MONTH) ) - calendarNowDate.get(Calendar.MONTH);
        else
            result = (12 - calendarNowDate.get(Calendar.MONTH)) + ((year - 1) * 12) + mount;

        return result;
    }
}
