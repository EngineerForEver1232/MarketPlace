package com.pedpo.pedporent.widget.calendar.adapter.jalali;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.listener.ReturnContent;
import com.pedpo.pedporent.utills.ContextApp;
import com.pedpo.pedporent.utills.MyMutable;
import com.pedpo.pedporent.utills.language.PrefManagerLanguage;
import com.pedpo.pedporent.widget.calendar.interfaceCalendar.OnItemCalendarClickLister;
import com.pedpo.pedporent.widget.calendar.utils.ArrayListCalendar;
import com.pedpo.pedporent.widget.calendar.utils.ConvertTimeTo;
import com.pedpo.pedporent.widget.calendar.utils.GridUtil;
import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian;
import com.pedpo.pedporent.widget.calendar.utils.PrefManager;
import com.pedpo.pedporent.widget.calendar.vo.DayData;
import com.pedpo.pedporent.widget.calendar.vo.MarkedDates_In_Frag_And_Grid;

import java.util.ArrayList;


//public class AdapterGrid extends ArrayAdapter implements Observer {
public class AdapterGrid_Jalali extends ArrayAdapter implements ReturnContent {

    private ArrayList arrayListDate;
    private LayoutInflater layoutInflater;
    private long jalali_EndRange;
    private long jalali_StartRange;
    private boolean rangeBoolean;
    private OnItemCalendarClickLister onItemCalendarClickLister;
    private PrefManager prefManager;
    private View line1, line2;
    private PrefManagerLanguage prefManagerLanguage;


    public AdapterGrid_Jalali(Context context, int resource, ArrayList arrayListDate) {
        super(context, resource);

        this.arrayListDate = arrayListDate;
        this.layoutInflater = LayoutInflater.from(context);
        prefManager = new PrefManager(context);
        prefManagerLanguage = new PrefManagerLanguage(context);
        Log.i("checkObserver", "AdapterGrid_Jalali: 2 ");

        MyMutable.Companion.getMutable().observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("checkObserver", "onChanged observer: ");
                notifyDataSetChanged();
            }
        });
//        MarkedDates_In_Frag_And_Grid.getInstance().addObserver(this);

    }


    @Override
    public View getView(final int position, View covertView, ViewGroup parent) {

        final DayData dayData = (DayData) arrayListDate.get(position);
        covertView = this.layoutInflater.inflate(R.layout.item_cell_day_calendar, parent, false);
        final TextView textViewCell = covertView.findViewById(R.id.textview_item);
        line1 = covertView.findViewById(R.id.line1);
        line2 = covertView.findViewById(R.id.lineHouse);

        //        Log.e("showDateCalendar", "getView: "+dayData.getJalaliCalendar().getYear()+"/"+ dayData.getJalaliCalendar().getMonth()+"/"+dayData.getJalaliCalendar().getDay() );

        if (prefManagerLanguage.getLanguage().equals(ContextApp.EN))
            textViewCell.setText(dayData.getJalaliCalendar().getDay()+"");
        else
            textViewCell.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(dayData.getJalaliCalendar().getDay() + ""));

        GridUtil.newInstance().sunDay(dayData, textViewCell);
        GridUtil.newInstance().rangAvaliableJalali(dayData, textViewCell
                , ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.startAvaliable),
                ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.endAvailable));

        //vase rozhayi k nmishavad rent ya hire kard ... chon bayad rang ghermz bashad pas bad az avaliable biyad
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {
//            GridUtil.newInstance().rangNoAvaliableJalali(dayData, covertView,textViewCell, line1,line2,linearCell,
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i)),
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i)));
//
//            if (dayData.getDate().getDay() == ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i).getMonth()) {
//            }
//        }

//        Log.i("jalaliCalendar", "onCreate: " + ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size());


        jalali_StartRange = prefManager.getRange1();
        jalali_EndRange = prefManager.getRange2();

//        GridUtil.newInstance().rangNoAvaliableJalali(dayData, covertView, textViewCell, line1, line2, linearCell,
//                jalali_StartRange,
//                jalali_EndRange
//        );


        rangeBoolean = prefManager.getRangeBoolean();

        covertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCalendarClickLister.onItemCalendarClickListenerRange(
                        view,
                        dayData,
                        rangeBoolean,
                        prefManager.getRange1(),
                        prefManager.getFormatStart_Jalali()
                );
            }
        });

//        if (prefManager.getBoolClick1ForNotDate())
//            GridUtil.newInstance().showAvaliable_AFter_Click(dayData, textViewCell, jalali_StartRange);

//        GridUtil.newInstance().rangCalendar_Jalali(dayData, textViewCell, jalali_StartRange, jalali_EndRange);
        GridUtil.newInstance().toDay(dayData, textViewCell);
        GridUtil.newInstance().rangCalendar(
                dayData, covertView, textViewCell,
                prefManager.getRange1(),
                prefManager.getRange2(), line1, line2);
        GridUtil.newInstance().kmmToday(dayData,covertView, textViewCell,line1, line2);
        GridUtil.newInstance().dayIsZero(dayData, textViewCell, line1, line2);

        return covertView;
    }

    @Override
    public int getCount() {
        return arrayListDate.size();
    }




//    @Override
//    public void update(Observable o, Object arg) {
//        Log.i("checkObserver", "update: ");
//        if (prefManager.isRefresh())
//        AdapterGrid.this.notifyDataSetChanged();
//
//    }

    public void setOnItemCalendarClickLister(OnItemCalendarClickLister
                                                     onItemCalendarClickLister) {
        this.onItemCalendarClickLister = onItemCalendarClickLister;
    }

    @Override
    public void returnContent(@Nullable String content) {
        AdapterGrid_Jalali.this.notifyDataSetChanged();

    }
}
