package com.pedpo.pedporent.view.dialog

import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class  ShowProgressBar @Inject constructor() {


    private var dialogFragmentProgress: DialogFragmentProgress? = null

    fun show(fragmentManager: FragmentManager) {
        if (dialogFragmentProgress != null)
            dialogFragmentProgress?.dismiss()
        dialogFragmentProgress = DialogFragmentProgress()
        dialogFragmentProgress?.show(fragmentManager, "progress")
    }

    fun dismiss() {
        if (dialogFragmentProgress != null) dialogFragmentProgress?.dismiss()
    }

}