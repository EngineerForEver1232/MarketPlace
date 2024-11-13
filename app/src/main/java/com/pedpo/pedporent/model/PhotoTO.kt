package com.pedpo.pedporent.model

import android.graphics.Bitmap
import android.net.Uri

data class PhotoTO(
    var photoResource: Int?=null,
    var photoUri: Uri?=null,
    var bitmap: Bitmap?=null,
    var base64: String?=null,
    var counter:Int?=null,
    var type:String?=null
//    var sdcardPath: String?,
//    var index: Int = 0
){

}