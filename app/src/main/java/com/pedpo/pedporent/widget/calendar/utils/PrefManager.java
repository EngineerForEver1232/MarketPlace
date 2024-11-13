package com.pedpo.pedporent.widget.calendar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pedpo.pedporent.utills.ContextApp;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.ContentsCalendarEN;

public class PrefManager {

    private SharedPreferences sharedPreferences;

    private final static String TYPE_CALENDAR = "typeCalendar";

    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(AppContentsCalendar.NAME_PREF, Context.MODE_PRIVATE);
    }

    public void setMiladiStartRange(long miladiStartRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.MILADI_START_RANGE, miladiStartRange).apply();
    }

    public long getMiladiStartRange() {
        return sharedPreferences.getLong(AppContentsCalendar.MILADI_START_RANGE, 0);
    }

    public void setMiladiEndRange(long miladiEndRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.MILADI_END_RANGE, miladiEndRange).apply();
    }

    public long getMiladiEndRange() {
        return sharedPreferences.getLong(AppContentsCalendar.MILADI_END_RANGE, 0);
    }

    public void setJalaliStartRange(long jalaliStartRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.JALALI_START_RANGE, jalaliStartRange).apply();
    }

    public long getJalaliStartRange() {
        return sharedPreferences.getLong(AppContentsCalendar.JALALI_START_RANGE, 0);
    }

    public void setRange1(long jalaliStartRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.JALALI_START_RANGE_DAYDATA, jalaliStartRange).apply();
    }

    public long getRange1() {
        return sharedPreferences.getLong(AppContentsCalendar.JALALI_START_RANGE_DAYDATA, 0);
    }

    public void setRange2(long endRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.JALALI_END_RANGE_DAYDATA, endRange).apply();
    }

    public long getRange2() {
        return sharedPreferences.getLong(AppContentsCalendar.JALALI_END_RANGE_DAYDATA, 0);
    }


    public void setJalaliEndRange(long endRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.JALALI_END_RANGE, endRange).apply();
    }

    public long getJalaliEndRange() {
        return sharedPreferences.getLong(AppContentsCalendar.JALALI_END_RANGE, AppContentsCalendar.ZERO_CALENDAR);
    }

//    public void setDateFormatMiladiStart(String dateStringStart) {
//        sharedPreferences.edit().putString(AppContentsCalendar.DATE_FORMAT_START, dateStringStart).apply();
//    }

    public String getDateFormatStart() {
        return sharedPreferences.getString(AppContentsCalendar.DATE_FORMAT_START, "");
    }

    public void setDateFormatJalaliStart(String dateStringJalaliStart) {
        sharedPreferences.edit().putString(AppContentsCalendar.DATE_JALALI_FORMAT_START, dateStringJalaliStart).apply();
    }

    public String getFormatStart_Jalali() {
        return sharedPreferences.getString(AppContentsCalendar.DATE_JALALI_FORMAT_START, "");
    }

//    public void setDateFormatMiladiEnd(String dateFormat) {
//        sharedPreferences.edit().putString(AppContentsCalendar.DATE_FORMAT_END, dateFormat).apply();
//    }
//
//    public String getDateFormatMiladiEnd() {
//        return sharedPreferences.getString(AppContentsCalendar.DATE_FORMAT_END, "");
//    }

    public void setDateFormatJalaliEnd(String dateFormat) {
        sharedPreferences.edit().putString(AppContentsCalendar.DATE_JALALI_FORMAT_END, dateFormat).apply();
    }

    public String getFormatEnd_Jalali() {
        return sharedPreferences.getString(AppContentsCalendar.DATE_JALALI_FORMAT_END, "");
    }

    public void setRangeBoolean(boolean bool) {
        sharedPreferences.edit().putBoolean("rangeBoolean", bool).apply();
    }

    public boolean getRangeBoolean() {
        return sharedPreferences.getBoolean("rangeBoolean", true);
    }


    public void setClick(boolean bool) {
        sharedPreferences.edit().putBoolean("click", bool).apply();
    }

    public boolean getClick() {
        return sharedPreferences.getBoolean("click", false);
    }

    public void setClickAvaliable(boolean bool) {
        sharedPreferences.edit().putBoolean("clickAvaliable", bool).apply();
    }

    public boolean getClickAvaliable() {
        return sharedPreferences.getBoolean("clickAvaliable", false);
    }

    public void setBoolClick1ForNotDate(boolean bool) {
        sharedPreferences.edit().putBoolean("boolClick1ForNotDate", bool).apply();
    }

    public boolean getBoolClick1ForNotDate() {
        return sharedPreferences.getBoolean("boolClick1ForNotDate", false);
    }

    public void setNowDateLong(long nowDate) {
        sharedPreferences.edit().putLong("dateNow", nowDate).apply();
    }

    public long getNowDateLong() {
        return sharedPreferences.getLong("dateNow", 0);
    }

    public void setNowDateString(String nowDate) {
        sharedPreferences.edit().putString("dateNow", nowDate).apply();
    }

    public String getNowDateString() {
        return sharedPreferences.getString("dateNow", "");
    }


    /*****************************************
     ***          M I L A D I            ***
     *****************************************/


    public void setRange1_Miladi(long startDate) {
        sharedPreferences.edit().putLong(AppContentsCalendar.RANGE_START_MILADI, startDate).apply();
    }

    public long getRange1_Miladi() {
        return sharedPreferences.getLong(AppContentsCalendar.RANGE_START_MILADI, 0);
    }


    public void setRange2_Miladi(long endRange) {
        sharedPreferences.edit().putLong(AppContentsCalendar.RANGE_END_MILADI, endRange).apply();
    }

    public long getRange2_Miladi() {
        return sharedPreferences.getLong(AppContentsCalendar.RANGE_END_MILADI, 0);
    }

    public void setFormatStart_Miladi(String dateStringStart)
    {
        sharedPreferences.edit().putString(ContentsCalendarEN.FORMAT_START_MILADI, dateStringStart).apply();
    }

    public String getFormatStart_Miladi()
    {
        return sharedPreferences.getString(ContentsCalendarEN.FORMAT_START_MILADI, "");
    }
    public void setFormatEnd_Miladi(String dateFormat)
    {
        sharedPreferences.edit().putString(ContentsCalendarEN.FORMAT_END_MILADI, dateFormat).apply();
    }

    public String getFormatEnd_Miladi()
    {
        return sharedPreferences.getString(ContentsCalendarEN.FORMAT_END_MILADI,"");
    }

    public void singleClick(boolean bool)
    {
        sharedPreferences.edit().putBoolean("singleClick", bool).apply();
    }
    public Boolean isSingleClick()
    {
        return sharedPreferences.getBoolean("singleClick", false);
    }

    public void setCalendar(String cal) {
        sharedPreferences.edit().putString(TYPE_CALENDAR,cal).apply();
    }

    public String getCalendar() {
        return sharedPreferences.getString(TYPE_CALENDAR, ContextApp.SHAMSI);
    }

    public void clear() {
        setRangeBoolean(true);

        setJalaliStartRange(0);
        setJalaliEndRange(0);
        setDateFormatJalaliStart("");
        setDateFormatJalaliEnd("");
        setRange1(0);
        setRange2(0);

        setMiladiStartRange(0);
        setMiladiEndRange(0);
        setFormatStart_Miladi("");
        setFormatEnd_Miladi("");



    }

}
