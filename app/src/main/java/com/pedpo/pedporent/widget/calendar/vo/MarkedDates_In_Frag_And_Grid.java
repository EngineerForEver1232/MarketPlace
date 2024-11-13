package com.pedpo.pedporent.widget.calendar.vo;

import com.pedpo.pedporent.widget.calendar.to.MarkStyle;

import java.util.ArrayList;
import java.util.Observable;

public class MarkedDates_In_Frag_And_Grid extends Observable {
    private static MarkedDates_In_Frag_And_Grid staticInstance= new MarkedDates_In_Frag_And_Grid();
    public ArrayList<DateData> data;

    private MarkedDates_In_Frag_And_Grid() {
        super();
        data = new ArrayList<>();
    }

    public static MarkedDates_In_Frag_And_Grid getInstance() {
//        if (staticInstance == null)
//            staticInstance = new MarkedDates();
        return staticInstance;
    }

    public void refresh() {
        MarkedDates_In_Frag_And_Grid.this.setChanged();
        MarkedDates_In_Frag_And_Grid.this.notifyObservers();
    }

    public MarkStyle check(DateData date) {
        int index = data.indexOf(date);
        if (index == -1) {
            return null;
        }
        return data.get(index).getMarkStyle();
    }

    public boolean remove(DateData date) {
        this.setChanged();
        this.notifyObservers();
        return data.remove(date);

    }

    public MarkedDates_In_Frag_And_Grid add(DateData dateData) {
        data.add(dateData);
        this.setChanged();
        this.notifyObservers();
        return this;
    }


    public ArrayList<DateData> getAll() {
        return data;
    }

    public MarkedDates_In_Frag_And_Grid removeAll() {
        data.clear();
        this.setChanged();
        this.notifyObservers();
        return this;
    }
}
