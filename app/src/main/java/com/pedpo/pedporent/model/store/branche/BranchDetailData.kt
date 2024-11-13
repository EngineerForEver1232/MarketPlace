package com.pedpo.pedporent.model.store.branche

import com.pedpo.pedporent.model.ResponseTO

class BranchDetailData :ResponseTO(){

    var data:BranchDetailTO?=null;


           data  class BranchDetailTO(
                var branchID:String,
                var name:String,
                var address:String,
                var latitude:String,
                var longitude:String,

            )

}