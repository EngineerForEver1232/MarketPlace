package com.pedpo.pedporent.widget.calendar.utils;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.widget.calendar.vo.DayData;

import java.util.Calendar;
import java.util.Date;

public class GridUtil {


    private static GridUtil gridUtil = new GridUtil();

    public static GridUtil newInstance() {
        return gridUtil;
    }

    public void toDay(DayData dayData, TextView textView) {
        if (dayData.getJalaliCalendar() != null)
            if (dayData.getJalaliCalendar().equals(CurrentCalendar.getCurrentJalali())) {
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.black_dark));
                textView.setBackgroundResource(R.drawable.border_item_calendar_primary);
            }
    }

    public boolean clickOff(DayData dayData) {
        if (dayData.getJalaliCalendar() != null) {
            Date date1 = new Date(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
            Date date2 = new Date(CurrentCalendar.getCurrentJalali().getYear(), CurrentCalendar.getCurrentJalali().getMonth(), CurrentCalendar.getCurrentJalali().getDay());

            if (date1.getTime() < date2.getTime()) {
                return true;
            }
        }
        return false;
    }

    public boolean kmmToday(DayData dayData,View view, TextView textView, View line1, View line2) {
        if (dayData.getJalaliCalendar() != null) {
            Calendar calendarKmm = Calendar.getInstance();
            calendarKmm.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
            ConvertTimeTo.getInstance().CovertTimeToZero(calendarKmm);
            Calendar calendarNow = Calendar.getInstance();
            ConvertTimeTo.getInstance().CovertTimeToZero(calendarNow);
            calendarNow.set(CurrentCalendar.getCurrentJalali().getYear(), CurrentCalendar.getCurrentJalali().getMonth(), CurrentCalendar.getCurrentJalali().getDay());

            if (calendarKmm.getTimeInMillis() < calendarNow.getTimeInMillis()) {
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
        }
        return false;
    }

    public void sunDay(DayData dayData, TextView textViewCell) {
        if (dayData.getJalaliCalendar() != null) {
            if (dayData.getJalaliCalendar().getDayOfWeekString().equals(AppContentsCalendar.FRIDAY)) {
                //color = RED
                textViewCell.setTextColor(Color.RED);
//                GridUtil.newInstance().kmmToday(dayData, textViewCell);
            }
        }
    }

    public void dayIsZero(DayData dayData, TextView textViewCell , View line1, View line2) {


        if (dayData.getJalaliCalendar().getDay() == 0) {
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
    }

    public void rangCalendar(
            DayData dayData, View view, TextView textView,
             long rang1, long rang2,
             View line1 , View line2) {

        long initDate = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

        rangCalendar(initDate,view,textView,rang1,rang2,line1,line2);

    }

    public void rangCalendar(Long initDate, View view, TextView textView,
                              long rang1, long rang2,
                              View line1 , View line2){
        /** Select aval */
        if (initDate == rang1) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);

            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.cell));
        }


        if (initDate >= rang1 &&
                rang2 != 0 &&
                initDate <= rang2) {
            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.colorPrimaryTransparet));
            textView.setTextColor(Color.WHITE);

            /**
             * vase inke line2 marbot b select aval range select ro begire
             * */
            if (initDate == rang1) {
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
            if (initDate == rang2) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.border_item_calendar_end_green);
//            linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_end);
                line1.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.colorPrimaryTransparet));
                view.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
            }

            /** vaghti user 2 selectesh yek tarikh bashad */
            if (rang2 == rang1 ) {
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

    public void rangAvaliableJalali(DayData dayData, TextView textView, long calendar1, long calendar2) {

        long calendarBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

        if (calendarBase != 0 && calendar1 != 0 && calendarBase == calendar1) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
            /*** color cell*/
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
            if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays1.contains(calendarBase))
                ArrayListCalendar.arrayListCalendar.saveAvailableDays1.add(calendarBase);
        }
        if (calendarBase != 0 && calendar1 != 0 && calendar2 != 0 &&
                calendarBase >= calendar1 &&
                calendarBase <= calendar2) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
            /*** color cell*/
            textView.setBackgroundColor(Color.parseColor("#E6E5E5"));
            if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays1.contains(calendarBase))
                ArrayListCalendar.arrayListCalendar.saveAvailableDays1.add(calendarBase);
        }

        if (calendarBase != 0 && calendar1 != 0 && calendarBase == calendar1)
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_gray);

        if (calendarBase != 0 && calendar1 != 0 && calendarBase == calendar2)
            textView.setBackgroundResource(R.drawable.border_item_calendar_end_gray);

    }

    public void rangNoAvaliableJalali(DayData dayData,View view, TextView textView,
                                      View line1 , View line2,ConstraintLayout linearCell, long range1, long range2) {

        long calendarBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());
        long initDate = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

        if (initDate >= range1 &&
                range2 != 0 &&
                initDate <= range2) {
            /*** color cell*/
            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.colorPrimaryTransparet));

        }


