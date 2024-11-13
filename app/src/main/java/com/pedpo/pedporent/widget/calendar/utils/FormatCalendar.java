package com.pedpo.pedporent.widget.calendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FormatCalendar {

    private static FormatCalendar formatCalendar = new FormatCalendar();

    public static FormatCalendar getInstance() {
        return formatCalendar;
    }


    public JalaliCalendar getFormatMiladiToJalali(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcalendar = new GregorianCalendar();
        try {
            gcalendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
        }
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        jalaliCalendar.fromGregorian(gcalendar);

        return jalaliCalendar;
    }

//    public Calendar getFormatMiladiToJalali(String dateString) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        GregorianCalendar gcalendar = new GregorianCalendar();
//        try {
//            gcalendar.setTime(sdf.parse(dateString));
//        } catch (ParseException e) {
//        }
//        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.fromGregorian(gcalendar);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(jalaliCalendar.getYear(), jalaliCalendar.getMonth(), jalaliCalendar.getDay());
//
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendar);
//
//        Log.e("testServer", "getView: " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
//        return calendar;
//    }

    public String formatMiladi(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcalendar = new GregorianCalendar();
        try {
            gcalendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
        }
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        jalaliCalendar.set(gcalendar.get(Calendar.YEAR), gcalendar.get(Calendar.MONTH) + 1, gcalendar.get(Calendar.DATE));

        GregorianCalendar gregorianCalendar =jalaliCalendar.toGregorian();



        String miladiFormat =
                String.format("%04d",gregorianCalendar.get(Calendar.YEAR))  +"-"+
                String.format("%02d", (gregorianCalendar.get(Calendar.MONTH)+1)) +
                "-"+
                String.format("%02d",gregorianCalendar.get(Calendar.DATE));

        return miladiFormat;
    }

    public Calendar toMiladi(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcalendar = new GregorianCalendar();
        try {
            gcalendar.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
        }
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        jalaliCalendar.set(gcalendar.get(Calendar.YEAR), gcalendar.get(Calendar.MONTH) + 1, gcalendar.get(Calendar.DATE));

        GregorianCalendar gregorianCalendar =jalaliCalendar.toGregorian();

        Calendar calendar = Calendar.getInstance();
        calendar.set(
                gregorianCalendar.get(Calendar.YEAR),
                (gregorianCalendar.get(Calendar.MONTH)),
                gregorianCalendar.get(Calendar.DATE));

        return calendar;
    }
    public String formatMiladi_To_Miladi(Calendar dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar gcalendar = dateString;
//        try {
//            gcalendar.setTime(sdf.parse(dateString));
//        } catch (ParseException e) {
//        }


        String miladiFormat =
                String.format("%04d",gcalendar.get(Calendar.YEAR))  +"-"+
                String.format("%02d", (gcalendar.get(Calendar.MONTH)+1)) +
                "-"+
                String.format("%02d",gcalendar.get(Calendar.DATE));

        return miladiFormat;
    }
//    public String formatMiladi(String dateString) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        GregorianCalendar gcalendar = new GregorianCalendar();
//        try {
//            gcalendar.setTime(sdf.parse(dateString));
//        } catch (ParseException e) {
//        }
//        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        jalaliCalendar.set(gcalendar.get(Calendar.YEAR), gcalendar.get(Calendar.MONTH) + 1, gcalendar.get(Calendar.DATE));
//
//        GregorianCalendar gregorianCalendar =jalaliCalendar.toGregorian();
//        Log.e("returnDate", "jalali to miladi: " + gregorianCalendar.get(Calendar.YEAR) + " , " + (gregorianCalendar.get(Calendar.MONTH))+" , "+gregorianCalendar.get(Calendar.DATE));
//
//        String miladiFormat = gregorianCalendar.get(Calendar.YEAR)+"-"+(gregorianCalendar.get(Calendar.MONTH)+1)+"-"+gregorianCalendar.get(Calendar.DATE);
//
//        return miladiFormat;
//    }


}
