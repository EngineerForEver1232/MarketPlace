package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.R


class DialogFragmentProgress : DialogFragment(){

    private var progressBar: ProgressBar? = null
    private var textProgress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog_progressbar);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialog!!.window!!.attributes)
//        //        lp.width = (int)context.getResources().getDimension(R.dimen.width_dialog);
////        lp.width = context!!.resources.getDimension(R.dimen.width_dialog_progress).toInt()
//        lp.width = requireContext().resources.getDimension(R.dimen.width_dialog_progress).toInt()
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//
//        dialog!!.window!!.attributes = lp
//        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog_transparent);
//
//        progressBar = view.findViewById(R.id.progress)
//        textProgress = view.findViewById(R.id.textProgress)
    }


}