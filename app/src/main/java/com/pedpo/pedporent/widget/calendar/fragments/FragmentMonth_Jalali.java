package com.pedpo.pedporent.widget.calendar.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.utills.MyMutable;
import com.pedpo.pedporent.widget.calendar.CalendarActivity;
import com.pedpo.pedporent.widget.calendar.adapter.jalali.AdapterGrid_Jalali;
import com.pedpo.pedporent.widget.calendar.interfaceCalendar.OnItemCalendarClickLister;
import com.pedpo.pedporent.widget.calendar.utils.ArrayListCalendar;
import com.pedpo.pedporent.widget.calendar.utils.ConvertTimeTo;
import com.pedpo.pedporent.widget.calendar.utils.FormatCalendar;
import com.pedpo.pedporent.widget.calendar.utils.GridUtil;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.utils.PrefManager;
import com.pedpo.pedporent.widget.calendar.vo.DayData;
import com.pedpo.pedporent.widget.calendar.vo.MonthData;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to.RangeTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentMonth_Jalali extends Fragment implements OnItemCalendarClickLister {

    private MonthData monthData;
    private GridView gridView;
    private AdapterGrid_Jalali adapterGridJalali;
    private JalaliCalendar jalaliCalendar;
    private PrefManager prefManager;


    public void setData(MonthData monthData) {
        this.monthData = monthData;
    }

    public void setDataJalaly(JalaliCalendar jalaliCalendar) {
        this.jalaliCalendar = jalaliCalendar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_month, container, false);
//        MarkedDates_In_Frag_And_Grid.getInstance().addObserver(this);


        prefManager = new PrefManager(getContext());
        gridView = view.findViewById(R.id.gridview);

        adapterGridJalali = new AdapterGrid_Jalali(getContext(), 1, monthData.getData());

        gridView.setAdapter(adapterGridJalali);
        adapterGridJalali.setOnItemCalendarClickLister(this);


        return view;
    }

    public void initNowDate(View view) {

//        tMonthJalali = view.findViewById(R.id.tMonthJalali);
//
//        tMonthMiladi = view.findViewById(R.id.tMonthMiladi);
//        tDayMiladi = view.findViewById(R.id.tDayMiladi);
//
//        tMiladi = view.findViewById(R.id.tMiladi);
//        tJalali = view.findViewById(R.id.tjalali);
//
//        tMonthJalali.setText(CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthString());
//
//        tDayMiladi.setText(CurrentCalendar.getCurrentDateData().getDay()+"");
//        tMonthMiladi.setText( ConvertDate.newInstance().getMonth(CurrentCalendar.getCurrentDateData().getMonth()));
//
//        tJalali.setText(CurrentCalendar.getCurrentDateDataJalali().toString());
//        tMiladi.setText(CurrentCalendar.getCurrentDateData().toString());
    }


    @Override
    public void onItemCalendarClickListenerRange(View view, DayData dayData,
                                                 boolean rangeBoolean,
                                                 long jalali_StartRange,
                                                 String jalali_StartRangeString) {

        if (prefManager.getClick())
            return;

        if (prefManager.getClickAvaliable()) {
            if (GridUtil.newInstance().rangAvaliableForClickJalali(
                    dayData,
                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.startAvaliable),
                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.endAvailable)
            )
            ) {
                Log.i("jalaliCalendar", "click available: " + jalaliCalendar);
                return;
            }
        }
        //new
//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {
//            if (GridUtil.newInstance().rangNotAvaliable_For_Click_Jalali(
//                    dayData,
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i)),
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i)))
//            ) {
////                Toast.makeText(getContext(), "Only gray days", Toast.LENGTH_SHORT).show();
//                Log.i("jalaliCalendar", "click not availiable : "+jalaliCalendar);
//                return;
//            }
//        }

//        Calendar rangBaseClick = Calendar.getInstance();
//        rangBaseClick.set(dayData.getJalaliCalendar().getYear(), dayData.getJalaliCalendar().getMonth(), dayData.getJalaliCalendar().getDay());
//        ConvertTimeTo.getInstance().CovertTimeToZero(rangBaseClick);

        long calendarBase = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());
//        if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.isEmpty() &&
//                !ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.contains(calendarBase)) {
////                Log.e("sizeArray", calendar+"  =  " + calendarBase);
////                Toast.makeText(getContext(), "this date is not exists in range2", Toast.LENGTH_SHORT).show();
//            Log.i("jalaliCalendar", "click saveAvailableDays2_Click : "+jalaliCalendar);
//            return;
//        }


//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.saveAvailableDays1.size(); i++) {
//            if (!ArrayListCalendar.arrayListCalendar.saveAvailableDays1.contains(calendarBase)) {
////                Toast.makeText(getContext(), "this date is not exists in range1", Toast.LENGTH_SHORT).show();
//                Log.i("jalaliCalendar", "click saveAvailableDays1 : "+jalaliCalendar);
//
//                return;
//            }
//        }

        if (GridUtil.newInstance().clickOff(dayData))
            return;

        if (prefManager.isSingleClick()) {
                oneClickCalendar(view, dayData);
            } else {
                if (rangeBoolean) {
                    oneClickCalendar(view, dayData);
                } else if (rangeBoolean == false) {
                    towClickCalendar(view, dayData, jalali_StartRange, jalali_StartRangeString);
                }
            }


    }


    public void oneClickCalendar(View view, DayData dayData) {

        //vaghti click mishavad notavailable pak mishavad ta safe ba click jadid rangbandi shavad
//        ArrayListCalendar.arrayListCalendar.listNotAvailableStart.clear();
//        ArrayListCalendar.arrayListCalendar.listNotAvailableEND.clear();

        /**
         * ba avalin click ma  startJalali ro initialize mikonim
         */

        CalendarActivity.mutableLiveData.postValue(new RangeTO(dayData.getJalaliCalendar().toString(), ""));


        prefManager.setBoolClick1ForNotDate(true);

        prefManager.setDateFormatJalaliStart(dayData.getJalaliCalendar().toString());
        prefManager.setDateFormatJalaliEnd("");
//        tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbers(dayData.getJalaliCalendar().toString()));
//        tEndDate.setText("");

        prefManager.setRange1(ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar()));
        prefManager.setRange2(0);

        prefManager.setRangeBoolean(false);

        view.setBackgroundResource(R.drawable.border_item_calendar_green);
