//package com.pedpo.pedporent.widget.myCalendarEnglish.calendar;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.Group;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.android.material.textview.MaterialTextView;
//import com.pedpo.pedporent.R;
//import com.pedpo.pedporent.model.profile.CalendarData;
//import com.pedpo.pedporent.utills.ContextApp;
//import com.pedpo.pedporent.utills.CustomObserver;
//import com.pedpo.pedporent.utills.MyContextWrapper;
//import com.pedpo.pedporent.utills.language.PrefManagerLanguage;
//import com.pedpo.pedporent.viewModel.ProfileViewModel;
//import com.pedpo.pedporent.widget.calendar.adapter.AdapterGridTitle;
//import com.pedpo.pedporent.widget.calendar.utils.AppContents;
//import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian;
//import com.pedpo.pedporent.widget.calendar.utils.PositionViewPager;
//import com.pedpo.pedporent.widget.calendar.utils.ZoomOutPageTransformer;
//import com.pedpo.pedporent.widget.calendar.vo.MarkedDates_In_Frag_And_Grid;
//import com.pedpo.pedporent.widget.calendar.adapter.miladi.AdapterCalendarViewEN;
//import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.to.RangeTO;
//import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.ArrayListCalendar;
//import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.CalendarUtil;
//import com.pedpo.pedporent.widget.myCalendarEnglish.calendar.utils.PrefCalendarEN;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import dagger.hilt.android.AndroidEntryPoint;
//
//@AndroidEntryPoint
//public class CalendarActivity extends AppCompatActivity implements View.OnClickListener  {
//
//
//    private ViewPager viewPager;
//    private AdapterCalendarViewEN adapterCalendarViewEN;
//
//    private String type = "";
//    private ImageView btnBack, btnNext;
//    private Button btnApply, btnClear;
//    private View arrow1, arrowPrevious;
//    private GridView gridViewTitle;
//    private AdapterGridTitle adapterGridTitle;
//    private TextView tDate;
//    private String marketID = "";
//    private TextView tEndDate, tStartDate;
//    private String[] monthString;
//    private PrefCalendarEN prefCalendarEN;
//
//    public static MutableLiveData<RangeTO> mutableLiveData;
//    private ZoomOutPageTransformer zoomOutPageTransformer;
//    private ProfileViewModel profileViewModel ;
//    private Group groupLabelDate;
//    private PrefManagerLanguage prefManagerLanguage;
//
//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        startActivity(getIntent());
//        finish();
//    }
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.calendar_activity_en);
//
//        marketID = (getIntent().getStringExtra(ContextApp.MARKET_ID)==null)?"": getIntent().getStringExtra(ContextApp.MARKET_ID);
//        type = (getIntent().getStringExtra(ContextApp.TYPE)==null)?"": getIntent().getStringExtra(ContextApp.TYPE);
//
//        prefManagerLanguage = new PrefManagerLanguage(this);
//        mutableLiveData = new MutableLiveData<>();
//        prefCalendarEN = new PrefCalendarEN(this);
//        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//
//        zoomOutPageTransformer = new ZoomOutPageTransformer();
//        monthString = getResources().getStringArray(R.array.month_string);
//        findView();
//        initViewPager();
//        mutableLiveData.observe(this, new Observer<RangeTO>() {
//            @Override
//            public void onChanged(RangeTO s) {
//                if (prefManagerLanguage.getLanguage()==ContextApp.EN){
//                    tStartDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(s.getStartDate()));
//                    tEndDate.setText(NumberFormatPersian.getNewInstance().toPersianNumbersSample(s.getEndDate()));
//                }else{
//                    tStartDate.setText(s.getStartDate());
//                    tEndDate.setText(s.getEndDate());
//                }
//
//                groupLabelDate.setVisibility(View.VISIBLE);
//            }
//        }
//    );
//
////        switchConditionCalendar();
////        init_Start_End_Date();
//
////        setCondition_For_Date();
//
//        clearDate();
//        loadData();
//
//    }
//
//    public void loadData(){
//
////        showProgressBar.show(getSupportFragmentManager());
//        profileViewModel.datesDeActive(marketID,type).observe(this,
//                new CustomObserver<>(new CustomObserver.ResultListener<CalendarData>() {
//                    @Override
//                    public void onSuccess(CalendarData dataInput) {
////                        showProgressBar.dismiss();
//
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                        Calendar calendarStart = Calendar.getInstance();
//                        Calendar calendarEnd = Calendar.getInstance();
//                        try {
//                            calendarStart.setTime(new Date(format.parse(dataInput.getData().getStartDate()).getTime()));
//                            calendarEnd.setTime(new Date(format.parse(dataInput.getData().getEndDate()).getTime()));
//                            Log.i("calendarEN", "start: }"+calendarStart.getTime().getDay());
//                            Log.i("calendarEN", "end: }"+calendarEnd.getTime().getDay());
//                            Log.i("calendarEN", "start: }"+dataInput.getData().getStartDate());
//                            Log.i("calendarEN", "end: }"+dataInput.getData().getEndDate());
//
//                            ArrayListCalendar.arrayListCalendar.arrayListStart.add(calendarStart);
//
////                            ArrayListCalendar.arrayListCalendar.arrayListEND = jalaliCalendar;
////                                    ArrayListCalendar.arrayListCalendar.listNotAvailableEND.clear()
//                            ArrayListCalendar.arrayListCalendar.arrayListEND.add(calendarEnd);
////                            ArrayListCalendar.arrayListCalendar.endNotAvailable = jalaliCalendar2;
//                            Date rang1 = new Date(
//                                    calendarStart.get(Calendar.YEAR),
//                                    calendarStart.get(Calendar.MONTH),
//                                    calendarStart.get(Calendar.DAY_OF_MONTH)
//                            );
//                            Date rang2 = new Date(
//                                    calendarEnd.get(Calendar.YEAR),
//                                    calendarEnd.get(Calendar.MONTH),
//                                    calendarEnd.get(Calendar.DAY_OF_MONTH)
//                            );
//
//                            prefCalendarEN.setRange1(rang1.getTime());
//                            prefCalendarEN.setRange2(rang2.getTime());
//
//                            initViewPager();
//                            adapterCalendarViewEN.notifyDataSetChanged();
//
//
//
//
//                        } catch (Exception e) {
//                        }
//
//                    }
//
//                    @Override
//                    public void onException(@NonNull Exception exception) {
//
//                    }
//                }));
//
//    }
//
//
//    public void findView() {
//
//
//        ((MaterialTextView)findViewById(R.id.typeCalendar)).setText(getString(R.string.shamsi));
//
//        groupLabelDate = findViewById(R.id.groupLabelDate);
//
//        tStartDate = findViewById(R.id.tStartDate);
//        tEndDate = findViewById(R.id.tEndDate);
//        tEndDate.setOnClickListener(this);
//        tStartDate.setOnClickListener(this);
//
//        findViewById(R.id.icCalendar).setOnClickListener(this);
//        findViewById(R.id.typeCalendar).setOnClickListener(this);
//
//        gridViewTitle = findViewById(R.id.gridviewTitle);
//        adapterGridTitle = new AdapterGridTitle(this, 1, new ArrayList(Arrays.asList(getResources().getStringArray(R.array.weekEN))));
//        gridViewTitle.setAdapter(adapterGridTitle);
//        viewPager = findViewById(R.id.viewPagerCalendar);
//        tDate = findViewById(R.id.tDate);
//        tDate.setOnClickListener(this);
//
//        btnNext = findViewById(R.id.btnNext);
//        btnBack = findViewById(R.id.btnBack);
//
//        arrow1 = findViewById(R.id.arrowNext);
//        arrowPrevious = findViewById(R.id.arrowPrevious);
//        arrow1.setOnClickListener(this);
//        arrowPrevious.setOnClickListener(this);
//
//        btnNext.setOnClickListener(this);
//        btnBack.setOnClickListener(this);
//
//        btnApply = findViewById(R.id.btnApply);
//        btnApply.setOnClickListener(this);
//        btnClear = findViewById(R.id.btnClear);
//        btnClear.setOnClickListener(this);
//
//
//    }
//
//
//    public void setCondition_For_Date() {
//        if (type.equals(AppContents.RENT)) {
//            prefCalendarEN.setClickAvaliable(true);
//            prefCalendarEN.setClick(false);
////            CalendarActivity.sharedPreferences.edit().putBoolean("clickAvaliable", true).apply();
////            CalendarActivity.sharedPreferences.edit().putBoolean("click", false).apply();
////            Toast.makeText(this, "rent", Toast.LENGTH_SHORT).show();
//            clearDate();
//
//            return;
//        }
//        if (type.equals(AppContents.ACTIVITY_DETAILS_TOOLS)) {
//            prefCalendarEN.setClickAvaliable(true);
//            prefCalendarEN.setClick(true);
////            CalendarActivity.sharedPreferences.edit().putBoolean("clickAvaliable", true).apply();
////            CalendarActivity.sharedPreferences.edit().putBoolean("click", true).apply();
////            Toast.makeText(this, "tools", Toast.LENGTH_SHORT).show();
//            clearDate();
//
//            return;
//        }
//        if (type.equals(AppContents.ACTIVITY_DETAILS_TRADES)) {
//            prefCalendarEN.setClickAvaliable(true);
//            prefCalendarEN.setClick(true);
////            CalendarActivity.sharedPreferences.edit().putBoolean("clickAvaliable", true).apply();
////            // chon in calendar  faghat vazife namayesh avaliable ra dard pas ckick nabayad dashte bashad .
////            CalendarActivity.sharedPreferences.edit().putBoolean("click", true).apply();
////            Toast.makeText(this, "trades", Toast.LENGTH_SHORT).show();
//            clearDate();
//
//            return;
//        }
//        if (type.equals(AppContents.HIRE)) {
//
//            prefCalendarEN.setClickAvaliable(true);
//            prefCalendarEN.setClick(false);
////            CalendarActivity.sharedPreferences.edit().putBoolean("clickAvaliable", true).apply();
////            CalendarActivity.sharedPreferences.edit().putBoolean("click", false).apply();
////            Toast.makeText(this, "hire", Toast.LENGTH_SHORT).show();
//            clearDate();
//
//            return;
//        }
//
//        prefCalendarEN.setClickAvaliable(false);
//        prefCalendarEN.setClick(false);
////        CalendarActivity.sharedPreferences.edit().putBoolean("clickAvaliable", false).apply();
////        CalendarActivity.sharedPreferences.edit().putBoolean("click", false).apply();
//        clearDate();
//        adapterCalendarViewEN = new AdapterCalendarViewEN(getSupportFragmentManager(), viewPager);
//        viewPager.setAdapter(adapterCalendarViewEN);
//        viewPager.setCurrentItem(500);
//        viewPager.clearOnPageChangeListeners();
//    }
//
//    public void initViewPager(){
//
//        adapterCalendarViewEN = new AdapterCalendarViewEN(getSupportFragmentManager(), viewPager);
//        viewPager.setPageTransformer(false, zoomOutPageTransformer);
//        viewPager.setAdapter(adapterCalendarViewEN);
//        viewPager.setCurrentItem(500);
//        viewPager.clearOnPageChangeListeners();
//        arrowPrevious.setEnabled(false);
//        arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);
//
//        Calendar cal = Calendar.getInstance();
//
//        tDate.setText(String.format("%s %s", monthString[cal.get(Calendar.MONTH)],
//                cal.get(Calendar.YEAR) + ""));
////        tDate.setText(String.format("%s %s", cal.getDisplayName(Calendar.MONTH,1,Locale.US),
////                cal.get(Calendar.YEAR) + ""));
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position < 500) {
////                    viewPager.computeScroll();
//                    viewPager.setScrollX(0);
//                    viewPager.setScrollY(0);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("viewpager", "position: "+position );
//
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        FragmentMonth.mutable.postValue(true);
////                    }
////                }, 250);
//
//
//                if (position == 500) {
//                    arrowPrevious.setEnabled(false);
//                    arrowPrevious.setBackgroundResource(R.drawable.selector_left_disable);
////                    tDate.setText(String.format("%s %s",
////                            CurrentCalendar.getCurrentJalali().getDayOfWeekDayMonthString(),
////                            NumberFormatPersian.getNewInstance().toPersianNumbersSample(
////                                    CurrentCalendar.getCurrentJalali().getYear() + ""))
////                    );
//                } else {
//                    arrowPrevious.setEnabled(true);
//                    arrowPrevious.setBackgroundResource(R.drawable.selector_action_calendar_pervious);
//
////                    int yearJalaly = CalendarUtil.position2YearJalaly(position);
////                    int monthJalaly = (int) CalendarUtil.position2MonthJalaly(position);
////                    JalaliCalendar jalaliCalendar = new JalaliCalendar(yearJalaly, monthJalaly, monthJalaly / 2);
////
////                    tDate.setText(String.format("%s %s", jalaliCalendar.getMonthString(),
////                            NumberFormatPersian.getNewInstance().toPersianNumbersSample(
////                                    jalaliCalendar.getYear() + ""))
////                    );
//                }
//                int year = CalendarUtil.position2Year(position);
//                int month =  CalendarUtil.position2Month(position);
//                cal.set(Calendar.YEAR,year);
//                cal.set(Calendar.MONTH,month-1);
//                cal.set(Calendar.DAY_OF_MONTH,5);
//
//                tDate.setText(String.format("%s %s", monthString[month-1],
//                        cal.get(Calendar.YEAR) + ""));
////                tDate.setText(String.format("%s %s", cal.getDisplayName(Calendar.MONTH,1,Locale.US),
////                        cal.get(Calendar.YEAR) + ""));
////                Log.e("onDateChanged", "onDateChanged: " + jalaliCalendar.getMonthString() + " " +
////                        NumberFormatPersian.getNewInstance().toPersianNumbersSample(jalaliCalendar.getYear() + "") + " position : " + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.i("viewpager", "onPageScrollStateChanged: " + state);
//            }
//        });
//
//    }
//
//    public void clearDate() {
//        //meghdar rang1 & rang2 ro 0 mikonim k dar calendarhae dg tasir nadashte bashe
//        prefCalendarEN.setRange1(0);
//        prefCalendarEN.setRange2(0);
//        //for edittextDate
//        prefCalendarEN.setDateFormatMiladiStart("");
//        prefCalendarEN.setDateFormatMiladiEnd("");
//
//        //clear arraylist notavaliable , chon nabayad dar save shavad , balke meghdar jadid bayad bgirad
////        ArrayListCalendar.arrayListCalendar.arrayListStart.clear();
////        ArrayListCalendar.arrayListCalendar.arrayListEND.clear();
////
////        //clear Avaliable : chon nabayad range sabz dar clandar user bashad , k shayad male yek trades digast
////        ArrayListCalendar.arrayListCalendar.startAvaliable.clear();
////        ArrayListCalendar.arrayListCalendar.endAvaliable.clear();
//
//        /* vase inke zamani rang1 ro entekhab mikonim in false mishavad va bedone inke rang2 ra entekhab konim kharej beshim
//          rang1 clear mishavad vali nobate entekhab hanoz ba rang2 hast , pas rang1 = 0 , range2 = new choose */
//        prefCalendarEN.setRangeBoolean(true);
//        prefCalendarEN.setBoolClick1ForNotDate(false);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//
//            case R.id.icCalendar:
//            case R.id.typeCalendar:
//                Intent intentCalendar = new Intent(CalendarActivity.this, com.pedpo.pedporent.widget.calendar.CalendarActivity.class);
//                intentCalendar.putExtra(ContextApp.MARKET_ID , marketID);
//                intentCalendar.putExtra(ContextApp.TYPE , type);
//                startActivity(intentCalendar);
//                finish();
//
//                break;
//            case R.id.tEndDate:
//                if (PositionViewPager.getInstance().positionMiladiEnd(this) != -1)
//                    viewPager.setCurrentItem(500 + PositionViewPager.getInstance().positionMiladiEnd(this));
//                break;
//
//            case R.id.tStartDate:
//                if (PositionViewPager.getInstance().positionMiladiStart(this) != -1)
//                    viewPager.setCurrentItem(500 + PositionViewPager.getInstance().positionMiladiStart(this));
//                break;
//            case R.id.tDate:
////                Dialog_DatePicker.newInstance().showDialog(CalendarActivity.this);
////                Dialog_DatePicker.newInstance().setOnclickDatePicker(CalendarActivity.this);
//
//                break;
//            case R.id.arrowNext:
//            case R.id.btnNext:
//                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                break;
//            case R.id.arrowPrevious:
//            case R.id.btnBack:
//                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//                break;
//            case R.id.btnClear:
//                if (prefCalendarEN.getDateFormatMiladiStart().isEmpty()) {
//
//                    prefCalendarEN.clear();
//                    viewPager.setCurrentItem(500);
////                    switchConditionCalendar();
////                    init_Start_End_Date();
//                    com.pedpo.pedporent.widget.calendar.utils.ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.clear();
//                    tStartDate.setText("");
//                    tEndDate.setText("");
//                    MarkedDates_In_Frag_And_Grid.getInstance().refresh();
//                } else {
//
//                    prefCalendarEN.clear();
////                    init_Start_End_Date();
////                    switchConditionCalendar();
////                    ArrayListCalendar.arrayListCalendar.saveAvailableDays2_Click.clear();
//                    tStartDate.setText("");
//                    tEndDate.setText("");
//                    viewPager.setCurrentItem(500);
////                    MarkedDates_In_Frag_And_Grid.getInstance().refresh();
//                }
//                finish();
//                break;
//
//            case R.id.btnApply:
//
//                if (prefCalendarEN.getDateFormatMiladiStart().trim().length() == 0) {
//                    Toast.makeText(CalendarActivity.this, "Please enter start date", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (prefCalendarEN.getDateFormatMiladiEnd().trim().length() == 0) {
//                    Toast.makeText(CalendarActivity.this, "Please enter end date", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Intent intent = new Intent();
//                intent.putExtra(AppContents.FROM_CALENDAR_MILADI, prefCalendarEN.getDateFormatMiladiStart());
//                intent.putExtra(AppContents.TO_CALENDAR_MILADI, prefCalendarEN.getDateFormatMiladiEnd());
//                setResult(AppContents.RESULT_SET_CALENDAR_POSTER, intent);
//                finish();
//
//
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        prefCalendarEN.clear();
//    }
//
//
//}
