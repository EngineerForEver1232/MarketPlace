package com.pedpo.pedporent.widget.calendar.adapter.miladi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.utills.ContextApp;
import com.pedpo.pedporent.utills.MyMutable;
import com.pedpo.pedporent.utills.NumberFormatDigits;
import com.pedpo.pedporent.utills.NumberFormatPersian;
import com.pedpo.pedporent.utills.language.PrefManagerLanguage;
import com.pedpo.pedporent.widget.calendar.utils.ConvertDate;
import com.pedpo.pedporent.widget.calendar.utils.PrefManager;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.fragments.FragmentMonth;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.interfaceCalendar.OnItemCalendarClickLister;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to.MarkStyle;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.ArrayListCalendar;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.CalendarUtil;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.GridUtil;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DayData;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.MarkedDates;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AdapterGrid_Miladi extends ArrayAdapter {

    private ArrayList arrayListDate;
    private LayoutInflater layoutInflater;
    private Date rang1, rang2;
    private PrefManager prefManager;
    private OnItemCalendarClickLister onItemCalendarClickLister;
    private View line1,line2;
    private NumberFormatPersian numberFormatPersian;
    private PrefManagerLanguage prefManagerLanguage;


    public void setOnItemCalendarClickLister(OnItemCalendarClickLister onItemCalendarClickLister){
        this.onItemCalendarClickLister = onItemCalendarClickLister;
    }

    public AdapterGrid_Miladi(Context context, int resource, ArrayList arrayListDate) {
        super(context, resource);
        prefManager = new PrefManager(context);

        this.arrayListDate = arrayListDate;
        this.layoutInflater = LayoutInflater.from(context);
        numberFormatPersian = new NumberFormatPersian();
        prefManagerLanguage = new PrefManagerLanguage(context);
        Log.i("checkObserver", "AdapterGrid_Miladi: 2 ");

        MyMutable.Companion.getMutable().observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("checkObserver", "onChanged observer: ");
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public View getView(final int position, View covertView, ViewGroup parent) {

        final DayData dayData = (DayData) arrayListDate.get(position);

        covertView = this.layoutInflater.inflate(R.layout.item_cell_day_calendar, parent, false);
        final TextView textViewCell = covertView.findViewById(R.id.textview_item);
        line1 = covertView.findViewById(R.id.line1);
        line2 = covertView.findViewById(R.id.lineHouse);

        if (prefManagerLanguage.getLanguage().equals(ContextApp.EN))
            textViewCell.setText(dayData.getText());
        else
            textViewCell.setText(numberFormatPersian.toPersianNumbersSample(dayData.getText()));


//        textViewCell.setText(dayData.getText());


        //vase rozhayi k mishavad rent ya hire kard
//        GridUtil.newInstance().rangAvaliable(dayData, textViewCell,
//                ArrayListCalendar.arrayListCalendar.startAvaliable,
//                ArrayListCalendar.arrayListCalendar.endAvaliable);


        //vase rozhayi k nmishavad rent ya hire kard ... chon bayad rang ghermz bashad pas bad az avaliable biyad
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.arrayListStart.size(); i++) {
//            GridUtil.newInstance().rangNoAvaliable(dayData, textViewCell,
//                    ArrayListCalendar.arrayListCalendar.arrayListStart.get(i),
//                    ArrayListCalendar.arrayListCalendar.arrayListEND.get(i));
//
//            if (dayData.getDate().getDay() == ArrayListCalendar.arrayListCalendar.arrayListEND.get(i).get(Calendar.DAY_OF_MONTH)) {
////                Toast.makeText(appCompatActivity, dayData.stringDate().getDayString(), Toast.LENGTH_SHORT).show();
//            }
//        }

//        //for  hired
//        for (int i = 0; i< ArrayListCalendar.arrayListCalendar.hiredStart.size() ; i++)
//        {
//            GridUtil.newInstance().rangHired(dayData,textViewCell,ArrayListCalendar.arrayListCalendar.hiredStart.get(i),ArrayListCalendar.arrayListCalendar.hiredEND.get(i));
//        }


//      Set Range
        rang1 = new Date(prefManager.getRange1_Miladi());
        rang2 = new Date(prefManager.getRange2_Miladi());


//        if (rang1 != null && rang2 != null) {
//            GridUtil.newInstance().rangCalendar(dayData, textViewCell, rang1, rang2);
//        }
        chengeCalendarDay(dayData, textViewCell);

//        showAvaliable_AFter_Click(dayData,textViewCell);

        if (rang1 != null && rang2 != null) {
            if (prefManager.isSingleClick()){
                GridUtil.newInstance().singleClick(dayData, covertView,
                        textViewCell,
                        rang1, rang2, line1, line2);
            }else {
                GridUtil.newInstance().rangCalendar(dayData, covertView,
                        textViewCell,
                        rang1, rang2, line1, line2);
            }
        }

//        GridUtil.newInstance().dayIsZero(dayData, textViewCell);


//        MarkStyle style = MarkedDates.getInstance().check(dayData.getDate());
//        boolean marked = style != null;
//        if (marked) {
//            dayData.getDate().getMarkStyle().getStyle();
//
////            ShapeDrawable circleDrawable = new ShapeDrawable(new OvalShape());
////            circleDrawable.getPaint().setColor(style.getColor());
//
//            textViewCell.setText(dayData.getText());
//            textViewCell.setTextColor(Color.GREEN);
//            textViewCell.setBackgroundColor(style.getColor());
//
//        }

        covertView.setOnClickListener((v)->{
            if (onItemCalendarClickLister!=null)
                onItemCalendarClickLister.onItemCalendarClickListenerRange(
                        v,dayData,
                        new Date(prefManager.getRange1_Miladi())
                        ,prefManager.getFormatStart_Miladi());
        });


        GridUtil.newInstance().sunDay(dayData, textViewCell);
        GridUtil.newInstance().dayIsZero(dayData, textViewCell,line1,line2);

        GridUtil.newInstance().kmmToday(dayData,covertView, textViewCell,line1, line2);


        return covertView;
    }


    public void showAvaliable_AFter_Click(DayData dayData,TextView textViewCell)
    {
//        if (CalendarActivity.sharedPreferences.getBoolean("boolClick1ForNotDate", false))
        if (prefManager.getBoolClick1ForNotDate())
            for (int i = 0; i < ArrayListCalendar.arrayListCalendar.arrayListStart.size(); i++) {
                Date notDateStart = new Date(
                        ArrayListCalendar.arrayListCalendar.arrayListStart.get(i).get(Calendar.YEAR),
                        ArrayListCalendar.arrayListCalendar.arrayListStart.get(i).get(Calendar.MONTH),
                        ArrayListCalendar.arrayListCalendar.arrayListStart.get(i).get(Calendar.DAY_OF_MONTH)
                );
                Date notDateEnd = new Date(
                        ArrayListCalendar.arrayListCalendar.arrayListEND.get(i).get(Calendar.YEAR),
                        ArrayListCalendar.arrayListCalendar.arrayListEND.get(i).get(Calendar.MONTH),
                        ArrayListCalendar.arrayListCalendar.arrayListEND.get(i).get(Calendar.DAY_OF_MONTH)
                );

                Date avaliableDateEnd = new Date(
                        ArrayListCalendar.arrayListCalendar.endAvaliable.get(Calendar.YEAR),
                        ArrayListCalendar.arrayListCalendar.endAvaliable.get(Calendar.MONTH),
                        ArrayListCalendar.arrayListCalendar.endAvaliable.get(Calendar.DAY_OF_MONTH)
                );


                Date rangBase = new Date(dayData.getDate().getYear(), (dayData.getDate().getMonth()) - 1, dayData.getDate().getDay());

//                textViewCell.setBackgroundColor(Color.WHITE);
                textViewCell.setBackgroundResource(R.drawable.border_cardview);
                textViewCell.setTextColor(Color.GRAY);

                //نشان دادن روزهای در دسترس که بعد از آن notAvaliable  وجود ندارد
                if (rangBase.getTime() > notDateEnd.getTime() && rang1.getTime() > notDateEnd.getTime() && rangBase.getTime() <=avaliableDateEnd.getTime() ) {

//                    textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
                    textViewCell.setTextColor(Color.WHITE);
                }

//                if (rang1.getTime() < avaliableDateEnd.getTime())
//                {
//                    textViewCell.setBackgroundColor(Color.RED);
//                    textViewCell.setTextColor(Color.WHITE);
//                }

                //نشان دادن روزهای در دسترس از جایی که click1 شده
                if (rangBase.getTime() >= rang1.getTime() && rangBase.getTime() < notDateStart.getTime()   ) {
//                    textViewCell.setBackgroundResource(R.drawable.border_item_calendar_gray);
                    textViewCell.setTextColor(Color.WHITE);
                }

                // دستور شکستن حلقه زمانی که محدوده click1 شده در اولین notavaliable یافت می شود ‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍‍
                if (rang1.getTime() < notDateStart.getTime()) {
                    break;
                }
            }
    }

    public void chengeCalendarDay(DayData dayData, TextView textView) {
        GridUtil.newInstance().toDay(dayData, textView);
    }



    @Override
    public int getCount() {
        return arrayListDate.size();
    }


//    @Override
//    public void update(Observable o, Object arg) {
//        notifyDataSetChanged();
//    }
}
