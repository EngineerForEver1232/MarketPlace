package com.pedpo.pedporent.model.store

import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.details.PhotoDetailTO

class DataStorePhotos : ResponseTO() {

     var images: List<PhotoDetailTO>? = null;
     var title: String? = null
     var description: String? = null
     var rateStore: Float? = null
     var isStore: Boolean? = false;

}