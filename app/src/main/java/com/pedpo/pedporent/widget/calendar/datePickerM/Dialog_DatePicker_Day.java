package com.pedpo.pedporent.widget.calendar.datePickerM;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import androidx.appcompat.app.AlertDialog;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.utils.datepicker.PersianCalendarUtils;

public class Dialog_DatePicker_Day implements View.OnClickListener {

    private OnDateChangedListener onDateChangedListener;
    private Button btnApply;
    private ImageButton imgClose;
    private AlertDialog dialog;
    private NumberPicker yearNumberPicker;
    private NumberPicker monthNumberPicker;
    private NumberPicker dayNumberPicker;

    private int minYear;
    private int maxYear;
    private int yearRangeMin;
    private int yearRangeMax;
    private int yearSelector;

    public Dialog_DatePicker_Day()
    {
        this.yearRangeMin = 50 ;
        this.yearRangeMax = 50 ;
        this.yearSelector = 0 ;
    }
    public Dialog_DatePicker_Day(int yearRangeMin , int yearRangeMax , int yearSelector)
    {
        this.yearRangeMin = yearRangeMin ;
        this.yearRangeMax = yearRangeMax ;
        this.yearSelector = yearSelector ;
    }

    public void showDialog(Context context) {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.datepicker_sh, null);
        builder.setView(view);
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);
        btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        yearNumberPicker = view.findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setWrapSelectorWheel(false);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthNumberPicker = view.findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setWrapSelectorWheel(false);
        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        dayNumberPicker = view.findViewById(R.id.dayNumberPicker);
        dayNumberPicker.setWrapSelectorWheel(false);
        dayNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        TypedArray a = context.obtainStyledAttributes(null, R.styleable.PersianDatePicker, 0, 0);

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        /*
         * Set timezone
         */
//        PersianCalendar pCalendar;
//        timezone = a.getString(R.styleable.PersianDatePicker_timezone);
//        if( timezone == null || timezone.isEmpty()) {
//            pCalendar = new PersianCalendar();
//        } else {
//            pCalendar = new PersianCalendar(TimeZone.getTimeZone(timezone));
//        }

//        yearRange = a.getInteger(R.styleable.PersianDatePicker_yearRange, 10);

        /*
         * Initializing yearNumberPicker min and max values If minYear and
         * maxYear attributes are not set, use (current year - 10) as min and
         * (current year + 10) as max.
         */
        minYear = a.getInt(R.styleable.PersianDatePicker_minYear, jalaliCalendar.getYear() + yearRangeMin);
        maxYear = a.getInt(R.styleable.PersianDatePicker_maxYear, jalaliCalendar.getYear() + yearRangeMax);
        yearNumberPicker.setMinValue(minYear);
        yearNumberPicker.setMaxValue(maxYear);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        int selectedYear = a.getInt(R.styleable.PersianDatePicker_selectedYear, jalaliCalendar.getYear()+yearSelector);
        if (selectedYear > maxYear || selectedYear < minYear) {
            throw new IllegalArgumentException(String.format("Selected year (%d) must be between minYear(%d) and maxYear(%d)", selectedYear, minYear, maxYear));
        }
        yearNumberPicker.setValue(selectedYear);
        yearNumberPicker.setOnValueChangedListener(dateChangeListener);

        /*
         * initializng monthNumberPicker
         */
        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        int selectedMonth = a.getInteger(R.styleable.PersianDatePicker_selectedMonth, jalaliCalendar.getMonth());
        if (selectedMonth < 1 || selectedMonth > 12) {
            throw new IllegalArgumentException(String.format("Selected month (%d) must be between 1 and 12", selectedMonth));
        }
        monthNumberPicker.setValue(selectedMonth);
        monthNumberPicker.setOnValueChangedListener(dateChangeListener);



        /*
         * initializiing dayNumberPicker
         */
        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        int selectedDay = a.getInteger(R.styleable.PersianDatePicker_selectedDay, jalaliCalendar.getDay());
        if (selectedDay > 31 || selectedDay < 1) {
            throw new IllegalArgumentException(String.format("Selected day (%d) must be between 1 and 31", selectedDay));
        }
        if (selectedMonth > 6 && selectedMonth < 12 && selectedDay == 31) {
            selectedDay = 30;
        } else {
            boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(selectedYear);
            if (isLeapYear && selectedDay == 31) {
                selectedDay = 30;
            } else if (selectedDay > 29) {
                selectedDay = 29;
            }
        }
        dayNumberPicker.setValue(selectedDay);
        dayNumberPicker.setOnValueChangedListener(dateChangeListener);

        a.recycle();

        dialog = builder.create();
        Dialog d = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        dialog.show();


    }


    NumberPicker.OnValueChangeListener dateChangeListener = new NumberPicker.OnValueChangeListener() {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            int year = yearNumberPicker.getValue();
            boolean isLeapYear = PersianCalendarUtils.isPersianLeapYear(year);

            int month = monthNumberPicker.getValue();
            int day = dayNumberPicker.getValue();

            if (month < 7) {
                dayNumberPicker.setMinValue(1);
                dayNumberPicker.setMaxValue(31);
            } else if (month > 6 && month < 12) {
                if (day == 31) {
                    dayNumberPicker.setValue(30);
                }
                dayNumberPicker.setMinValue(1);
                dayNumberPicker.setMaxValue(30);
            } else if (month == 12) {
                if (isLeapYear) {
                    if (day == 31) {
                        dayNumberPicker.setValue(30);
                    }
                    dayNumberPicker.setMinValue(1);
                    dayNumberPicker.setMaxValue(30);
                } else {
                    if (day > 29) {
                        dayNumberPicker.setValue(29);
                    }
                    dayNumberPicker.setMinValue(1);
                    dayNumberPicker.setMaxValue(29);
                }
            }


        }

    };

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }

    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param newYear  The year that was set.
         * @param newMonth The month that was set (1-12)
         * @param newDay   The day of the month that was set.
         */
        void onDateChanged(int newYear, int newMonth, int newDay);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imgClose:
                if (dialog != null)
                    dialog.dismiss();
                break;
            case R.id.btnApply:
                if (dialog != null)
                    dialog.dismiss();
                if (onDateChangedListener != null)
                    onDateChangedListener.onDateChanged(yearNumberPicker.getValue(), monthNumberPicker.getValue(), dayNumberPicker.getValue());
                break;
        }

    }

    public void setMinYearMin(int yearRangeMin)
    {
        this.yearRangeMin = yearRangeMin ;
    }
    public void setMinYearMax(int yearRangeMax)
    {
        this.yearRangeMax = yearRangeMax ;
    }

    public void setYearSelector(int yearSelector)
    {
        this.yearSelector = yearSelector;
    }

}
