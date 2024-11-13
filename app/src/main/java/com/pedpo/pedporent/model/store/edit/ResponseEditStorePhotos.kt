package com.pedpo.pedporent.model.store.edit

import com.google.gson.annotations.SerializedName
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.PhotoStoreTO

class ResponseEditStorePhotos : ResponseTO() {

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