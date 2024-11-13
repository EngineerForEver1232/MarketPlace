package com.pedpo.pedporent.widget.calendar.utils;

import android.annotation.SuppressLint;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ConvertDate {

    private ConvertDate() {
    }

    private static ConvertDate convertDate = new ConvertDate();

    public static ConvertDate newInstance() {
        return convertDate;
    }

    public static String stringDate(String date) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return parseFormat.format(parseFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd'T'hh:mm:ss";
//        String outputPattern = "dd-MMM-yyyy h:mm a";
        String outputPattern = "dd-MMM-yyyy h:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public String getDate2(String date) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-mm-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(parseFormat.parse(date));
            return parseFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }


    public String toPersianDateNumber(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendarStart = Calendar.getInstance();
        try {
            calendarStart.setTime(new Date(format.parse(date).getTime()));
        } catch (Exception e) {
        }

        GregorianCalendar gregorianCalendar = (GregorianCalendar) calendarStart;

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.set(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DATE));
        jalaliCalendar.fromGregorian(gregorianCalendar);

        String customStartDate = String.format("%04d-%02d-%02d",
                jalaliCalendar.getYear() ,
                jalaliCalendar.getMonth() ,
                jalaliCalendar.getDay()
        );
        return customStartDate;
    }

    public String getFormatMiladi(Calendar date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date.getTime());

        return dateString;
    }

    public String getDate_And_FormatMonth(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendarStart = Calendar.getInstance();
        try {
            calendarStart.setTime(new Date(format.parse(date).getTime()));
        } catch (Exception e) {
        }

        GregorianCalendar gregorianCalendar = (GregorianCalendar) calendarStart;

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.set(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DATE));
        jalaliCalendar.fromGregorian(gregorianCalendar);
        String customStartDate =
                NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getDay() + "") + " " +
                        jalaliCalendar.getMonthString() + " " +
                        NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getYear() + "");

        return customStartDate;
    }

    public String persianDate_FormatMonth_time(String time) {
        String inputPattern = "yyyy-MM-dd'T'hh:mm:ss";
//        String outputPattern = "dd-MMM-yyyy h:mm a";
        String outputPattern = "dd-MMM-yyyy";
        String timePattern = "h:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat timeFormat = new SimpleDateFormat(timePattern);

        Calendar calendarStart = Calendar.getInstance();

        Date date = null;
        String strDate = null;
        String strTime = null;

        try {
            date = inputFormat.parse(time);
            strDate = outputFormat.format(date);
            strTime = NumberFormatPersian.getNewInstance().toPersianNumbersSample(timeFormat.format(date));
            calendarStart.setTime(new Date(outputFormat.parse(strDate).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar gregorianCalendar = (GregorianCalendar) calendarStart;
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.set(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DATE));
        jalaliCalendar.fromGregorian(gregorianCalendar);
        String customStartDate =
                NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getDay() + "") + " " +
                        jalaliCalendar.getMonthString() + " " +
                        NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getYear() + "");

        return customStartDate +" ، "+strTime;
    }

    public String show(String time) {
        String inputPattern = "yyyy-MM-dd'T'hh:mm:ss.SSSSSSS Z";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        String outputPattern = "dd-MMM-yyyy h:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time +" +0000");
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String formatMonth_day_forInbox(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendarStart = Calendar.getInstance();
        try {
            calendarStart.setTime(new Date(format.parse(date).getTime()));
        } catch (Exception e) {
        }

        GregorianCalendar gregorianCalendar = (GregorianCalendar) calendarStart;

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.set(calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH) + 1, calendarStart.get(Calendar.DATE));
        jalaliCalendar.fromGregorian(gregorianCalendar);
        String customStartDate =
                NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getDay() + "") + " " +
                        jalaliCalendar.getMonthString();
        return customStartDate;
    }


}
