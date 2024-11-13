package com.pedpo.pedporent.view.dialog.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.databinding.DialogFragmentProgressPedpoBinding
import com.pedpo.pedporent.widget.calendar.utils.AnimLoadingIconPedpo

class DialogFragmentProgressPedpo :DialogFragment(){

    private lateinit var binding: DialogFragmentProgressPedpoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding = DialogFragmentProgressPedpoBinding.inflate(inflater,container,false);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageProgress.startAnimation(
            AnimLoadingIconPedpo.getInstance().anim(requireContext())
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);


    }


}