package com.pedpo.pedporent.listener

import android.net.Uri
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO

interface IReturnPhotoPermission {

    fun onReturnPhotoPermission(uri:Uri?);


}