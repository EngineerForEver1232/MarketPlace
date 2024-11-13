package com.pedpo.pedporent.widget.calendar.vo.jalali;

import com.pedpo.pedporent.widget.calendar.to.MarkStyle;

import java.util.ArrayList;
import java.util.Observable;

public class MarkedDates_In_CalendarAc extends Observable {
    private static MarkedDates_In_CalendarAc staticInstance;
    public ArrayList<DateDataJalali> data;

    private MarkedDates_In_CalendarAc(){
        super();
        data = new ArrayList<>();
    }

    public static MarkedDates_In_CalendarAc getInstance(){
        if (staticInstance == null)
            staticInstance = new MarkedDates_In_CalendarAc();
        return staticInstance;
    }

    public void refresh()
    {
        this.setChanged();
        this.notifyObservers();
    }

    public MarkStyle check(DateDataJalali date){
        int index = data.indexOf(date);
        if (index == -1) {
            return null;
        }
        return data.get(index).getMarkStyle();
    }

    public boolean remove(DateDataJalali date){
        this.setChanged();
        this.notifyObservers();
        return data.remove(date);

    }

    public MarkedDates_In_CalendarAc add(DateDataJalali dateData){
        data.add(dateData);
        this.setChanged();
        this.notifyObservers();
        return this;
    }


    public ArrayList<DateDataJalali> getAll(){
        return data;
    }

    public MarkedDates_In_CalendarAc removeAll(){
        data.clear();
        this.setChanged();
        this.notifyObservers();
        return this;
    }
}