//        prefManager.setObserver("button");
        setMiladiRange1();


        adapterGridJalali.notifyDataSetChanged();

        MyMutable.Companion.getMutable().postValue(true);

    }

    public void towClickCalendar(View view, DayData dayData,
                                 long jalali_StartRange,
                                 String jalali_StartRangeString) {


        /**
         * ba dovomin click ma endMiladi & endJalali ro initialize mikonim
         * tip : startMiladi & startJalali ro az parameter migirim
         */

        long jalali_EndRange = ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar());

//        for (int i = 0; i < ArrayListCalendar.arrayListCalendar.listNotAvailableStart.size(); i++) {
//            if (GridUtil.newInstance().notAvaliable_For_Click2(jalali_StartRange, jalali_EndRange,
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableStart.get(i)),
//                    ConvertTimeTo.getInstance().convertToLong(ArrayListCalendar.arrayListCalendar.listNotAvailableEND.get(i)))) {
//                return;
//            }
//        }


        if (jalali_StartRange > jalali_EndRange) {

            CalendarActivity.mutableLiveData.postValue(
                    new RangeTO(dayData.getJalaliCalendar().toString(), jalali_StartRangeString)
            );

            prefManager.setRange1(ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar()));
            prefManager.setRange2(jalali_StartRange);

            prefManager.setDateFormatJalaliStart(dayData.getJalaliCalendar().toString());
            prefManager.setDateFormatJalaliEnd(jalali_StartRangeString);

            adapterGridJalali.notifyDataSetChanged();
        } else {

            CalendarActivity.mutableLiveData.postValue(
                    new RangeTO(jalali_StartRangeString, dayData.getJalaliCalendar().toString()));

            prefManager.setRange2(ConvertTimeTo.getInstance().convertToLong(dayData.getJalaliCalendar()));
            prefManager.setDateFormatJalaliEnd(dayData.getJalaliCalendar().toString());
        }
        prefManager.setRangeBoolean(true);
        setMiladiRange2();

        view.setBackgroundResource(R.drawable.border_item_calendar_green);
        adapterGridJalali.notifyDataSetChanged();
        MyMutable.Companion.getMutable().postValue(true);


    }


    public void setMiladiRange1() {

//        //TODO calendarStartForReveres bayad dar sate class bashad , chon vase barAx kar kardane calendar lazeme . chon dar if az in vase namayesh dar edittext bahre mibarim .
        Calendar calendarStartForReveres = FormatCalendar.getInstance().toMiladi(prefManager.getFormatStart_Jalali());
//        calendarStartForReveres.set(dayData.getDate().getYear(), dayData.getDate().getMonth() - 1, dayData.getDate().getDay());
        Date rang1 = new Date(
                calendarStartForReveres.get(Calendar.YEAR),
                calendarStartForReveres.get(Calendar.MONTH),
                calendarStartForReveres.get(Calendar.DAY_OF_MONTH)
        );
//
        prefManager.setBoolClick1ForNotDate(true);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(calendarStartForReveres.getTime());
        prefManager.setFormatStart_Miladi(dateString);
        prefManager.setFormatEnd_Miladi("");

        prefManager.setRange1_Miladi(rang1.getTime());
        prefManager.setRange2_Miladi(0);

        prefManager.setRangeBoolean(false);

    }

    public void setMiladiRange2() {

        Calendar calendarStart = FormatCalendar.getInstance().toMiladi(prefManager.getFormatStart_Jalali());
        Calendar calendarEnd = FormatCalendar.getInstance().toMiladi(prefManager.getFormatEnd_Jalali());

        Date rang1 = new Date(
                calendarStart.get(Calendar.YEAR),
                calendarStart.get(Calendar.MONTH),
                calendarStart.get(Calendar.DAY_OF_MONTH)
        );

        Date rang2 = new Date(
                calendarEnd.get(Calendar.YEAR),
                calendarEnd.get(Calendar.MONTH),
                calendarEnd.get(Calendar.DAY_OF_MONTH)
        );


        prefManager.setRange2_Miladi(rang2.getTime());
        prefManager.setRange1_Miladi(rang1.getTime());


        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //TODO calendarStartForReveres bayad dar sate class bashad , chon vase barAx kar kardane calendar lazeme . chon dar if az in vase namayesh dar edittext bahre mibarim .
        String dateStart = sdf.format(calendarStart.getTime());
        String dateEnd = sdf.format(calendarEnd.getTime());
        prefManager.setFormatStart_Miladi(dateStart);
        prefManager.setFormatEnd_Miladi(dateEnd);

//        CalendarActivity.mutableLiveData.postValue(new RangeTO(dateEnd, dateStart));

        prefManager.setRangeBoolean(true);

    }

}
