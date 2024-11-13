package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterDepositDetailsBinding
import javax.inject.Inject

class DepositDetailsAdapter @Inject constructor(var context:Context)  :RecyclerView.Adapter<DepositDetailsAdapter.ViewHolder>(){

    private var list: List<String>? = null;


    fun updateAdapter(list:List<String>?){
        this.list = list;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val binding = AdapterDepositDetailsBinding.inflate(LayoutInflater.from(context),viewGroup,false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size ?:0
    }




    inner class ViewHolder(val binding: AdapterDepositDetailsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(str:String?){
            binding.tTitle.text = str;
        }

    }

}