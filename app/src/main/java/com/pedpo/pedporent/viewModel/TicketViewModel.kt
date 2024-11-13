package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.repository.TicketRepository
import com.pedpo.pedporent.model.ticket.create.TicketCreateTO
import com.pedpo.pedporent.model.ticket.details.DetailsTicketData
import com.pedpo.pedporent.model.ticket.msg.CreateTicketMsg
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsData
import com.pedpo.pedporent.model.ticket.tickets.TicketData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel : ViewModel {

    private var ticketRepository:TicketRepository?=null;

    @Inject
    constructor(ticketRepository: TicketRepository){
        this.ticketRepository = ticketRepository;
    }

    fun tickets():LiveData<DataWrapper<TicketData>>?{
        return ticketRepository?.tickets()
    }

    fun createTicket(ticketCreateTO: TicketCreateTO):LiveData<DataWrapper<ResponseTO>>?{
        return ticketRepository?.createTicket(ticketCreateTO = ticketCreateTO)
    }

    fun detialsTicket(ticketID:String):LiveData<DataWrapper<DetailsTicketData>>?{
        return ticketRepository?.detialsTicket(ticketID)
    }

    fun sendMsg(createTicketMsg: CreateTicketMsg):LiveData<DataWrapper<ResponseTO>>?{
        return ticketRepository?.sendMsg(createTicketMsg)
    }

    fun ticketSections():LiveData<DataWrapper<TicketSectionsData>>?{
        return ticketRepository?.ticketSections()
    }

}