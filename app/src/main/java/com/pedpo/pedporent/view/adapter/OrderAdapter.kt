package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterAssignmentBinding
import com.pedpo.pedporent.databinding.AdapterOrderBinding
import com.pedpo.pedporent.listener.ClickAdapter
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import java.util.*
import javax.inject.Inject

class OrderAdapter: RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private var list: Array<String>? = null;
    private var context:Context?=null;
    var returnContent: ClickAdapter?=null;

    @Inject
    constructor(context: Context){
        this.context = context;
    }

    fun updateAdapter(list: Array<String>) {
        this.list = list ?: arrayOf();

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = AdapterOrderBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(list?.get(position),position = position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0;
    }


    inner class ViewHolder(val binding: AdapterOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(str:String?,position:Int?) {
            binding.text.text = str ;


            binding.text.setOnClickListener {
                returnContent?.ReturnIdOrder(str?:"")
            }
        }

    }

}