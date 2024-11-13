package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterShitftTimeBinding
import com.pedpo.pedporent.databinding.AdapterTimeBranchUserBinding
import com.pedpo.pedporent.model.TimeBranchTO
import com.pedpo.pedporent.utills.ContextApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShiftTimeUserAdapter : RecyclerView.Adapter<ShiftTimeUserAdapter.ViewHolder>  {

    private var list:ArrayList<TimeBranchTO>?=null;
    private var context:Context?=null;
    private var workTimeID:String?=null;

    @Inject
    constructor(@ApplicationContext context:Context){
        this.context = context;
        list = ArrayList<TimeBranchTO>()
    }

    fun updateAdapter(list: List<TimeBranchTO> , workTimeID:String){
        this.list = (list) as ArrayList<TimeBranchTO>?;
        this.workTimeID = workTimeID;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTimeBranchUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        binding.listenerAdapter = this;
        binding.shift1 = ContextApp.SHIFT_1
        binding.shift2 = ContextApp.SHIFT_2;
        return ViewHolder(binding = binding)
    }


    inner class ViewHolder(val binding: AdapterTimeBranchUserBinding) : RecyclerView.ViewHolder(binding.root){
            fun init(shiftWorkTO:TimeBranchTO?,position: Int){
                binding.viewModel = shiftWorkTO;

                if (shiftWorkTO?.enable == false
                    ||
                    shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.startTime2.isNullOrEmpty() &&
                    shiftWorkTO?.endTime1.isNullOrEmpty() && shiftWorkTO?.endTime2.isNullOrEmpty()){
                    binding.tDay.setTextColor(ContextCompat.getColor(context!!,R.color.gray_e))
                    binding.icTime.setColorFilter(ContextCompat.getColor(context!!,R.color.gray_c))
                    binding.icTime.setImageResource(R.drawable.ic_disable_day_branch)
//                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.gray_e) )
                }else{
                    binding.tDay.setTextColor(ContextCompat.getColor(context!!,R.color.black_standard))
                    binding.icTime.setColorFilter(ContextCompat.getColor(context!!,R.color.tinticon))
                    binding.icTime.setImageResource(R.drawable.ic_enable_day_branch)
                }

//                if (shiftWorkTO?.startTime1.isNullOrEmpty() && shiftWorkTO?.startTime2.isNullOrEmpty() &&
//                    shiftWorkTO?.endTime1.isNullOrEmpty() && shiftWorkTO?.endTime2.isNullOrEmpty()){
//                    binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.gray_e) )
//                    binding.tDay.setTextColor(ContextCompat.getColor(context!!,R.color.gray_standard))
//                    }
                Log.i("testAdapterBranch", "init: ${shiftWorkTO?.startTime1} ${shiftWorkTO?.endTime1} ${shiftWorkTO?.enable}")

            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(list?.get(position),position)
        holder.binding.position = position;


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