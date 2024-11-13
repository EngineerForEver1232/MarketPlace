package com.pedpo.pedporent.widget.calendar.datePickerM;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.widget.calendar.interfaceCalendar.OnclickDatePicker;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian;

import java.util.ArrayList;

public class Dialog_DatePicker implements View.OnClickListener {

    private Button btnApply;
    private AlertDialog dialog;
    private NumberPicker numberPickerYear;
    private NumberPicker numberPickerMonth;
    private OnclickDatePicker onclickDatePicker;

    private static Dialog_DatePicker dialog_datePicker = new Dialog_DatePicker();

    public static Dialog_DatePicker newInstance() {
        return dialog_datePicker;
    }

    public void showDialog(Context context) {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker_persian, null);
        builder.setView(view);
        numberPickerYear = view.findViewById(R.id.numberPickerYear);
        numberPickerMonth = view.findViewById(R.id.numberPickerMonth);
        btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        numberPickerYear.setMinValue(jalaliCalendar.getYear());
        numberPickerYear.setMaxValue(jalaliCalendar.getYear() + 30);
        numberPickerYear.setWrapSelectorWheel(false);
        numberPickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPickerYear.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return NumberFormatPersian.getNewInstance().toPersianNumbersSample(value+"");
            }
        });


        String[] monthString = context.getResources().getStringArray(R.array.month);

        numberPickerMonth.setMinValue(jalaliCalendar.getMonth());
        ArrayList<String> arrayList = new ArrayList<>();
        for (int z = jalaliCalendar.getMonth() - 1; z < 12; z++) {
            arrayList.add(monthString[z]);
            Log.e("onClick", "onClick: " + monthString[z]);
        }
        String[] newMonth = arrayList.toArray(new String[arrayList.size()]);
        numberPickerMonth.setDisplayedValues(newMonth);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(jalaliCalendar.getMonth());
        numberPickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPickerMonth.setWrapSelectorWheel(false);
//        numberPickerMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                numberPickerMonth.setDisplayedValues(null);
//                numberPickerMonth.setMinValue(jalaliCalendar.getMonth());
//                numberPickerMonth.setMaxValue(12);
//                numberPickerMonth.setValue(jalaliCalendar.getMonth());
//                numberPickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//                numberPickerMonth.setWrapSelectorWheel(false);
//            }
//        });

        numberPickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (numberPicker.getValue() == jalaliCalendar.getYear()) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int z = jalaliCalendar.getMonth() - 1; z < 12; z++) {
                        arrayList.add(monthString[z]);
                        Log.e("onClick", "onClick: " + monthString[z]);
                    }
                    numberPickerMonth.setMinValue(jalaliCalendar.getMonth());
                    String[] newMonth = arrayList.toArray(new String[arrayList.size()]);
                    numberPickerMonth.setDisplayedValues(newMonth);
                    numberPickerMonth.setMaxValue(12);
                    numberPickerMonth.setValue(jalaliCalendar.getMonth());
                    numberPickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    numberPickerMonth.setWrapSelectorWheel(false);

                } else {
                    numberPickerMonth.setDisplayedValues(monthString);
                    numberPickerMonth.setMinValue(1);
                    numberPickerMonth.setMaxValue(12);
//                    numberPickerMonth.setValue(jalaliCalendar.getMonth());
                    numberPickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    numberPickerMonth.setWrapSelectorWheel(false);

                }
            }
        });

        dialog = builder.create();
        dialog.show();

//        Window window = dialog.getWindow();
//        window.setLayout((int)context.getResources().getDimension(R.dimen.width_dialog), WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (500f);
        layoutParams.width = dialogWindowWidth;
        dialog.getWindow().setAttributes(layoutParams);

    }

    @Override
    public void onClick(View view) {
        if (onclickDatePicker != null) {
            onclickDatePicker.onClickDatePicker(view, numberPickerYear.getValue(), numberPickerMonth.getValue());
            dialog.dismiss();
        }
    }

    public void setOnclickDatePicker(OnclickDatePicker onclickDatePicker) {
        this.onclickDatePicker = onclickDatePicker;
    }


}
