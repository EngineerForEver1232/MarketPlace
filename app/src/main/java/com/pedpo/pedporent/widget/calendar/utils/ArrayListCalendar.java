package com.pedpo.pedporent.widget.calendar.utils;

import java.util.ArrayList;
import java.util.Calendar;

public class ArrayListCalendar {

    public static ArrayListCalendar arrayListCalendar = new ArrayListCalendar();

    public ArrayList<JalaliCalendar> listNotAvailableStart = new ArrayList<>();
    public ArrayList<JalaliCalendar> listNotAvailableEND = new ArrayList<>();


    public JalaliCalendar startNotAvaliable = new JalaliCalendar();
    public JalaliCalendar endNotAvailable = new JalaliCalendar();

    public ArrayList<Calendar> hiredStart = new ArrayList<>();
    public ArrayList<Calendar> hiredEND = new ArrayList<>();

    public ArrayList<Calendar> arrayToolsStart = new ArrayList<>();
    public ArrayList<Calendar> arrayToolsEND = new ArrayList<>();

    public JalaliCalendar startAvaliable = null;
    public JalaliCalendar endAvailable = null;

//    public Calendar startAvaliable = Calendar.getInstance();
//    public Calendar endAvailable = Calendar.getInstance();

    public ArrayList<Long> saveAvailableDays2_Click = new ArrayList<>();
    public ArrayList<Long> saveAvailableDays1 = new ArrayList<>();


}
