package com.pedpo.pedporent.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.R

class DoRegister constructor(private val context:Context) {


    fun show():MutableLiveData<Boolean>{
        var mutable = MutableLiveData<Boolean>()
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.please_register))
            .setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                mutable.value = true;
                dialog.dismiss()
            }.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                mutable.value = false;
                dialog.dismiss()
            }.show()

        return mutable;

    }

}