package com.pedpo.pedporent.model.ticket.create

import com.google.gson.annotations.SerializedName

data class TicketCreateTO(
    @SerializedName("TicketSectionID")
    var ticketSectionID:String,
    @SerializedName("TicketNecessaryID")
    var ticketNecessaryID:String,
    @SerializedName("Title")
    var title:String,
    @SerializedName("Description")
    var description:String,
)