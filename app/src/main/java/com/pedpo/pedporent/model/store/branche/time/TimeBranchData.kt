package com.pedpo.pedporent.model.store.branche.time

import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.TimeBranchTO

class TimeBranchData :ResponseTO() {

    var data: TimeBranchData?=null;

    data class TimeBranchData(
        var workTimeID:String?=null,
        var workTimes: List<TimeBranchTO>?=null
    )

}