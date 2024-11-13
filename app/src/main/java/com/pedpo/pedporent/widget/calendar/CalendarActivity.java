package com.pedpo.pedporent.widget.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.textview.MaterialTextView;
import com.pedpo.pedporent.R;
import com.pedpo.pedporent.model.profile.CalendarData;
import com.pedpo.pedporent.utills.ContextApp;
import com.pedpo.pedporent.utills.CustomObserver;
import com.pedpo.pedporent.utills.MyContextWrapper;
import com.pedpo.pedporent.utills.MyMutable;
import com.pedpo.pedporent.utills.language.PrefManagerLanguage;
import com.pedpo.pedporent.view.dialog.ShowProgressBar;
import com.pedpo.pedporent.viewModel.ProfileViewModel;
import com.pedpo.pedporent.widget.calendar.adapter.jalali.AdapterCalendar_Jalali;
import com.pedpo.pedporent.widget.calendar.adapter.jalali.AdapterGridTitle_Jalali;
import com.pedpo.pedporent.widget.calendar.interfaceCalendar.OnclickDatePicker;
import com.pedpo.pedporent.widget.calendar.utils.AnimLoadingIconPedpo;
import com.pedpo.pedporent.widget.calendar.utils.AppContents;
import com.pedpo.pedporent.widget.calendar.utils.ArrayListCalendar;
import com.pedpo.pedporent.widget.calendar.utils.CalendarUtil;
import com.pedpo.pedporent.widget.calendar.utils.ConvertTimeTo;
import com.pedpo.pedporent.widget.calendar.utils.CurrentCalendar;
import com.pedpo.pedporent.widget.calendar.utils.FormatCalendar;
import com.pedpo.pedporent.widget.calendar.utils.JalaliCalendar;
import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian;
import com.pedpo.pedporent.widget.calendar.utils.PositionViewPager;
import com.pedpo.pedporent.widget.calendar.utils.PrefManager;
import com.pedpo.pedporent.widget.calendar.utils.ZoomOutPageTransformer;
import com.pedpo.pedporent.widget.calendar.adapter.miladi.AdapterCalendar_Miladi;
import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to.RangeTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        OnclickDatePicker {

    private ViewPager viewPager;

    private AdapterCalendar_Jalali adapterCalendarJalali;
    private AdapterCalendar_Miladi adapterCalendarMiladi;
    private ImageView btnBack, btnNext, imageProgress;
    private PrefManager prefManager;
    private Button btnApply, btnClear;
    private View arrow1, arrowPrevious;
    private GridView gridViewTitle;
    private AdapterGridTitle_Jalali adapterGridTitleJalali;
    private TextView tDate;
    private String type = "";
    private String marketID = "";
    private String startDate = "";
    private String endDate = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tEndDate, tStartDate;

    private ProfileViewModel profileViewModel;

    private ZoomOutPageTransformer zoomOutPageTransformer;
    private PrefManagerLanguage prefManagerLanguage;
    private String[] monthString;
    public static MutableLiveData<RangeTO> mutableLiveData;
    private MaterialTextView mTypeCalendar;
    private ShowProgressBar showProgressBar;
    private Calendar calendaCurrent;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefManager = new PrefManager(this);
        calendaCurrent = Calendar.getInstance();
//        if (savedInstanceState == null)
//            prefManager.clear();
        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_en_old);
        showProgressBar = new ShowProgressBar();

        monthString = getResources().getStringArray(R.array.month_string);
        prefManagerLanguage = new PrefManagerLanguage(this);
        marketID = getIntent().getStringExtra(ContextApp.MARKET_ID);
        startDate = getIntent().getStringExtra(ContextApp.START_DATE);
        endDate = getIntent().getStringExtra(ContextApp.END_DATE);
        type = (getIntent().getStringExtra(ContextApp.TYPE) == null) ? "" : getIntent().getStringExtra(ContextApp.TYPE);

        findView();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        if (prefManager.getCalendar().equals(ContextApp.MILADI)) {
            mTypeCalendar.setText(getResources().getString(R.string.shamsi));
            if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
                tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(prefManager.getFormatStart_Miladi()));
                tEndDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(prefManager.getFormatEnd_Miladi()));
            } else {
                tStartDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(prefManager.getFormatStart_Miladi()));
                tEndDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(prefManager.getFormatEnd_Miladi()));
            }
            initViewPagerEN();
        } else {
            mTypeCalendar.setText(getResources().getString(R.string.miladi));
            if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
                tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(prefManager.getFormatStart_Jalali()));
                tEndDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(prefManager.getFormatEnd_Jalali()));
            } else {
                tStartDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(prefManager.getFormatStart_Jalali()));
                tEndDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(prefManager.getFormatEnd_Jalali()));
            }
            initViewPAger();
        }

        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, (rangeTO) -> {
                    if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
                        tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(rangeTO.getStartDate()));
                        tEndDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(rangeTO.getEndDate()));
                    } else {
                        tStartDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(rangeTO.getStartDate()));
                        tEndDate.setText(NumberFormatPersian.getNewInstance().toNumberEnlish(rangeTO.getEndDate()));
                    }
                    findViewById(R.id.groupLabelDate).setVisibility(View.VISIBLE);
                }
        );


        loadDate();

    }

    public void loadDate(){


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        try {
            calendarStart.setTime(new Date(Objects.requireNonNull(format.parse(startDate)).getTime()));
            calendarEnd.setTime(new Date(Objects.requireNonNull(format.parse(endDate)).getTime()));

            JalaliCalendar jalaliCalendar = new JalaliCalendar();
            GregorianCalendar gregorianCalendar = (GregorianCalendar) calendarStart;
            jalaliCalendar.fromGregorian(gregorianCalendar);

            JalaliCalendar jalaliCalendar2 = new JalaliCalendar();
            GregorianCalendar gregorianCalendar2 = (GregorianCalendar) calendarEnd;
            jalaliCalendar2.fromGregorian(gregorianCalendar2);


            prefManager.setRange1(ConvertTimeTo.getInstance().convertToLong(jalaliCalendar));
            prefManager.setRange2(ConvertTimeTo.getInstance().convertToLong(jalaliCalendar2));

            initDate_Miladi_FromServer();


//                            initViewPAger();
            MyMutable.Companion.getMutable().postValue(true);


        } catch (Exception e) {
            Log.e("jalaliCalendar", "onCreate: " + e.getMessage());

        }
    }


    public void initDate_Miladi_FromServer() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();


        calendarStart.setTime(new Date(format.parse(startDate).getTime()));
        calendarEnd.setTime(new Date(format.parse(endDate).getTime()));
        Log.i("calendarEN", "start: }" + calendarStart.getTime().getDay());
        Log.i("calendarEN", "end: }" + calendarEnd.getTime().getDay());
        Log.i("calendarEN", "start: }" + startDate);
        Log.i("calendarEN", "end: }" + endDate);

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

        prefManager.setRange1_Miladi(rang1.getTime());
        prefManager.setRange2_Miladi(rang2.getTime());
    }

    public void initLoadingIconPedpo() {
        imageProgress = findViewById(R.id.imageProgress);
//        Animation animation = AnimationUtils.loadAnimation(DetailsToolsActivity.this, R.anim.anim_progress);
        imageProgress.startAnimation(AnimLoadingIconPedpo.getInstance().anim(CalendarActivity.this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void findView() {

        findViewById(R.id.layoutTypeCalendar).setOnClickListener(this);
        mTypeCalendar = findViewById(R.id.typeCalendar);
        initLoadingIconPedpo();

        tStartDate = findViewById(R.id.tStartDate);
        tEndDate = findViewById(R.id.tEndDate);
        tEndDate.setOnClickListener(this);
        tStartDate.setOnClickListener(this);


        gridViewTitle = findViewById(R.id.gridviewTitle);
        viewPager = findViewById(R.id.viewPagerCalendar);
        tDate = findViewById(R.id.tDate);
        tDate.setOnClickListener(this);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        arrow1 = findViewById(R.id.arrowNext);
        arrowPrevious = findViewById(R.id.arrowPrevious);
        arrow1.setOnClickListener(this);
        arrowPrevious.setOnClickListener(this);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        adapterCalendarJalali = new AdapterCalendar_Jalali(getSupportFragmentManager());
        adapterCalendarMiladi = new AdapterCalendar_Miladi(getSupportFragmentManager());
        zoomOutPageTransformer = new ZoomOutPageTransformer();


    }


    // vase meqdarDehi textviewStartDate and EndDate , k mitavanad aslan nabashad chon hengam vorod nabayad chizi save bashad
    public void init_Start_End_Date() {

        JalaliCalendar jalaliCalendar = new JalaliCalendar();
//        Calendar calendarNowDate = Calendar.getInstance();
//        calendarNowDate.set(jalaliCalendar.getYear(), jalaliCalendar.getMonth(), jalaliCalendar.getDay());
        prefManager.setNowDateLong(ConvertTimeTo.getInstance().convertToLong(jalaliCalendar));
        prefManager.setNowDateString(jalaliCalendar.toString());
    }


    public void initViewPAger() {

        adapterGridTitleJalali = new AdapterGridTitle_Jalali(this, 1, new ArrayList(Arrays.asList(getResources().getStringArray(R.array.week))));
        gridViewTitle.setAdapter(adapterGridTitleJalali);
        gridViewTitle.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        viewPager.setAdapter(adapterCalendarJalali);
        viewPager.setPageTransformer(false, zoomOutPageTransformer);
        viewPager.setCurrentItem(500);

        arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);


        if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
            tDate.setText(String.format("%s %s", CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthString(),
                    NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                            CurrentCalendar.getCurrentJalali().getYear() + "")));
        } else {
            tDate.setText(String.format("%s %s", CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthStringEN(),
                    NumberFormatPersian.getNewInstance().toNumberEnlish(
                            CurrentCalendar.getCurrentJalali().getYear() + "")));
        }

        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(pagerChangeJalali);


    }

    ViewPager.OnPageChangeListener pagerChangeJalali = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position < 500) {
                viewPager.setScrollX(0);
                viewPager.setScrollY(0);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (position == 500) {
                arrowPrevious.setEnabled(false);
                arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);
                if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
                    tDate.setText(String.format("%s %s", CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthString(),
                            NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                                    CurrentCalendar.getCurrentJalali().getYear() + "")));
                } else {
                    tDate.setText(String.format("%s %s", CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthStringEN(),
                            NumberFormatPersian.getNewInstance().toNumberEnlish(
                                    CurrentCalendar.getCurrentJalali().getYear() + "")));
                }
            } else {
                arrowPrevious.setEnabled(true);
                arrowPrevious.setBackgroundResource(R.drawable.selector_action_calendar_pervious);

                int yearJalaly = CalendarUtil.position2YearJalaly(position);
                int monthJalaly = (int) CalendarUtil.position2MonthJalaly(position);
                JalaliCalendar jalaliCalendar = new JalaliCalendar(yearJalaly, monthJalaly, monthJalaly / 2);

                if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {
                    tDate.setText(String.format("%s %s", jalaliCalendar.getMonthString(), NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getYear() + "")));
                } else {
                    tDate.setText(String.format("%s %s", jalaliCalendar.getMonthStringEN(), NumberFormatPersian.getNewInstance().toNumberEnlish(jalaliCalendar.getYear() + "")));
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.e("viewpager", "onPageScrollStateChanged: " + state);
        }
    };

    public void initViewPagerEN() {

        adapterGridTitleJalali = new AdapterGridTitle_Jalali(this, 1, new ArrayList(Arrays.asList(getResources().getStringArray(R.array.weekEN))));
        gridViewTitle.setAdapter(adapterGridTitleJalali);
        gridViewTitle.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        viewPager.setPageTransformer(false, zoomOutPageTransformer);
        viewPager.setAdapter(adapterCalendarMiladi);
        viewPager.setCurrentItem(500);

        arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);


        Calendar cal = Calendar.getInstance();

        if (prefManagerLanguage.getLanguage().equals(ContextApp.FA)) {
            tDate.setText(String.format("%s %s",
                    NumberFormatPersian.getNewInstance().toPersianNumbersSample(cal.get(Calendar.DATE)+"")
                            + " " + monthString[calendaCurrent.get(Calendar.MONTH)],
                    NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                            calendaCurrent.get(Calendar.YEAR) + ""))
            );
        }else{
            tDate.setText(String.format("%s %s",
                    calendaCurrent.getDisplayName(Calendar.DAY_OF_WEEK, 0, Locale.US)
                            + " " + cal.get(Calendar.DATE)
                            + " " + monthString[calendaCurrent.get(Calendar.MONTH)],
                    calendaCurrent.get(Calendar.YEAR) + "")
            );
        }


        viewPager.removeOnPageChangeListener(pagerChangeJalali);
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < 500) {
                    viewPager.scrollTo(0, 0);
                }
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 500) {
                    arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);
                    if (prefManagerLanguage.getLanguage().equals(ContextApp.FA)) {
                        tDate.setText(String.format("%s %s",
                                monthString[calendaCurrent.get(Calendar.MONTH)],
                                NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                                        calendaCurrent.get(Calendar.YEAR) + ""))
                        );
                    }else{
                        tDate.setText(String.format("%s %s",
                                calendaCurrent.getDisplayName(Calendar.DAY_OF_WEEK, 0, Locale.US)
                                        + " " + calendaCurrent.get(Calendar.DATE)
                                        + " " + monthString[calendaCurrent.get(Calendar.MONTH)],
                                calendaCurrent.get(Calendar.YEAR) + "")
                        );
                    }
                } else {
                    arrowPrevious.setBackgroundResource(R.drawable.selector_action_calendar_pervious);

                    int year = com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.CalendarUtil.position2Year(position);
                    int month = com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.CalendarUtil.position2Month(position);
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month - 1);
                    cal.set(Calendar.DAY_OF_MONTH, 5);

                    if (!prefManagerLanguage.getLanguage().equals(ContextApp.EN)) {

                        tDate.setText(String.format("%s %s", monthString[month - 1],
                                        NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                                                cal.get(Calendar.YEAR) + "")
                                )
                        );
                    } else {
                        tDate.setText(String.format("%s %s", monthString[month - 1],
                                        NumberFormatPersian.getNewInstance().toNumberEnlish(
                                                cal.get(Calendar.YEAR) + "")
                                )
                        );
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layoutTypeCalendar:


                if (prefManager.getCalendar().equals(ContextApp.MILADI)) {
                    prefManager.setCalendar(ContextApp.SHAMSI);
                    mTypeCalendar.setText(getResources().getString(R.string.shamsi));

                    gridViewTitle.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                    initViewPAger();
                    viewPager.setCurrentItem(500);

                    recreate();
                } else {
                    prefManager.setCalendar(ContextApp.MILADI);
                    mTypeCalendar.setText(getResources().getString(R.string.miladi));

                    gridViewTitle.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    viewPager.setCurrentItem(500);

//                    initViewPagerEN();
                    recreate();
                }

                break;

            case R.id.tEndDate:
                if (PositionViewPager.getInstance().position(this) != -1)
                    viewPager.setCurrentItem(500 + PositionViewPager.getInstance().position(this));
                break;

            case R.id.tStartDate:
                if (PositionViewPager.getInstance().positionStart(this) != -1)
                    viewPager.setCurrentItem(500 + PositionViewPager.getInstance().positionStart(this));
                break;
            case R.id.tDate:
//                Dialog_DatePicker.newInstance().showDialog(CalendarActivity.this);
//                Dialog_DatePicker.newInstance().setOnclickDatePicker(CalendarActivity.this);

                break;
            case R.id.arrowNext:
            case R.id.btnNext:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
            case R.id.arrowPrevious:
            case R.id.btnBack:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;
            case R.id.btnClear:
                if (prefManager.getFormatStart_Jalali().isEmpty()) {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    prefManager.clear();
                    viewPager.setCurrentItem(500);
                    init_Start_End_Date();
                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.clear();
                    tStartDate.setText("");
                    tEndDate.setText("");
                } else {
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    prefManager.clear();
                    init_Start_End_Date();
                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.clear();
                    tStartDate.setText("");
                    tEndDate.setText("");
                    viewPager.setCurrentItem(500);
//                    MarkedDates_In_Frag_And_Grid.getInstance().refresh();
                }
                finish();
                break;

            case R.id.btnApply:

                if (prefManager.getFormatStart_Jalali().trim().length() == 0) {
                    Toast.makeText(CalendarActivity.this, "Please enter start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (prefManager.getFormatEnd_Jalali().trim().length() == 0) {
                    Toast.makeText(CalendarActivity.this, "Please enter end date", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra(AppContents.FROM_CALENDAR_Jalali, prefManager.getFormatStart_Jalali());
                intent.putExtra(AppContents.TO_CALENDAR_Jalali, prefManager.getFormatEnd_Jalali());
                intent.putExtra(AppContents.FROM_CALENDAR_MILADI, FormatCalendar.getInstance().formatMiladi(prefManager.getFormatStart_Jalali()));
                intent.putExtra(AppContents.TO_CALENDAR_MILADI, FormatCalendar.getInstance().formatMiladi(prefManager.getFormatEnd_Jalali()));
                setResult(AppContents.RESULT_SET_CALENDAR_POSTER, intent);
                finish();

                break;
        }
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        viewPager.setCurrentItem(500);
        init_Start_End_Date();
        ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.clear();

//        MarkedDates_In_CalendarAc.getInstance().refresh();
//        MarkedDates_In_Frag_And_Grid.getInstance().refresh();
    }

    @Override
    public void onClickDatePicker(View view, int year, int month) {
        viewPager.setCurrentItem(500 + PositionViewPager.getInstance().positionMonthDatepicker(this, year, month));

    }

}
