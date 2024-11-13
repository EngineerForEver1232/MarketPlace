package com.pedpo.pedporent.view.dialog.progress

import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class  ShowLoadingPedpo @Inject constructor() {


    private var dialogFragmentProgress: DialogFragmentProgressPedpo? = null

    fun show(fragmentManager: FragmentManager?) {
        if (dialogFragmentProgress != null) dialogFragmentProgress!!.dismiss()
        dialogFragmentProgress = DialogFragmentProgressPedpo()
        dialogFragmentProgress!!.show(fragmentManager!!, "progressPedpo")
    }

    fun dismiss() {
        if (dialogFragmentProgress != null) dialogFragmentProgress!!.dismiss()
    }

}