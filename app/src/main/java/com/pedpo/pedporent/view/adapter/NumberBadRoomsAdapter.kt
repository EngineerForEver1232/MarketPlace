package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterNumberBadroomBinding
import com.pedpo.pedporent.listener.ReturnBadRoom
import com.pedpo.pedporent.listener.ReturnBathRoom
import com.pedpo.pedporent.listener.ReturnContent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class NumberBadRoomsAdapter @Inject constructor(@ActivityContext private val context: Context): RecyclerView.Adapter<NumberBadRoomsAdapter.ViewHolder>(){

    lateinit var binding:AdapterNumberBadroomBinding;
    private var list:Array<String>?=null;
     var returnBadRoom:ReturnBadRoom?=null;
     var returnBathRoom:ReturnBathRoom?=null;

    init {
        list = context.resources?.getStringArray(R.array.numberBadRooms)
    }

    inner class ViewHolder(val binding:AdapterNumberBadroomBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(result:String,position: Int){
            binding?.tNumber.text = result;


            binding.root?.setOnClickListener {
                returnBadRoom?.returnBadRooms(position);
                returnBathRoom?.returnBathRooms(position);

//                for (root in listView) {
//                    root.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
//                }
                for (textView in listViewTextView) {
                    textView.setTextColor(ContextCompat.getColor(context,R.color.black))
                    textView.setBackgroundResource(R.drawable.border_number_badroom)
                }
//                binding?.tNumber?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                binding?.tNumber?.setTextColor(ContextCompat.getColor(context, R.color.white))
                binding?.tNumber?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterNumberBadroomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding = binding)
    }

    private var listView = ArrayList<View>();
    private var listViewTextView = ArrayList<MaterialTextView>();


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        listView.add(holder.itemView)
        listViewTextView.add(holder.binding?.tNumber)


        holder.initView(list?.get(position)?:"",position)

    }

    override fun getItemCount(): Int {
            return list?.size?:0;
    }


}