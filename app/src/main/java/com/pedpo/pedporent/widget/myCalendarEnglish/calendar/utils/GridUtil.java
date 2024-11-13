package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DayData;

import java.util.Calendar;
import java.util.Date;

public class GridUtil {


    private static GridUtil gridUtil = new GridUtil();

    public static GridUtil newInstance() {
        return gridUtil;
    }

    public void toDay(DayData dayData, TextView textView) {
        if (dayData.getDate().equals(CurrentCalendar.getCurrentDateData())) {

            textView.setTextColor(textView.getContext().getResources().getColor(R.color.black_dark));
            textView.setBackgroundResource(R.drawable.border_item_calendar_primary);

        }
    }

    public boolean clickOff(DayData dayData) {
        Date date1 = new Date(dayData.getDate().getYear(), dayData.getDate().getMonth(), dayData.getDate().getDay());

        Date date2 = new Date(CurrentCalendar.getCurrentDateData().getYear(), CurrentCalendar.getCurrentDateData().getMonth(), CurrentCalendar.getCurrentDateData().getDay());

        if (date1.getTime() < date2.getTime())
            return true;
        else
            return false;
    }

    public boolean kmmToday(DayData dayData,View view, TextView textView, View line1, View line2) {
        Date date1 = new Date(dayData.getDate().getYear(), dayData.getDate().getMonth(), dayData.getDate().getDay());

        Date date2 = new Date(CurrentCalendar.getCurrentDateData().getYear(), CurrentCalendar.getCurrentDateData().getMonth(), CurrentCalendar.getCurrentDateData().getDay());

        if (date1.getTime() < date2.getTime()) {

//            textView.setTextColor(Color.GRAY);
//            textView.setText(dayData.getText());
//            textView.setBackgroundColor(Color.WHITE);

            textView.setTextColor(textView.getContext().getResources().getColor(R.color.gray_cell_calendar_kmm));
            textView.setBackgroundColor(
                    textView.getContext().getResources()
                            .getColor(R.color.cell)
            );
            view.setBackgroundColor(
                    textView.getContext().getResources()
                            .getColor(R.color.cell)
            );
            line1.setBackgroundColor(textView.getContext().getResources()
                    .getColor(R.color.cell));
            line2.setBackgroundColor(textView.getContext().getResources()
                    .getColor(R.color.cell));
            return true;
        }
        return false;
    }

    public void sunDay(DayData dayData, TextView textViewCell) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(dayData.getDate().getYear(), dayData.getDate().getMonth() - 1, dayData.getDate().getDay());
        calendar.setTime(date);

//        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY + 1 || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            //color = RED
//            textViewCell.setTextColor(Color.RED);
//            GridUtil.newInstance().kmmToday(dayData, textViewCell);
//        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY + 1) {
            //color = RED
            textViewCell.setTextColor(Color.RED);
//            GridUtil.newInstance().kmmToday(dayData, textViewCell);
        }
    }

    public void dayIsZero(DayData dayData, TextView textViewCell , View line1, View line2) {


        if (dayData.getDate().getDay() == 0) {
            textViewCell.setText("");
            textViewCell.setBackgroundColor(
                    textViewCell.getContext().getResources()
                            .getColor(R.color.cell)
            );
            line1.setBackgroundColor(textViewCell.getContext().getResources()
                    .getColor(R.color.cell));
            line2.setBackgroundColor(textViewCell.getContext().getResources()
                    .getColor(R.color.cell));
        }

//        if (dayData.getDate().getDay() == 0) {
//            textViewCell.setText("");
//            textViewCell.setBackgroundColor(Color.WHITE);
//        }
    }

    public void singleClick(DayData dayData,View view,
                             TextView textView, Date rang1, Date rang2,
                             View line1, View line2 ) {


        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rangBase.getTime() == rang1.getTime()) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);

