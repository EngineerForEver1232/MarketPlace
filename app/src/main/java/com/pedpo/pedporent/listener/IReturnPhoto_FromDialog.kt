package com.pedpo.pedporent.listener

import android.net.Uri
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO

interface IReturnPhoto_FromDialog {

    fun onReturnPhoto_FromCamera(uri:Uri);
    fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>);
    fun onReturnSinglePhoto_FromGallery(customGalleryTO: CustomGalleryTO);

}