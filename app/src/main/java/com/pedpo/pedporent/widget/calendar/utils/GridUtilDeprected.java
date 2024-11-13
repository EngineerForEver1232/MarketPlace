package com.pedpo.pedporent.widget.calendar.utils;//package com.pedpo.pedpoPersian.calendar.utils;
//
//import android.graphics.Color;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.pedpo.pedpoPersian.R;
//import com.pedpo.pedpoPersian.calendar.vo.DayData;
//
//import java.util.Calendar;
//import java.util.Date;
//
//public class GridUtilDeprected {
//
//
//    private static GridUtilDeprected gridUtil = new GridUtilDeprected();
//
//    public static GridUtilDeprected newInstance() {
//        return gridUtil;
//    }
//
//    public void toDay(DayData dayData, TextView textView) {
//        if (dayData.getJalaliCalendar() != null)
//            if (dayData.getJalaliCalendar().equals(CurrentCalendar.getCurrentJalali())) {
//                textView.setTextColor(Color.WHITE);
//                textView.setBackgroundResource(R.drawable.border_item_calendar_primary);
//            }
//    }
//
//    public boolean clickOff(DayData dayData) {
//        if (dayData.getJalaliCalendar() != null) {
//            Date date1 = new Date(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//            Date date2 = new Date(CurrentCalendar.getCurrentJalali().getYear(), CurrentCalendar.getCurrentJalali().getMonth(), CurrentCalendar.getCurrentJalali().getDay());
//
//            if (date1.getTime() < date2.getTime()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean kmmToday(DayData dayData, TextView textView) {
//        if (dayData.getJalaliCalendar() != null) {
//            Calendar calendarKmm = Calendar.getInstance();
//            calendarKmm.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//            ConvertTimeTo.getInstance().CovertTimeToZero(calendarKmm);
//            Calendar calendarNow = Calendar.getInstance();
//            ConvertTimeTo.getInstance().CovertTimeToZero(calendarNow);
//            calendarNow.set(CurrentCalendar.getCurrentJalali().getYear(), CurrentCalendar.getCurrentJalali().getMonth(), CurrentCalendar.getCurrentJalali().getDay());
//
//            if (calendarKmm.getTimeInMillis() < calendarNow.getTimeInMillis()) {
//                textView.setTextColor(Color.GRAY);
//                textView.setBackgroundColor(Color.WHITE);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void sunDay(DayData dayData, TextView textViewCell) {
//        if (dayData.getJalaliCalendar() != null) {
//            if (dayData.getJalaliCalendar().getDayOfWeekString().equals(AppContents.FRIDAY)) {
//                //color = RED
//                textViewCell.setTextColor(Color.RED);
//                GridUtilDeprected.newInstance().kmmToday(dayData, textViewCell);
//            }
//        }
//    }
//
//    public void dayIsZero(DayData dayData, TextView textViewCell) {
//
//
//        if (dayData.getJalaliCalendar().getDay() == 0) {
//            textViewCell.setText("");
//            textViewCell.setBackgroundColor(Color.WHITE);
//        }
//    }
//
//
//    public void rangCalendar_Jalali(DayData dayData, TextView textView, Calendar rang1, Calendar rang2) {
//        Log.e("showDateCalendar", "getView: " + dayData.getJalaliCalendar().getYear() + "/" + dayData.getJalaliCalendar().getMonth() + "/" + dayData.getJalaliCalendar().getDay());
//
//        Calendar rangBase = Calendar.getInstance();
//        rangBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//
//        ConvertTimeTo.getInstance().CovertTimeToZero(rangBase);
//        ConvertTimeTo.getInstance().CovertTimeToZero(rang1);
//        ConvertTimeTo.getInstance().CovertTimeToZero(rang2);
//
//        if (rangBase.getTimeInMillis() == rang1.getTimeInMillis()) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_green);
//            Log.e("rangeCalendarTest", "rangCalendar_Jalali1111: " + rangBase.get(Calendar.YEAR) + "/" + rangBase.get(Calendar.MONTH) + "/" + rangBase.get(Calendar.DAY_OF_MONTH));
//        }
////        }
//        if (rang2.getTimeInMillis() != (AppContents.ZERO_CALENDAR) && rangBase.getTimeInMillis() >= rang1.getTimeInMillis() &&
//                rangBase.getTimeInMillis() <= rang2.getTimeInMillis()) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_green);
//            Log.e("rangeCalendarTest", "rangCalendar_Jalali2222: " + rangBase.get(Calendar.YEAR) + "/" + rangBase.get(Calendar.MONTH) + "/" + rangBase.get(Calendar.DAY_OF_MONTH));
//
//        }
//    }
//
//    public void rangCalendar_longDateJalali
//            (DayData dayData, TextView textView, long rang1, long rang2) {
//        Log.e("showDateCalendar", "getView: " + dayData.getJalaliCalendar().getYear() + "/" + dayData.getJalaliCalendar().getMonth() + "/" + dayData.getJalaliCalendar().getDay());
//
//        long initDate = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());
//
//        if (initDate == rang1) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_green);
//            Log.e("rangeCalendarTestClick", "rangCalendar_Jalali1111: " +
//                    dayData.getJalaliCalendar().toString()
//                    + " = " +
//                    rang1 + " rang2 " +
//                    rang2 + " initDate " +
//                    initDate);
//        }
////        }
//        if (initDate >= rang1 &&
//                rang2!=0 &&
//                initDate <= rang2) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_green);
//        }
//    }
//
//
//    public void rangAvaliableJalali(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
//
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendar1);
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendar2);
//
//        Calendar calendarBase = Calendar.getInstance();
//        calendarBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendarBase);
//
//        if (calendarBase.getTimeInMillis() == calendar1.getTimeInMillis()) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
//            if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays1.contains(calendarBase.getTimeInMillis()))
//                ArrayListCalendar.arrayListCalendar.saveAvailableDays1.add(calendarBase.getTimeInMillis());
//        }
//        if (calendarBase.getTimeInMillis() >= calendar1.getTimeInMillis() &&
//                calendarBase.getTimeInMillis() <= calendar2.getTimeInMillis()) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
//            if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays1.contains(calendarBase.getTimeInMillis()))
//                ArrayListCalendar.arrayListCalendar.saveAvailableDays1.add(calendarBase.getTimeInMillis());
//        }
//    }
//
//    public void rangNoAvaliableJalali(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
//
//        Calendar calendarBase = Calendar.getInstance();
//        calendarBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendarBase);
//
//        if (calendar2.getTimeInMillis() == 0 || calendar2.getTimeInMillis() < calendar1.getTimeInMillis()) {
//            if (calendarBase.getTimeInMillis() >= calendar1.getTimeInMillis() && calendarBase.getTimeInMillis() <= calendar1.getTimeInMillis()) {
//                textView.setBackgroundResource(R.drawable.border_item_calendar_red);
//                textView.setTextColor(Color.WHITE);
//            }
//        }
//        if (calendarBase.getTimeInMillis() >= calendar1.getTimeInMillis() && calendarBase.getTimeInMillis() <= calendar2.getTimeInMillis()) {
//            textView.setBackgroundResource(R.drawable.border_item_calendar_red);
//            textView.setTextColor(Color.WHITE);
//        }
//    }
//
//
//    public void rangNoAvaliable(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
//        Date rang1 = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//
//        );
//        Date rang2 = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
//            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
//                //color = RED
////                textView.setBackgroundColor(Color.RED);
//                textView.setBackgroundResource(R.drawable.border_item_calendar_red);
//                textView.setTextColor(Color.WHITE);
//                textView.setText(dayData.getText());
//
//            }
//        }
//        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
//            //color = RED
////            textView.setBackgroundColor(Color.RED);
//            textView.setBackgroundResource(R.drawable.border_item_calendar_red);
//            textView.setTextColor(Color.WHITE);
//            textView.setText(dayData.getText());
//
////            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public boolean rangAvaliableForClick(DayData dayData, Calendar calendar1, Calendar calendar2) {
//        Date startEvaliable = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//
//        );
//        Date endAvaliable = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rangBase.getTime() >= startEvaliable.getTime() && rangBase.getTime() <= endAvaliable.getTime()) {
//
//            return false;
//        }
//        return true;
//    }
//
//
//    public boolean rangAvaliableForClickJalali(DayData dayData, Calendar calendar1, Calendar calendar2) {
//
//        Calendar calendarBase = Calendar.getInstance();
//        calendarBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendarBase);
//
//        if (calendarBase.getTimeInMillis() >= calendar1.getTimeInMillis() && calendarBase.getTimeInMillis() <= calendar2.getTimeInMillis()) {
//            return false;
//        }
//        return true;
//    }
//
//    public boolean rangNotAvaliable_For_Click(DayData dayData, Calendar calendar1, Calendar calendar2) {
//        Date rang1 = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rang2 = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean rangNotAvaliable_For_Click_Jalali(DayData dayData, Calendar calendar1, Calendar calendar2) {
//
//        Calendar calendarBase = Calendar.getInstance();
//        calendarBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(calendarBase);
//
//        if (calendarBase.getTimeInMillis() >= calendar1.getTimeInMillis() && calendarBase.getTimeInMillis() <= calendar2.getTimeInMillis()) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean notAvaliable_For_Click2(Calendar click1, Calendar click2, Calendar calendarNotAvailableStart, Calendar calendarNotAvailableEnd) {
//
//        if (click1.getTimeInMillis() < calendarNotAvailableStart.getTimeInMillis() && click2.getTimeInMillis() > calendarNotAvailableEnd.getTimeInMillis()) {
//            return true;
//        }
//        return false;
//    }
//
//
//    public boolean notAvaliable_For_Click2_Previous(Date click1, Date click2, Calendar calendar1, Calendar calendar2) {
//        Date notDateStart = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//        );
//        Date notDateEnd = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//
//        if (click1.getTime() > notDateEnd.getTime() && click2.getTime() < notDateStart.getTime()) {
//            return true;
//        }
//
//        return false;
//    }
//
//
//    public void rangAvaliable(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
//        Date rang1 = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//
//        );
//        Date rang2 = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
//            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
//                //color = Gray
////                textView.setBackgroundColor(Color.parseColor("#bfbfbf"));
//                textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
////                textView.setTextColor(Color.WHITE);
//                textView.setText(dayData.getText());
//
//            }
//        }
//        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
//
//            //color = Gray
////            textView.setBackgroundColor(Color.parseColor("#bfbfbf"));
//            textView.setBackgroundResource(R.drawable.border_item_calendar_gray);
//
////            textView.setTextColor(Color.WHITE);
//            textView.setText(dayData.getText());
//
////            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//    public boolean rangAvaliableForEditText(DayData dayData, Calendar calendar1, Calendar calendar2) {
//        Date startEvaliable = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//
//        );
//        Date endAvaliable = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rangBase.getTime() >= startEvaliable.getTime() && rangBase.getTime() <= endAvaliable.getTime()) {
//
//            return false;
//        }
//        return true;
//    }
//
//
//    public void rangHired(DayData dayData, TextView textView, Calendar calendar1, Calendar calendar2) {
//        Date rang1 = new Date(
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH)
//
//        );
//        Date rang2 = new Date(
//                calendar2.get(Calendar.YEAR),
//                calendar2.get(Calendar.MONTH),
//                calendar2.get(Calendar.DAY_OF_MONTH)
//        );
//        Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());
//
//        if (rang2.getTime() == 0 || rang2.getTime() < rang1.getTime()) {
//            if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang1.getTime()) {
//                textView.setBackgroundColor(Color.BLUE);
//                textView.setTextColor(Color.WHITE);
//                textView.setText(dayData.getText());
//
//            }
//        }
//        if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() <= rang2.getTime()) {
//
//            textView.setBackgroundColor(Color.BLUE);
//            textView.setTextColor(Color.WHITE);
//            textView.setText(dayData.getText());
//
////            Toast.makeText(getContext(), dayData.stringDate().getYear() + "/" + dayData.stringDate().getMonth() + "/" + dayData.stringDate().getDay(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void showAvaliable_AFter_Click(DayData dayData, TextView textViewCell, Calendar rang1) {
//
//
//        ConvertTimeTo.getInstance().CovertTimeToZero(rang1);
////        if (CalendarActivity.sharedPreferences.getBoolean("boolClick1ForNotDate", false))
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {
//
//            Calendar startAvailable = ArrayListCalendar.arrayListCalendar.startAvaliable;
//            ConvertTimeTo.getInstance().CovertTimeToZero(startAvailable);
//
//            Calendar endAvailable = ArrayListCalendar.arrayListCalendar.endAvailable;
//            ConvertTimeTo.getInstance().CovertTimeToZero(endAvailable);
//
//            Calendar notDateStartInner = ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i);
//            ConvertTimeTo.getInstance().CovertTimeToZero(notDateStartInner);
//
//            Calendar notDateEndPrevious = null;
//            if (i != 0 && ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1) != null) {
//                notDateEndPrevious = ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i - 1);
//                ConvertTimeTo.getInstance().CovertTimeToZero(notDateEndPrevious);
//            }
//            Calendar notDateEndInner = ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i);
//            ConvertTimeTo.getInstance().CovertTimeToZero(notDateEndInner);
//
//            Calendar avaliableDateEnd = ArrayListCalendar.arrayListCalendar.endAvailable;
//            ConvertTimeTo.getInstance().CovertTimeToZero(avaliableDateEnd);
//
//
//            Log.e("testClickAvailable", "showAvaliable_AFter_Click: Notstart" +
////                    notDateStart.get(Calendar.YEAR) + "," + notDateStart.get(Calendar.MONTH) + " Notends "
////                    + notDateEnd.get(Calendar.YEAR) + "," + notDateEnd.get(Calendar.MONTH) + " avaliableDateEnd " +
//                            avaliableDateEnd.get(Calendar.YEAR) + "-" + avaliableDateEnd.get(Calendar.MONTH) + "-" + avaliableDateEnd.get(Calendar.DATE)
//            );
//
//            Calendar rangBase = Calendar.getInstance();
//            rangBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//            ConvertTimeTo.getInstance().CovertTimeToZero(rangBase);
//
////                textViewCell.setBackgroundColor(Color.WHITE);
//            textViewCell.setBackgroundResource(R.drawable.border_cardview);
//            textViewCell.setTextColor(Color.GRAY);
//            textViewCell.setTag("available");
//
//            //نشان دادن روزهای در دسترس که بعد از آن notAvaliable  وجود ندارد
////                if (rangBase.getTimeInMillis() > notDateEnd.getTimeInMillis() &&
////                        rang1.getTimeInMillis() > notDateEnd.getTimeInMillis() &&
////                        rangBase.getTimeInMillis() <=avaliableDateEnd.getTimeInMillis() ) {
////
////                    textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
////                    textViewCell.setTextColor(Color.WHITE);
////                }
//
////                if (rang1.getTime() < avaliableDateEnd.getTime())
////                {
////                    textViewCell.setBackgroundColor(Color.RED);
////                    textViewCell.setTextColor(Color.WHITE);
////                }
//
//            //نشان دادن روزهای در دسترس از جایی که click1 شده
////                if (rangBase.getTimeInMillis() >= rang1.getTimeInMillis() && rangBase.getTimeInMillis() < notDateStart.getTimeInMillis()   ) {
////                    textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
////                    textViewCell.setTextColor(Color.WHITE);
////                }
//
//            if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty()) {
//
//                Calendar firstNotDateStart = ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(0);
//                ConvertTimeTo.getInstance().CovertTimeToZero(firstNotDateStart);
//
//                Calendar lastNotDateEnd = ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.size() - 1);
//                ConvertTimeTo.getInstance().CovertTimeToZero(lastNotDateEnd);
//
//
//                if (rang1.getTimeInMillis() < firstNotDateStart.getTimeInMillis()) {
//                    if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty() &&
//                            rangBase.getTimeInMillis() >= startAvailable.getTimeInMillis() &&
//                            firstNotDateStart != null &&
//                            rangBase.getTimeInMillis() < firstNotDateStart.getTimeInMillis()) {
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
//                        textViewCell.setTextColor(Color.WHITE);
//                        Log.e("sizeArray", rangBase.get(Calendar.YEAR) + " / " + rangBase.get(Calendar.MONTH) + " / " + rangBase.get(Calendar.DAY_OF_MONTH));
//
//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase.getTimeInMillis());
//                        Log.e("sizeArray", "pekhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh 1111111111");
//
//                    }
//                } else if (rang1.getTimeInMillis() > lastNotDateEnd.getTimeInMillis()) {
//                    if (rangBase.getTimeInMillis() > lastNotDateEnd.getTimeInMillis() &&
//                            rangBase.getTimeInMillis() <= endAvailable.getTimeInMillis()) {
//
//                        textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
//                        textViewCell.setTextColor(Color.WHITE);
//
//                        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase.getTimeInMillis());
//                        Log.e("sizeArray", "pekhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh 222222222222222");
//
//                    }
//                } else if (notDateEndPrevious != null &&
////                        rang1.getTimeInMillis() > notDateEndPrevious.getTimeInMillis() &&
//                        rang1.getTimeInMillis() < notDateStartInner.getTimeInMillis() &&
//                        rangBase.getTimeInMillis() > notDateEndPrevious.getTimeInMillis() &&
//                        rangBase.getTimeInMillis() < notDateStartInner.getTimeInMillis()) {
//                    textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
//                    textViewCell.setTextColor(Color.WHITE);
//                    Log.e("sizeArray", "pekhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh 33333");
//
//                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.add(rangBase.getTimeInMillis());
//                    Log.e("sizeArray", rangBase.get(Calendar.YEAR) + " / " + rangBase.get(Calendar.MONTH) + " / " + rangBase.get(Calendar.DAY_OF_MONTH));
//
//                }
//            }
//
//            // دستور شکستن حلقه زمانی که محدوده click1 شده در اولین notavaliable یافت می شود ‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍
//            if (rang1.getTimeInMillis() < notDateStartInner.getTimeInMillis()) {
//                break;
//            }
//        }
//    }
//
//    public boolean visibleClickAvailable(DayData dayData, Calendar rang1) {
//        ConvertTimeTo.getInstance().CovertTimeToZero(rang1);
//        Calendar endAvailable = ArrayListCalendar.arrayListCalendar.endAvailable;
//        ConvertTimeTo.getInstance().CovertTimeToZero(endAvailable);
//
//        Calendar rangBase = Calendar.getInstance();
//        rangBase.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(rangBase);
//
//
//        if (!ArrayListCalendar.arrayListCalendar.listNotAvailableEND.isEmpty()) {
//            Calendar firstNotDateStart = ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(0);
//            ConvertTimeTo.getInstance().CovertTimeToZero(firstNotDateStart);
//            Calendar lastNotDateEnd = ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.size() - 1);
//            ConvertTimeTo.getInstance().CovertTimeToZero(lastNotDateEnd);
//
//            if (rang1.getTimeInMillis() > lastNotDateEnd.getTimeInMillis()) {
//                if (rangBase.getTimeInMillis() > lastNotDateEnd.getTimeInMillis() &&
//                        rangBase.getTimeInMillis() < endAvailable.getTimeInMillis()) {
//
//                    return true;
//                }
//            }
//        } else
//            return false;
//
//        return false;
//    }
//
//
//}