//            line2.setBackgroundColor(view.getContext().getResources()
//                    .getColor(R.color.colorPrimaryTransparet));
//            view.setBackgroundColor(view.getContext().getResources()
//                    .getColor(R.color.cell));
        }
    }

    public void rangCalendar(DayData dayData,View view,
                             TextView textView, Date rang1, Date rang2,
                             View line1, View line2 ) {

        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        /** Select aval */
        if (rangBase.getTime() == rang1.getTime()) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);

            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.cell));
        }

        /** range beine range1 & range2 */
        if (rangBase.getTime() >= rang1.getTime() &&
                rang2.getTime() != 0 &&
                rangBase.getTime() <= rang2.getTime()) {
            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.colorPrimaryTransparet));
            textView.setTextColor(Color.WHITE);

            /**
             * vase inke line2 marbot b select aval range select ro begire
             * */
            if (rangBase.getTime() == rang1.getTime()) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);
                line2.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.colorPrimaryTransparet));
                view.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
            }

            /**
             * Select dovom : vase inke line1 marbot b select dovom range select ro begire
             * */
            if (rangBase.getTime() == rang2.getTime()) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.border_item_calendar_end_green);
//            linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_end);
                line1.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.colorPrimaryTransparet));
                view.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
            }

            /** vaghti user 2 selectesh yek tarikh bashad */
            if (rang2.getTime() == rang1.getTime() ) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);

                line2.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
                line1.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
                view.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
            }
        }


    }

    public void rangNoAvaliable(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
        Date rang1 = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)

        );
        Date rang2 = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
                //color = RED
//                textView.setBackgroundColor(Color.RED);
                textView.setBackgroundResource(R.drawable.border_item_calendar_red);
                textView.setTextColor(Color.WHITE);
                textView.setText(dayData.getText());

            }
        }
        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
            //color = RED
//            textView.setBackgroundColor(Color.RED);
            textView.setBackgroundResource(R.drawable.border_item_calendar_red);
            textView.setTextColor(Color.WHITE);
            textView.setText(dayData.getText());

//            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean rangNotAvaliable_For_Click(DayData dayData, Calendar calendar1, Calendar calendar2) {
        Date rang1 = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
        );
        Date rang2 = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
            return true;
        }
        return false;
    }

    public boolean notAvaliable_For_Click2(Date click1, Date click2, Calendar calendar1, Calendar calendar2) {
        Date notDateStart = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
        );
        Date notDateEnd = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );

        if (click1.getTime() < notDateStart.getTime() && click2.getTime() > notDateEnd.getTime()) {
            return true;
        }
        return false;
    }


    public boolean notAvaliable_For_Click2_Previous(Date click1, Date click2, Calendar calendar1, Calendar calendar2) {
        Date notDateStart = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)
        );
        Date notDateEnd = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );

        if (click1.getTime() > notDateEnd.getTime() && click2.getTime() < notDateStart.getTime()) {
            return true;
        }

        return false;
    }


    public void rangAvaliable(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
        Date rang1 = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)

        );
        Date rang2 = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
                //color = Gray
//                textView.setBackgroundColor(Color.parseColor("#bfbfbf"));
                textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
//                textView.setTextColor(Color.WHITE);
                textView.setText(dayData.getText());

            }
        }
        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {

            //color = Gray
//            textView.setBackgroundColor(Color.parseColor("#bfbfbf"));
            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);

//            textView.setTextColor(Color.WHITE);
            textView.setText(dayData.getText());

//            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean rangAvaliableForClick(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
        Date startEvaliable = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)

        );
        Date endAvaliable = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rangBase.getTime() >= startEvaliable.getTime() && rangBase.getTime() <= endAvaliable.getTime()) {

            return false;
        }
        return true;
    }

    public boolean rangAvaliableForEditText(DayData dayData, Calendar calendar1, Calendar calendar2) {
        Date startEvaliable = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)

        );
        Date endAvaliable = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rangBase.getTime() >= startEvaliable.getTime() && rangBase.getTime() <= endAvaliable.getTime()) {

            return false;
        }
        return true;
    }


    public void rangHired(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
        Date rang1 = new Date(
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH)

        );
        Date rang2 = new Date(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH)
        );
        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
                textView.setBackgroundColor(Color.BLUE);
                textView.setTextColor(Color.WHITE);
                textView.setText(dayData.getText());

            }
        }
        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {

            textView.setBackgroundColor(Color.BLUE);
            textView.setTextColor(Color.WHITE);
            textView.setText(dayData.getText());

//            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
        }
    }
}
