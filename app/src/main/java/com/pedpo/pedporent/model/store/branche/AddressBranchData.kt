package com.pedpo.pedporent.model.store.branche

import com.pedpo.pedporent.model.ResponseTO

class AddressBranchData : ResponseTO() {

    var data: AddressBranchTO?=null;

    data class AddressBranchTO(
        var branchID :String?=null,
        var name :String,
        var address :String,
        var latitude :String,
        var longitude :String
    )
}