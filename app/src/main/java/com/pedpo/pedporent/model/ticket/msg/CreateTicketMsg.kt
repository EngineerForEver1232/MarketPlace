package com.pedpo.pedporent.model.ticket.msg

import com.google.gson.annotations.SerializedName

data class CreateTicketMsg(
    @SerializedName("TicketID")
    var ticketID:String?,
    @SerializedName("Description")
    var description:String?
)