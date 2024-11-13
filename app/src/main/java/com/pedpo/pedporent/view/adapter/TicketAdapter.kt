package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterTicketsBinding
import com.pedpo.pedporent.listener.ClickAdapterTicket
import com.pedpo.pedporent.model.ticket.tickets.TicketTO
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class TicketAdapter :RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private var layoutInflater:LayoutInflater?=null;
    private var list:List<TicketTO>?=null;
    var clickAdapterTicket:ClickAdapterTicket?=null;

    @Inject
    constructor(@ActivityContext context: Context){
        this.layoutInflater = LayoutInflater.from(context);
        list = ArrayList();
    }

    fun updateAdapter(list:List<TicketTO>?){
        this.list = list ?: ArrayList();
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = AdapterTicketsBinding.inflate(layoutInflater!!,parent,false);
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }




    inner class ViewHolder(private val binding:AdapterTicketsBinding):RecyclerView.ViewHolder(binding.root){

        fun bindView(ticketTO: TicketTO?){
            binding.viewModel = ticketTO;

            Log.i("testTicket", "bindView: ${ticketTO?.ticketStatus}")

            binding.root.setOnClickListener {
                clickAdapterTicket?.OnClickTicket(ticketTO = ticketTO)
            }

        }

    }

}