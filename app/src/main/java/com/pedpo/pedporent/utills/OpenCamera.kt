package com.pedpo.pedporent.utills

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import javax.inject.Inject

class OpenCamera {

    private var context: Context? = null;
    private var laucher: ActivityResultLauncher<Intent>? = null;

    @Inject
    constructor() {
    }

    fun initContext(context: Context, laucher: ActivityResultLauncher<Intent>) {
        this.context = context;
        this.laucher = laucher;
    }

    // ma
    fun openCamera(): Uri {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        var imageUri = context?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!

        laucher?.launch(
            Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
            ).putExtra(
                MediaStore.EXTRA_OUTPUT,
                imageUri
            )
        )

        return imageUri;
    }


}