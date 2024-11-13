package com.pedpo.pedporent.widget.calendar.to;

public class MarkTO {

    private String date ;
     private int color ;

    public MarkTO() {
    }

    public MarkTO(String date, int color) {
        this.date = date;
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
