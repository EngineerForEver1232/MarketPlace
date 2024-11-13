package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterTicketChatBinding
import com.pedpo.pedporent.model.ticket.details.TicketChatTO
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ChatTicketAdapter : RecyclerView.Adapter<ChatTicketAdapter.ViewHolder> {

    private var list: List<TicketChatTO>? = null;
    private var layoutInflater: LayoutInflater? = null;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = ArrayList();
    }

    fun updateAdapter(list: List<TicketChatTO>?) {
        this.list = list ?: ArrayList<TicketChatTO>();
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterTicketChatBinding.inflate(layoutInflater!!, parent, false);
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size?:0;
    }


    class ViewHolder(private val binding: AdapterTicketChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(ticketChatTO: TicketChatTO?) {
            binding.viewmodel = ticketChatTO;

//            binding.imgAdmin.isVisible = ticketChatTO?.isAdmin == true;
//            binding.imgUser.isVisible = ticketChatTO?.isAdmin != true;
        }
    }

}