//        if (calendar2 == 0 || calendar2 < calendar1) {
//            if (calendarBase >= calendar1 && calendarBase <= calendar1) {
//
//                textView.setTextColor(Color.WHITE);
//                textView.setBackgroundResource(R.drawable.border_item_calendar_end_green);
////                linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_end);
//                line1.setBackgroundColor(view.getContext().getResources()
//                        .getColor(R.color.colorPrimaryTransparet));
//                view.setBackgroundColor(view.getContext().getResources()
//                        .getColor(R.color.cell));
//            }
//        }
//        if (calendarBase >= calendar1 && calendarBase <= calendar2) {
//
//            textView.setTextColor(Color.WHITE);
//            textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);
////            linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_start);
//            line2.setBackgroundColor(view.getContext().getResources()
//                    .getColor(R.color.red_error));
//            view.setBackgroundColor(view.getContext().getResources()
//                    .getColor(R.color.cell));
//
//        }

        if (initDate == range1) {
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.border_item_calendar_start_green);
//            linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_start);
            line2.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.colorPrimaryTransparet));
            view.setBackgroundColor(view.getContext().getResources()
                    .getColor(R.color.cell));
        }
        if (initDate == range2) {
            if (calendarBase >= range1 && calendarBase <= range2) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.border_item_calendar_end_green);
//            linearCell.setBackgroundResource(R.drawable.cell_boder_selector_back_end);
                line1.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.colorPrimaryTransparet));
                view.setBackgroundColor(view.getContext().getResources()
                        .getColor(R.color.cell));
            }
        }


    }

    public boolean rangAvaliableForClickJalali(DayData dayData, long calendar1, long calendar2) {

        long calendarBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

        if (calendarBase >= calendar1 && calendarBase <= calendar2) {
            return false;
        }
        return true;
    }

    public boolean rangNotAvaliable_For_Click_Jalali(DayData dayData, long calendar1, long calendar2) {

        long calendarBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

        if (calendarBase >= calendar1 && calendarBase <= calendar2) {
            return true;
        }
        return false;
    }

    public boolean notAvaliable_For_Click2(long click1, long click2, long calendarNotAvailableStart, long calendarNotAvailableEnd) {

        if (click1 < calendarNotAvailableStart && click2 > calendarNotAvailableEnd) {
            return true;
        }
        return false;
    }


    public void showAvaliable_AFter_Click(DayData dayData, TextView textViewCell, long rang1) {

//        if (CalendarActivity.sharedPreferences.getBoolean("boolClick1ForNotDate", false))
        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {

            long startAvailable = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.startAvaliable);
            long endAvailable = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.endAvailable);
            long notDateStartInner = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i));

            long notDateEndPrevious = 0;
            if (i != 0 && ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1) != null)
                notDateEndPrevious = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1));

            long rangBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

            textViewCell.setBackgroundColor(Color.WHITE);
