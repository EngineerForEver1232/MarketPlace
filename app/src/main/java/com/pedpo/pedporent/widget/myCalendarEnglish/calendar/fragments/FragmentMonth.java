package com.pedpo.pedporent.widget.myCalendarEnglish.calendar.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.utills.MyMutable;
import com.pedpo.pedporent.widget.calendar.CalendarActivity;
import com.pedpo.pedporent.widget.calendar.utils.ConvertTimeTo;
import com.pedpo.pedporent.widget.calendar.utils.FormatCalendar;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.utils.PrefManager;
import com.pedpo.pedporent.widget.calendar.adapter.miladi.AdapterGrid_Miladi;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.interfaceCalendar.OnItemCalendarClickLister;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to.RangeTO;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.GridUtil;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.DayData;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.vo.MonthData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentMonth extends Fragment implements  OnItemCalendarClickLister {

    private MonthData monthData;
    private GridView gridView;
    private AdapterGrid_Miladi adapterGridMiladi;

    public void setData(MonthData monthData) {
        this.monthData = monthData;
    }


    private PrefManager prefManager;
//    public static MutableLiveData mutable = new MutableLiveData<Boolean>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_month_en, container, false);

        gridView = view.findViewById(R.id.gridview);

        adapterGridMiladi = new AdapterGrid_Miladi(getContext(), 1, monthData.getData());
        gridView.setAdapter(adapterGridMiladi);
        adapterGridMiladi.setOnItemCalendarClickLister(this);

        prefManager = new PrefManager(getContext());


        return view;
    }




    @Override
    public void onItemCalendarClickListenerRange(View view, DayData dayData, Date range1, String StringDateJalali) {

        if (GridUtil.newInstance().clickOff(dayData))
            return;

        if (prefManager.isSingleClick()) {
            oneClickCalendar(view, dayData);
        } else {

            if (prefManager.getRangeBoolean()) {
                oneClickCalendar(view, dayData);
            } else {
                towClickCalendar(view, dayData, range1);
            }
        }
    }

    public void oneClickCalendar(View view, DayData dayData) {
//        MarkedDates.getInstance().data.add(new DateData(dayData.getDate().getYear(),dayData.getDate().getMonth(),dayData.getDate().getDay()));
        //TODO calendarStartForReveres bayad dar sate class bashad , chon vase barAx kar kardane calendar lazeme . chon dar if az in vase namayesh dar edittext bahre mibarim .
        Calendar calendarStartForReveres = Calendar.getInstance();
        calendarStartForReveres.set(dayData.getDate().getYear(), dayData.getDate().getMonth() - 1, dayData.getDate().getDay());
        Date rang1 = new Date(
                calendarStartForReveres.get(Calendar.YEAR),
                calendarStartForReveres.get(Calendar.MONTH),
                calendarStartForReveres.get(Calendar.DAY_OF_MONTH)
        );

        prefManager.setBoolClick1ForNotDate(true);
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.arrayListStart.size(); i++) {
//            long a =  GridUtil.newInstance().show_Avaliable_Click1(rang1, ArrayListCalendar.arrayListCalendar.arrayListStart.get(i), ArrayListCalendar.arrayListCalendar.arrayListEND.get(i));
//                Toast.makeText(appCompatActivity, a+"", Toast.LENGTH_SHORT).show();
//
//        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(calendarStartForReveres.getTime());
        prefManager.setFormatStart_Miladi(dateString);
        prefManager.setFormatEnd_Miladi("");
//        editTextStartDate.setText(dateString);

        prefManager.setRange1_Miladi(rang1.getTime());
        prefManager.setRange2_Miladi(0);

//                editTextStartDate.setText(dateString);
//                    rangeBoolean = false;
        prefManager.setRangeBoolean(false);
//        adapterGrid.notifyDataSetChanged();

        CalendarActivity.mutableLiveData.postValue(new RangeTO(dateString, ""));
        MyMutable.Companion.getMutable().postValue(true);

        setJalaliRange1();

    }

    public void towClickCalendar(View view, DayData dayData, Date rang1) {


        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(dayData.getDate().getYear(), dayData.getDate().getMonth() - 1, dayData.getDate().getDay());
        Date rang2 = new Date(
                calendarEnd.get(Calendar.YEAR),
                calendarEnd.get(Calendar.MONTH),
                calendarEnd.get(Calendar.DAY_OF_MONTH)
        );


//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.arrayListStart.size(); i++) {
//            if (GridUtil.newInstance().notAvaliable_For_Click2(rang1, rang2,
//                    ArrayListCalendar.arrayListCalendar.arrayListStart.get(i),
//                    ArrayListCalendar.arrayListCalendar.arrayListEND.get(i))) {
////                            Toast.makeText(appCompatActivity, "doing...", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }

        // if rang2 az rang1 kochiktar bashad , hamechi barAx mishavad , va tamame dadeha brax dakhel shared save mishavad
        if (rang1.getTime() > rang2.getTime()) {

//            for (int i = 0; i < ArrayListCalendar.arrayListCalendar.arrayListStart.size(); i++) {
//                if (GridUtil.newInstance().notAvaliable_For_Click2_Previous(rang1, rang2,
//                        ArrayListCalendar.arrayListCalendar.arrayListStart.get(i), ArrayListCalendar.arrayListCalendar.arrayListEND.get(i))) {
////                                Toast.makeText(appCompatActivity, "Previous...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }

//                        CalendarActivity.sharedPreferences.edit().putLong("rang2", rang1.getTime()).apply();
//                        CalendarActivity.sharedPreferences.edit().putLong("rang1", rang2.getTime()).apply();

            prefManager.setRange2_Miladi(rang1.getTime());
            prefManager.setRange1_Miladi(rang2.getTime());


            Calendar calendarStart = Calendar.getInstance();
            calendarStart.set(rang1.getYear(), rang1.getMonth(), rang1.getDate());

//                        calendarStart.setTime(rang1);
//                        Calendar calendarTest = Calendar.getInstance();
//                        calendarTest.set(
//                                calendarStart.get(Calendar.YEAR)-1900,
//                                calendarStart.get(Calendar.MONTH),
//                                calendarStart.get(Calendar.DAY_OF_MONTH));


            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //TODO calendarStartForReveres bayad dar sate class bashad , chon vase barAx kar kardane calendar lazeme . chon dar if az in vase namayesh dar edittext bahre mibarim .
            String dateStart = sdf.format(calendarStart.getTime());
            String dateEnd = sdf.format(calendarEnd.getTime());
            prefManager.setFormatStart_Miladi(dateEnd);
            prefManager.setFormatEnd_Miladi(dateStart);

            CalendarActivity.mutableLiveData.postValue(new RangeTO(dateEnd, dateStart));
            MyMutable.Companion.getMutable().postValue(true);


        } else {
            prefManager.setRange2_Miladi(rang2.getTime());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(calendarEnd.getTime());
            prefManager.setFormatEnd_Miladi(dateString);


            CalendarActivity.mutableLiveData.postValue(new RangeTO(prefManager.getFormatStart_Miladi(), dateString));
            MyMutable.Companion.getMutable().postValue(true);


        }

//                Toast.makeText(appCompatActivity, dayData.stringDate().getYear()+" : "+dayData.stringDate().getMonth()+"  : "+dayData.stringDate().getDay()+"", Toast.LENGTH_SHORT).show();
//                CalendarActivity.sharedPreferences.edit().putLong("rang1dialog", calendarRang1.getTimeInMillis()).apply();


//                    rangeBoolean = true;
        prefManager.setRangeBoolean(true);
//        adapterGrid.notifyDataSetChanged();

        setJalaliRange2();
    }


    public void setJalaliRange1(){
//        CalendarActivity.mutableLiveData.postValue(new RangeTO(dayData.getJalaliCalendar().toString(), ""));
        JalaliCalendar jalaliCalendar =FormatCalendar.getInstance().getFormatMiladiToJalali(prefManager.getFormatStart_Miladi());

        prefManager.setBoolClick1ForNotDate(true);

        prefManager.setDateFormatJalaliStart(jalaliCalendar.toString());
//        prefManager.setDateFormatJalaliStart(dayData.getJalaliCalendar().toString());
        prefManager.setDateFormatJalaliEnd("");
//        tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbers(dayData.getJalaliCalendar().toString()));
//        tEndDate.setText("");

        prefManager.setRange1(ConvertTimeTo.getInstance().convertToLong(jalaliCalendar));
        prefManager.setRange2(0);
//
//        prefManager.setRangeBoolean(false);
    }

    public void setJalaliRange2(){
        JalaliCalendar jalaliCalendarStart =FormatCalendar.getInstance().getFormatMiladiToJalali(prefManager.getFormatStart_Miladi());
        JalaliCalendar jalaliCalendarEnd =FormatCalendar.getInstance().getFormatMiladiToJalali(prefManager.getFormatEnd_Miladi());

        prefManager.setBoolClick1ForNotDate(true);

        prefManager.setDateFormatJalaliStart(jalaliCalendarStart.toString());
//        prefManager.setDateFormatJalaliStart(dayData.getJalaliCalendar().toString());
        prefManager.setDateFormatJalaliEnd(jalaliCalendarEnd.toString());
//        tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbers(dayData.getJalaliCalendar().toString()));
//        tEndDate.setText("");

        prefManager.setRange1(ConvertTimeTo.getInstance().convertToLong(jalaliCalendarStart));
        prefManager.setRange2(ConvertTimeTo.getInstance().convertToLong(jalaliCalendarEnd));
    }

}
