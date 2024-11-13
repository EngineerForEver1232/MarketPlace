package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogRateFragmentBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.RateStoreTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.GettingIP
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateDialogFragment : DialogFragment(){

    private var progressBar: ProgressBar? = null
    private var textProgress: TextView? = null

    private var returnContent:ReturnContent?=null

    fun setReturn(returnContent:ReturnContent){
        this.returnContent = returnContent;
    }

    private lateinit var binding:DialogRateFragmentBinding;
    private val profileViewModel: ProfileViewModel by viewModels();

    fun newInstance(storeID:String):RateDialogFragment{
        val fragment = RateDialogFragment()
        val bundle = Bundle()
        bundle.putString(ContextApp.STORE_ID,storeID)
        fragment.arguments = bundle;

        return fragment;
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
        binding = DialogRateFragmentBinding.inflate(inflater,container,false)
        binding.listener = this;
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

    }

    fun icClose(view: View){
        dismiss()
    }

    fun save(view: View){
        profileViewModel.setRate(
            RateStoreTO(
                ip = GettingIP(context).deviceIpAddress,
                 storeID = arguments?.getString(ContextApp.STORE_ID,"")?:"",
                rate = binding.rating.rating.toInt()
                )
        )?.observe(viewLifecycleOwner,
        CustomObserver(object :CustomObserver.ResultListener<ResponseTO>{
            override fun onSuccess(dataInput: ResponseTO) {
                if (dataInput.isSuccess == true){
                    returnContent?.returnContent("")
                    dismiss()
                }else{
                    Toast.makeText(context,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
                }
            }
            override fun onException(exception: Exception) {
                Toast.makeText(context,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
            }

        }))
    }


}