package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to;


/**
 * Created by bob.sun on 15/8/28.
 */
public class MarkStyle {
    public static final int BACKGROUND = 1;
    public static final int TEXT = 5;
    public static final int DEFAULT = 10;

//    public static int defaultColor = Color.rgb(0, 148, 243);

    public static String text;
    public static int textColor;






    private int style;
    private int color;

    public MarkStyle() {
        this.style = MarkStyle.DEFAULT;
//        this.color = MarkStyle.defaultColor;
    }

    public MarkStyle( int color) {
        this.color = color;
    }

    public int getStyle() {
        return style;
    }

    public MarkStyle setStyle(int style) {
        this.style = style;
        return this;
    }

    public int getColor() {
        return color;
    }

    public MarkStyle setColor(int color) {
        this.color = color;
        return this;
    }
}
