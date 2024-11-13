package com.pedpo.pedporent.model

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.store.edit.PhorosEditStore

class TestStore : ResponseTO() {

    var data : EditStorePhotosTO?=null;


    data class EditStorePhotosTO(

        var logo: String?=null,
        var madrak: String?=null,

        var images:List<PhotoStoreTO>?=null
    )

    data class PhotoStoreTO(

        var order:Int?=null,
        var image:String?=null


    )
}