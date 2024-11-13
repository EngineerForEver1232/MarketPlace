package com.pedpo.pedporent.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.ticket.create.TicketCreateTO
import com.pedpo.pedporent.model.ticket.details.DetailsTicketData
import com.pedpo.pedporent.model.ticket.msg.CreateTicketMsg
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsData
import com.pedpo.pedporent.model.ticket.tickets.TicketData
import kotlinx.coroutines.*
import javax.inject.Inject

class TicketRepository {

    private var serviceAPI: ServiceAPI? = null;

    @Inject
    constructor(serviceAPI: ServiceAPI) {
        this.serviceAPI = serviceAPI;
    }


    fun tickets(): MutableLiveData<DataWrapper<TicketData>> {
        var mutable = MutableLiveData<DataWrapper<TicketData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.tickets();
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun createTicket(ticketCreateTO: TicketCreateTO): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("testTicket", "createTicket: ${throwable?.message}")
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.ticketCreate(ticketCreateTO = ticketCreateTO);
            Log.i("testTicket", "createTicket: ${response?.isSuccessful} ${response?.message()}")
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun detialsTicket(ticketID: String): MutableLiveData<DataWrapper<DetailsTicketData>>? {
        val mutable = MutableLiveData<DataWrapper<DetailsTicketData>>();

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper<DetailsTicketData>(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = serviceAPI?.detialsTicket(ticketID = ticketID)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

    fun sendMsg(createTicketMsg: CreateTicketMsg): MutableLiveData<DataWrapper<ResponseTO>> {
        var mutable = MutableLiveData<DataWrapper<ResponseTO>>()

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.ticketSendMsg(createTicketMsg = createTicketMsg)

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }

        return mutable;
    }

    fun ticketSections(): MutableLiveData<DataWrapper<TicketSectionsData>> {
        var mutable = MutableLiveData<DataWrapper<TicketSectionsData>>();

        var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            mutable.postValue(DataWrapper(Exception(throwable), null))
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            var response = serviceAPI?.ticketSections()
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    mutable.value = DataWrapper(null, response?.body())
                } else {
                    mutable.value = DataWrapper(Exception(response?.body()?.message), null)
                }
            }
        }
        return mutable;
    }

}