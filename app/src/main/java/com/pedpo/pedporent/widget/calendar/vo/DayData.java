package com.pedpo.pedporent.widget.calendar.vo;


import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;

/**
 * Created by bob.sun on 15/8/27.
 */
public class DayData {

    private DateData date;
//    private DateDataJalali dataJalali;
    private JalaliCalendar jalaliCalendar ;
    private int textColor;
    private int textSize;
    private  int dayJalali;

    public DayData(){}
    public DayData(DateData date){
        this.date = date;
    }

    public DayData(DateData date,JalaliCalendar jalaliCalendar){
        this.date = date;
        this.jalaliCalendar = jalaliCalendar;
    }

    public JalaliCalendar getJalaliCalendar() {
        return jalaliCalendar;
    }

    public void setJalaliCalendar(JalaliCalendar jalaliCalendar) {
        this.jalaliCalendar = jalaliCalendar;
    }

//    public int getDayJalali() {
//        return dayJalali;
//    }
//
//    public void setDayJalali(int dayJalali) {
//        this.dayJalali = dayJalali;
//    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText(){
        return "" + date.getDay();
    }

    public DateData getDate(){
        return date;
    }

    public void setDate(DateData date) {
        this.date = date;
    }

//    public DateDataJalali getDataJalali() {
//        return dataJalali;
//    }
//
//    public void setDataJalali(DateDataJalali dataJalali) {
//        this.dataJalali = dataJalali;
//    }
}
