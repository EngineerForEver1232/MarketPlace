package com.pedpo.pedporent.view.store.branch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.TimeStoreActivityBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.TimeBranchTO
import com.pedpo.pedporent.model.store.branche.DeleteTimeRequest
import com.pedpo.pedporent.model.store.branche.time.EnableTimeResponse
import com.pedpo.pedporent.model.store.branche.time.EnableWorkTime
import com.pedpo.pedporent.model.store.branche.time.TimeBranchData
import com.pedpo.pedporent.model.store.branche.time.TimeBranchRequest
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.ConvertTimePicker
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.ShiftTimeAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.Utills
import com.pedpo.pedporent.viewModel.BrancheStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimeBranchStoreActivity : AppCompatActivity() , ShiftTimeAdapter.OnShowDialogTime {

    @Inject
    lateinit var adapterTime:ShiftTimeAdapter;
    @Inject
    lateinit var showProgressBar: ShowProgressBar
    private val viewModel:BrancheStoreViewModel by viewModels();
    lateinit var binding:TimeStoreActivityBinding;
    private var list:ArrayList<TimeBranchTO>?=null;
    private var brancheID:String?=null;


//    private var shiftWorkTO:TimeBranchTO?=null;
//    private var dayNumber : Int?=0;
//    private var shift : String?="";
//    private var typeTime : String?="";
    private var workTimeID : String?="";

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimeStoreActivityBinding.inflate(layoutInflater);
        setContentView(binding.root);
        setToolbar();



        brancheID = intent.getStringExtra(ContextApp.BRANCHE_ID)?:""

        list = ArrayList();
        list?.add(TimeBranchTO(day = getString(R.string.saturday)));
        list?.add(TimeBranchTO(day = getString(R.string.sunDay)));
        list?.add(TimeBranchTO(day = getString(R.string.monDay)));
        list?.add(TimeBranchTO(day = getString(R.string.tuesDay)));
        list?.add(TimeBranchTO(day = getString(R.string.wednesDay)));
        list?.add(TimeBranchTO(day = getString(R.string.thursDay)));
        list?.add(TimeBranchTO(day = getString(R.string.friDay)));

        binding.recycler.layoutManager = LinearLayoutManager(this@TimeBranchStoreActivity , LinearLayoutManager.VERTICAL ,false);
        adapterTime.setDialog(this)
        binding.recycler.adapter = adapterTime;

        if (!brancheID.isNullOrEmpty())
            getTimeBranch()

        binding.swipeRefreshLayout.setOnRefreshListener {
            getTimeBranch();
            binding.swipeRefreshLayout.isRefreshing = false;
        }
    }
    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.time)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    fun getTimeBranch(){

        viewModel.getTimeBranch(branchID = brancheID?:"")?.observe(this ,
        CustomObserver(object : CustomObserver.ResultListener<TimeBranchData>{
            override fun onSuccess(dataInput: TimeBranchData) {

                Log.i("testTimeBranche", "onSuccess WorkTime: ${dataInput.data?.workTimeID}")


                Log.i("testAdapterTime", "onSuccess: ${dataInput.isSuccess}")
                Log.i("testAdapterTime", "size: ${dataInput.data?.workTimes?.size}")
                Log.i("testAdapterTime", "onSuccess: ${dataInput.data?.workTimeID}")

                if (dataInput.isSuccess == true){
                    if (!dataInput.data?.workTimeID.isNullOrEmpty()) {
                        adapterTime.updateAdapter(
                            dataInput.data?.workTimes ?: arrayListOf(),
                            dataInput.data?.workTimeID ?: ""
                        )
                        workTimeID = dataInput.data?.workTimeID;
                        Log.i("testTimeBranche", "onSuccess WorkTime: ${workTimeID}")

                    }
                }else{

                }

            }

            override fun onException(exception: Exception) {

            }

        }))
    }


    override fun onShowDialogTime(textView: MaterialTextView?,dayNumber:Int?, shift:String, typeTime:String, timeBranchTO:TimeBranchTO?) {

        val timePicker = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setHour(8)
            .setMinute(10)
            .build()

        timePicker.addOnPositiveButtonClickListener { dialog ->
            val newHour: Int = timePicker.hour ;
            val newMinute: Int = timePicker.minute ;

            val formattedTime = ConvertTimePicker.convertTime(hourOfDay = newHour , minute = newMinute)
            textView?.text = formattedTime;
            timePickerDialogListener(formattedTime = formattedTime, timeBranchTO = timeBranchTO, shift = shift, typeTime = typeTime, dayNumber = dayNumber)
        }

//        timePicker.onActionModeStarted(ActionMode.TYPE_FLOATING)
        timePicker.show(supportFragmentManager,"timePicker")

    }

    fun timePickerDialogListener(formattedTime:String, timeBranchTO: TimeBranchTO?,shift:String,typeTime:String,dayNumber: Int?){
        if (shift == ContextApp.SHIFT_1) {
            if (typeTime == ContextApp.START_TIME) {
                timeBranchTO?.startTime1 = formattedTime
                addTimeBranch(
                    startOrEnd = ContextApp.START,
                    time = formattedTime,
                    dayNumber = dayNumber?:0,
                    shift = 1,
                    on = timeBranchTO?.enable == true
                )
            }
            else{
                timeBranchTO?.endTime1 = formattedTime
                addTimeBranch(
                    startOrEnd = ContextApp.END,
                    time = formattedTime,
                    dayNumber = dayNumber?:0,
                    shift = 1,
                    on = timeBranchTO?.enable == true
                )
            }
        }
        else {
            if (typeTime == ContextApp.START_TIME) {
                timeBranchTO?.startTime2 = formattedTime
                addTimeBranch(
                    startOrEnd = ContextApp.START,
                    time = formattedTime,
                    dayNumber = dayNumber?:0,
                    shift = 2,
                    on = timeBranchTO?.enable == true
                )
            }
            else {
                timeBranchTO?.endTime2 = formattedTime
                addTimeBranch(
                    startOrEnd = ContextApp.END,
                    time = formattedTime,
                    dayNumber = dayNumber?:0,
                    shift = 2,
                    on = timeBranchTO?.enable == true
                )
            }
        }
        list?.set(
            dayNumber?:0,
            timeBranchTO?:TimeBranchTO()
        )

//        adapterTime.updateAdapter(list = list?: ArrayList(), workTimeID = workTimeID?:"")
    }


    @Inject
    lateinit var utills:Utills

    override fun onDeleteTime(
        day: Int?,
        shift: String,
        typeTime: String,
        timeWorkID: String?

    ) {
        utills.showDialogPositive(
            getString(R.string.sure_delete_shift_time_branch),
            getString(R.string.yes),
            getString(R.string.no)
        ).observe(this, Observer {
            if (it==true)
                if (shift == ContextApp.SHIFT_1)
                    deleteWorkTime(workTime=  timeWorkID?:"",dayNumber = day?:0, shift = 1  )
                else
                    deleteWorkTime(workTime=  timeWorkID?:"",dayNumber = day?:0, shift = 2  )
            else {

            }
        })

    }

    override fun onOnTime(day: Int?, timeWorkID: String?, checked: Boolean) {
        enableWorkTime(workTime = workTimeID , dayNumber = day?:0 , checked = checked)
    }

    fun addTimeBranch(startOrEnd:String , time:String,dayNumber: Int,shift: Int,on:Boolean){

        showProgressBar.show(supportFragmentManager)
        Log.i("testTimeBranche", "onSuccess: $dayNumber")

        val time = TimeBranchRequest(
            branchID = brancheID?:"" ,
            startOrEnd = startOrEnd,
            time = time,
            dayNumber = dayNumber+1,
            shiftTime = shift,
            on = on
        );

        viewModel.addTimeBranch(time)?.observe(this,
        CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
            override fun onSuccess(dataInput: ResponseTO) {
                showProgressBar.dismiss()
                Log.i("testTimeBranche", "onSuccess: ${dataInput.isSuccess}")

                Log.i("testTimeBranche", "onSuccess: ${dataInput.isSuccess}")
                Log.i("testTimeBranche", "onSuccess: ${dataInput.message}")


                if (dataInput.isSuccess == true){
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onException(exception: Exception) {
                Log.e("testTimeBranche", "onSuccess: ${exception.message}")

            }

        }))
    }

    fun deleteWorkTime(workTime:String ,dayNumber: Int,shift: Int){
        showProgressBar.show(supportFragmentManager)

        val time = DeleteTimeRequest(
            workTimeID = workTime ,
            dayNumber = dayNumber+1,
            shiftTime = shift
        );

        viewModel.deleteWorkTime(time)?.observe(this,
        CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
            override fun onSuccess(dataInput: ResponseTO) {
                showProgressBar.dismiss()

                if (dataInput.isSuccess == true){

                    getTimeBranch()
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onException(exception: Exception) {
                Toast.makeText(this@TimeBranchStoreActivity,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
                showProgressBar.dismiss()
            }

        }))
    }

    fun enableWorkTime(workTime:String ?,dayNumber: Int , checked: Boolean){
        showProgressBar.show(supportFragmentManager)
        Log.i("testTimeBranche", "worktime: $workTime")
        Log.i("testTimeBranche", "brancheID: $brancheID")

        val time = EnableWorkTime(
            branchID = if (brancheID.isNullOrEmpty()) null else brancheID,
            workTimeID = if (workTime.isNullOrEmpty()) null else workTime,
            dayNumber = dayNumber+1,
            enable = checked
        );

        viewModel.enableWorkTime(time)?.observe(this,
        CustomObserver(object : CustomObserver.ResultListener<EnableTimeResponse>{
            override fun onSuccess(dataInput: EnableTimeResponse) {
                showProgressBar.dismiss()

                if (dataInput.isSuccess == true){
                    if (workTimeID.isNullOrEmpty())
                    workTimeID = dataInput.data;
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }else{
                    if (!workTime.isNullOrEmpty())
                    Toast.makeText(this@TimeBranchStoreActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onException(exception: Exception) {
                Toast.makeText(this@TimeBranchStoreActivity,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
                showProgressBar.dismiss()
            }

        }))
    }


}