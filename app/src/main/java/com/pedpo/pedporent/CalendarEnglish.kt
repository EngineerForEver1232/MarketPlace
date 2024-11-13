package com.pedpo.pedporent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


import com.google.android.material.button.MaterialButton
import com.pedpo.pedporent.model.profile.CalendarData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.calendar.utils.AppContents
import com.pedpo.pedporent.widget.calendar.utils.FormatCalendar
import com.pedpo.pedporent.widget.calendarEnglish.customviews.DateRangeCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

import java.util.*

@AndroidEntryPoint
class CalendarEnglish : AppCompatActivity() {

    lateinit var calendar: DateRangeCalendarView;
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_test)

        var marketID = intent.getStringExtra(ContextApp.MARKET_ID)
        var type = intent.getStringExtra(ContextApp.TYPE)

        calendar = findViewById(R.id.calendar)



        Log.i("testCalendr", "Date : $marketID $type")




        profileViewModel.datesDeActive(marketID ?: "", type ?: "")?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<CalendarData> {
                override fun onSuccess(dataInput: CalendarData) {

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val calendarStart = Calendar.getInstance()
                    val calendarEnd = Calendar.getInstance()
                    try {
                        calendarStart.time = Date(format.parse(dataInput.data?.startDate).time)
                        calendarEnd.time = Date(format.parse(dataInput.data?.endDate).time)

                        calendar.setSelectedDateRange(calendarStart, calendarEnd);

                    } catch (e: Exception) {
                    }

                    Log.i("testCalendr", "onSuccess: ${dataInput.data?.startDate}")
                    Log.i("testCalendr", "onSuccess: ${dataInput.data?.endDate}")
                }

                override fun onException(exception: Exception) {

                }

            })
        )


        Log.i("testDeactive", "returnContent: ${calendar.startDate.toString()}")

        findViewById<MaterialButton>(R.id.btn).setOnClickListener {

            Log.i("testDeactive", "start : ${calendar.startDate?.time}")
            Log.i("testDeactive", "end : ${calendar.endDate?.time}")
            Log.i("testDeactive", "end : ${calendar.endDate?.timeInMillis}")


            Log.i("testDeactive", "start : ${FormatCalendar.getInstance().formatMiladi_To_Miladi(calendar.startDate) }")
            Log.i("testDeactive", "end : ${FormatCalendar.getInstance().formatMiladi_To_Miladi(calendar.endDate) }")
//            Log.i("testDeactive", "start : ${calendar?.startDate?.time}")

            var intent = Intent()
            intent.putExtra(AppContents.FROM_CALENDAR_MILADI,
                FormatCalendar.getInstance().formatMiladi_To_Miladi(calendar.startDate))
            intent.putExtra(AppContents.TO_CALENDAR_MILADI,
                FormatCalendar.getInstance().formatMiladi_To_Miladi(calendar.endDate))
            setResult(AppContents.RESULT_SET_CALENDAR_POSTER,intent)
            finish()
        }

    }


}