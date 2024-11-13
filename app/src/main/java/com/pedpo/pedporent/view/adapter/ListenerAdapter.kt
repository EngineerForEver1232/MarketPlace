package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterListenerBinding
import com.pedpo.pedporent.listener.OnClickItemAdapterLiseen
import com.pedpo.pedporent.model.liseening.LiseeningTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ListenerAdapter @Inject constructor(@ActivityContext private val context:Context): RecyclerView.Adapter<ListenerAdapter.ViewHolder>(){

    var onClickItemAdapterLiseen:OnClickItemAdapterLiseen?=null;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterListenerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    private var list:ArrayList<LiseeningTO>?=null;

    fun update(list:ArrayList<LiseeningTO>){
        this.list =list;
        notifyDataSetChanged()
    }

    fun removeItem(position: Int?) {
        if (position!=null) {
            list?.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun restoreItem(item: LiseeningTO?, position: Int?) {
        if (item!=null && position !=null) {
            list?.add(position, item)
            notifyItemInserted(position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.initView(liseeningTO = list?.get(position))
    }

    override fun getItemCount(): Int {

        return  list?.size?:0;
    }

    inner class ViewHolder(val binding: AdapterListenerBinding) :
        RecyclerView.ViewHolder(binding?.root) {

        @JvmField
        var viewForeground: ConstraintLayout = binding.constraintListener

//        var viewForeground:ConstraintLayout? = binding.constraintListener;


        fun initView(liseeningTO: LiseeningTO?){

            binding?.root?.setOnClickListener {
                onClickItemAdapterLiseen?.onItemclickAdapterLiseening(liseeningTO = liseeningTO)
            }

            binding?.tCategory.text = liseeningTO?.categoryName
            binding?.tCity.text = liseeningTO?.cityName

            if (liseeningTO?.priceTo == 0 && liseeningTO?.priceFrom == 0) {
                binding?.tPrice.text = "0 ${context?.getString(R.string.to)} ∞"
            }else if (liseeningTO?.priceFrom == 0 && liseeningTO?.priceTo !=0){
                binding?.tPrice.text = "0 ${context?.getString(R.string.to)} ${liseeningTO?.priceTo}";
            }else if(liseeningTO?.priceFrom != 0 && liseeningTO?.priceTo ==0){
                binding?.tPrice.text = "${liseeningTO?.priceFrom} ${context?.getString(R.string.to)} ∞";

            }else{
                binding?.tPrice.text = "${liseeningTO?.priceFrom} ${context?.getString(R.string.to)} ${liseeningTO?.priceTo}";
            }
            }

    }

}