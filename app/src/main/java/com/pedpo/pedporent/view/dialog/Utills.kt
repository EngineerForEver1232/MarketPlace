package com.pedpo.pedporent.view.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pedpo.pedporent.R
import com.pedpo.pedporent.utills.ContextApp
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class Utills {
    private var context: Context? = null;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
    }

    fun showDialogPositive(message: String,positiveMessage:String?,negativeMessage:String?): LiveData<Boolean> {
        var mutable = MutableLiveData<Boolean>();
        AlertDialog.Builder(context!!,R.style.AlertDialog)

            .setMessage(message)
            .setPositiveButton(positiveMessage) {
                    dialog, p1 ->
                mutable.value = true;
                dialog.dismiss();
            }.setNegativeButton(negativeMessage) {
                    dialog, p ->
                mutable.value = false;
                dialog.dismiss();
            }.show();

        return mutable;
    }

    fun chooseCalendar(message: String,positiveMessage:String?,negativeMessage:String?): LiveData<Boolean> {
        var mutable = MutableLiveData<Boolean>();
        AlertDialog.Builder(context!!,R.style.AlertDialog)

            .setMessage(message)
            .setPositiveButton(positiveMessage) {
                    dialog, p1 ->
                mutable.value = true;
                dialog.dismiss()
            }.setNegativeButton(negativeMessage) {
                    dialog, p ->
                mutable.value = false;
                dialog.dismiss()
            }.show()

        return mutable;
    }


}