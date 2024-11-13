package com.pedpo.pedporent.model.store.user

import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.details.PhotoDetailTO
import com.pedpo.pedporent.model.store.DataStorePhotos

class ResponseStorePhotosUser : ResponseTO() {

    var data: List<PhotoDetailTO>? = null;

}