//            textViewCell.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textViewCell.setTextColor(Color.GRAY);

            //نشان دادن روزهای در دسترس که بعد از آن notAvaliable  وجود ندارد
            if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty()) {
                long firstNotDateStart = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(0));
                long lastNotDateEnd = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.size() - 1));

                /* ghable avalin NotAvaliable */
                if (rang1 < firstNotDateStart) {
                    if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty() &&
                            rangBase >= startAvailable &&
                            firstNotDateStart != 0 &&
                            rangBase < firstNotDateStart) {
                        /*** color cell*/
                        textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
                        textViewCell.setTextColor(Color.BLACK);

//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);
//
//                        //change color start && end range
//                        if (rangBase == startAvailable)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                        if (rangBase == firstNotDateStart - 1)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);

                    }
                    /* bad az akharin NotAvaliable */
                } else if (rang1 > lastNotDateEnd) {
                    if (rangBase > lastNotDateEnd &&
                            rangBase <= endAvailable) {

                        /*** color cell*/
                        textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
                        textViewCell.setTextColor(Color.BLACK);

//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);

                        //change color start && end range
//                        if (rangBase == lastNotDateEnd+1)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                        if (rangBase == endAvailable)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);
                    }
                    /* bein 2ta NotAvailable */
                } else if (notDateEndPrevious != 0 &&
//                        rang1.getTimeInMillis() > notDateEndPrevious.getTimeInMillis() &&
                        rang1 < notDateStartInner &&
                        rangBase > notDateEndPrevious &&
                        rangBase < notDateStartInner) {
                    /*** color cell*/
                    textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
                    textViewCell.setTextColor(Color.BLACK);

//                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);
//
//                    //change color start && end range
//                    if (rangBase == notDateEndPrevious+1)
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                    if (rangBase == notDateStartInner-1)
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);
                }
            }
            // دستور شکستن حلقه زمانی که محدوده click1 شده در اولین NotAvaliable یافت می شود ‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍
//            if (rang1 < notDateStartInner) {
//                break;
//            }
        }
    }

//    public void showAvaliable_AFter_Click(DayData dayData, TextView textViewCell, long rang1) {
//
////        if (CalendarActivity.sharedPreferences.getBoolean("boolClick1ForNotDate", false))
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {
//
//            long startAvailable = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.startAvaliable);
//            long endAvailable = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.endAvailable);
//            long notDateStartInner = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i));
//
//            long notDateEndPrevious = 0;
//            if (i != 0 && ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1) != null)
//                notDateEndPrevious = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1));
//
//            long rangBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());
//
//            textViewCell.setBackgroundColor(Color.WHITE);
////            textViewCell.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            textViewCell.setTextColor(Color.GRAY);
//
//            //نشان دادن روزهای در دسترس که بعد از آن notAvaliable  وجود ندارد
//            if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty()) {
//                long firstNotDateStart = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(0));
//                long lastNotDateEnd = ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.size() - 1));
//
//                /* ghable avalin NotAvaliable */
//                if (rang1 < firstNotDateStart) {
//                    if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty() &&
//                            rangBase >= startAvailable &&
//                            firstNotDateStart != 0 &&
//                            rangBase < firstNotDateStart) {
//                        /*** color cell*/
//                        textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
//                        textViewCell.setTextColor(Color.BLACK);
//
//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);
//
//                        //change color start && end range
//                        if (rangBase == startAvailable)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                        if (rangBase == firstNotDateStart - 1)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);
//
//                    }
//                    /* bad az akharin NotAvaliable */
//                } else if (rang1 > lastNotDateEnd) {
//                    if (rangBase > lastNotDateEnd &&
//                            rangBase <= endAvailable) {
//
//                        /*** color cell*/
//                        textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
//                        textViewCell.setTextColor(Color.BLACK);
//
//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);
//
//                        //change color start && end range
//                        if (rangBase == lastNotDateEnd+1)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                        if (rangBase == endAvailable)
//                            textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);
//                    }
//                    /* bein 2ta NotAvailable */
//                } else if (notDateEndPrevious != 0 &&
////                        rang1.getTimeInMillis() > notDateEndPrevious.getTimeInMillis() &&
//                        rang1 < notDateStartInner &&
//                        rangBase > notDateEndPrevious &&
//                        rangBase < notDateStartInner) {
//                    /*** color cell*/
//                    textViewCell.setBackgroundColor(Color.parseColor("#E6E5E5"));
//                    textViewCell.setTextColor(Color.BLACK);
//
//                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase);
//
//                    //change color start && end range
//                    if (rangBase == notDateEndPrevious+1)
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_start_gray);
//                    if (rangBase == notDateStartInner-1)
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_end_gray);
//                }
//            }
//            // دستور شکستن حلقه زمانی که محدوده click1 شده در اولین NotAvaliable یافت می شود ‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍
//            if (rang1 < notDateStartInner) {
//                break;
//            }
//        }
//    }


}
