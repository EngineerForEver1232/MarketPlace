package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterShitftTimeBinding
import com.pedpo.pedporent.model.TimeBranchTO
import com.pedpo.pedporent.utills.ContextApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShiftTimeAdapter : RecyclerView.Adapter<ShiftTimeAdapter.ViewHolder>  {

    private var list:ArrayList<TimeBranchTO>?=null;
    private var context:Context?=null;
    private var workTimeID:String?=null;

    @Inject
    constructor(@ApplicationContext context:Context){
        this.context = context;
        list = ArrayList<TimeBranchTO>()
        list?.add(TimeBranchTO(context.getString(R.string.saturday)))
        list?.add(TimeBranchTO(day = context.getString(R.string.sunDay)))
        list?.add(TimeBranchTO(day =context.getString(R.string.monDay)))
        list?.add(TimeBranchTO(day =context.getString(R.string.tuesDay)))
        list?.add(TimeBranchTO(day =context.getString(R.string.wednesDay)))
        list?.add(TimeBranchTO(day =context.getString(R.string.thursDay)))
        list?.add(TimeBranchTO(day =context.getString(R.string.friDay)))

    }

    fun updateAdapter(list: List<TimeBranchTO> , workTimeID:String){
        this.list = (list) as ArrayList<TimeBranchTO>?;
        this.workTimeID = workTimeID;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterShitftTimeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.listenerAdapter = this;
        binding.shift1 = ContextApp.SHIFT_1
        binding.shift2 = ContextApp.SHIFT_2;
        return ViewHolder(binding = binding)
    }


    inner class ViewHolder(val binding: AdapterShitftTimeBinding) : RecyclerView.ViewHolder(binding.root){
            fun init(shiftWorkTO:TimeBranchTO?,position: Int){
                binding.viewModel = shiftWorkTO;

                Log.i("testAdapterBranch", "init: ${shiftWorkTO?.getOnClickShowShift1()}")

//                    binding.tStartTime1.isVisible =
//                        !(shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.endTime1.isNullOrEmpty());
//                    binding.tEndTime1.isVisible =
//                        !(shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.endTime1.isNullOrEmpty());
//                    binding.toTime1.isVisible =
//                        !(shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.endTime1.isNullOrEmpty());
//                    binding.icClose.isVisible =
//                        !(shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.endTime1.isNullOrEmpty());


                binding.switchSat.setOnClickListener {
                    onShowDialogTime?.onOnTime(checked =  binding.switchSat.isChecked,
                        day =  position ,
                        timeWorkID = workTimeID
                    )

                    binding.constTime.isVisible = binding.switchSat.isChecked ;

                    if (shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.endTime1.isNullOrEmpty()) {
                        binding.tAdd.isVisible = true;
                        binding.icPlus.isVisible = true;
                    }
                    else if (shiftWorkTO?.startTime2.isNullOrEmpty() && shiftWorkTO?.endTime2.isNullOrEmpty()){
                        binding.tAdd.isVisible = true;
                        binding.icPlus.isVisible = true;
                    }else{
                        binding.tAdd.isVisible = false;
                        binding.icPlus.isVisible = false;
                    }

//                    binding.tAdd.isVisible =
//                        !(!shiftWorkTO?.startTime1.isNullOrEmpty() || !shiftWorkTO?.endTime1.isNullOrEmpty())
//                    binding.icPlus.isVisible =
//                        !(!shiftWorkTO?.startTime1.isNullOrEmpty() || !shiftWorkTO?.endTime1.isNullOrEmpty())
                }

                list?.get(position)?.setOnClickShowShift1(list?.get(position)?.getShowShift1())


                binding.tAdd.setOnClickListener {
                    if (list?.get(position)?.getOnClickShowShift1() == false ) {
                        binding.tStartTime1.isVisible = true;
                        binding.tEndTime1.isVisible = true;
                        binding.toTime1.isVisible = true;
                        binding.icClose.isVisible = true;
                        list?.get(position)?.setOnClickShowShift1(true)

                    } else if(list?.get(position)?.getOnClickShowShift2()== false){
                        binding.tStartTime2.isVisible = true;
                        binding.tEndTime2.isVisible = true;
                        binding.toTime2.isVisible = true;
                        binding.icCloseTime2.isVisible = true;

                        binding.icPlus.isVisible = false;
                        binding.tAdd.isVisible = false;
                    }
                }

                binding.switchSat.isChecked = shiftWorkTO?.enable?:false;

            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(list?.get(position),position)
        holder.binding.position = position;


    }

    fun onCheckedChanged(checked: Boolean,position:Int) {
//        onShowDialogTime?.onOnTime(checked =  checked, day =  position , timeWorkID = workTimeID)
    }


    /* onClick ic close  1 */
    fun onClickClose1Time(position: Int) {
            onShowDialogTime?.onDeleteTime(position,ContextApp.SHIFT_1,ContextApp.START_TIME, timeWorkID = workTimeID)
        }

    /* onClick ic close  2 */
    fun onClickClose2Time(position: Int  ) {
            onShowDialogTime?.onDeleteTime(position,ContextApp.SHIFT_2,ContextApp.START_TIME, timeWorkID = workTimeID)
        }


    /* onClick shift 1 */
    fun onClickShift1StartTime(view:View,position: Int ,shiftWorkTO:TimeBranchTO? ) {
            onShowDialogTime?.onShowDialogTime((view as MaterialTextView),position,ContextApp.SHIFT_1,ContextApp.START_TIME, timeBranchTO = shiftWorkTO)
        }
    /* onClick shift 1 */
    fun onClickShift1EndTime(view:View,position: Int ,shiftWorkTO:TimeBranchTO?  ) {
            onShowDialogTime?.onShowDialogTime((view as MaterialTextView),position,ContextApp.SHIFT_1,ContextApp.END_TIME,timeBranchTO = shiftWorkTO)
        }
    /* onClick shift 2 */
    fun onClickShift2StartTime(view:View,position: Int,shiftWorkTO:TimeBranchTO? ) {
            onShowDialogTime?.onShowDialogTime((view as MaterialTextView),position,ContextApp.SHIFT_2,ContextApp.START_TIME,timeBranchTO = shiftWorkTO)
        }
    /* onClick add shift */
    fun onClickAddShift2EndTime(view:View,position: Int,shiftWorkTO:TimeBranchTO? ) {
            onShowDialogTime?.onShowDialogTime((view as MaterialTextView),position,ContextApp.SHIFT_2,ContextApp.END_TIME,timeBranchTO = shiftWorkTO)
        }


    override fun getItemCount(): Int {
        return list?.size?:0 ;
    }

    fun setDialog(onshowDialogTime:OnShowDialogTime){
        this.onShowDialogTime = onshowDialogTime ;
    }

    interface OnShowDialogTime{
        fun onShowDialogTime(textView: MaterialTextView?,dayNumber:Int?, shift:String, typeTime:String, timeBranchTO:TimeBranchTO?)
        fun onDeleteTime(day:Int?, shift:String, typeTime:String, timeWorkID:String?)
        fun onOnTime(day:Int?, timeWorkID:String?,checked: Boolean)
    }

    private var onShowDialogTime:OnShowDialogTime?=null;


}