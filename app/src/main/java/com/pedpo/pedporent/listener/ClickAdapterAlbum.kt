package com.pedpo.pedporent.listener

import com.pedpo.pedporent.databinding.AdapterPhotoBinding
import com.pedpo.pedporent.model.PhotoTO

interface ClickAdapterAlbum {

    fun onItemClickChoosePhoto(photoTO: PhotoTO, position:Int);
    fun setOrginalPhotoInAlbums_Adapter(photo: PhotoTO, position:Int, viewHolder: AdapterPhotoBinding);
    fun onDeletePhotoFromAlbums_Adapter(photo: PhotoTO, position:Int, viewHolder: AdapterPhotoBinding);

}