package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogFragMenuItemsBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.ResponseComment
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuItemFragDialog : DialogFragment() {

    var binding: DialogFragMenuItemsBinding? = null
    var returnContent: ReturnContent? = null
    private var marketID:String?=null;
    private val profileViewModel: ProfileViewModel by viewModels()


    fun newInstance(isActive: Boolean , marketID: String?):MenuItemFragDialog {
        val fragment = MenuItemFragDialog()
        val bundle = Bundle()
        bundle.putBoolean(ContextApp.ACTIVE , isActive)
        bundle.putString(ContextApp.MARKET_ID , marketID)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragMenuItemsBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding?.linearActive?.isVisible = !(arguments?.getBoolean(ContextApp.ACTIVE,false)?:false)
        binding?.lineActive?.isVisible = !(arguments?.getBoolean(ContextApp.ACTIVE,false)?:false)
        marketID = arguments?.getString(ContextApp.MARKET_ID)

        getComment(marketID = marketID?:"")

        binding?.linearDeactive?.setOnClickListener {
            returnContent?.returnContent(ContextApp.DEACTIVE)
            dismiss()
        }
        binding?.linearActive?.setOnClickListener {
            returnContent?.returnContent(ContextApp.ACTIVE)
            dismiss()
        }
        binding?.linearDelete?.setOnClickListener {
            returnContent?.returnContent(ContextApp.DELETE)
            dismiss()
        }
        binding?.linearEdit?.setOnClickListener {
            returnContent?.returnContent(ContextApp.EDIT)
            dismiss()
        }
        binding?.linearComment?.setOnClickListener {
            returnContent?.returnContent(ContextApp.COMMENT)
            dismiss()
        }


    }

    private fun getComment(marketID:String){
        profileViewModel.getStatusComment(marketID = marketID)
            ?.observe(viewLifecycleOwner,
                CustomObserver(object : CustomObserver.ResultListener<ResponseComment>{
                    override fun onSuccess(dataInput: ResponseComment) {
                        if (dataInput.isSuccess){
                            if (dataInput.isCloseComment){
                                binding?.textComment?.text = getString(R.string.close_comment)
                            }else{
                                binding?.textComment?.text = getString(R.string.open_comment)
                            }
                        }

                    }

                    override fun onException(exception: Exception) {

                    }
                })
            )
    }

}