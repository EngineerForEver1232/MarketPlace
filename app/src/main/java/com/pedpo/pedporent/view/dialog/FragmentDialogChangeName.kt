package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogChangeNameBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDialogChangeName : DialogFragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    lateinit var binding: DialogChangeNameBinding
    var returnContent:ReturnContent?=null;

    fun newInstance(name:String?):FragmentDialogChangeName{
        var frag = FragmentDialogChangeName();
        var bundle  = Bundle();
        bundle.putString(ContextApp.NAME,name)
        frag.arguments = bundle;
        return frag;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogChangeNameBinding.inflate(inflater, container, false)
        binding.listener = this;
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_frag_dialog);


        binding?.eName.setText(arguments?.getString(ContextApp.NAME))

    }

    /*Onclick*/
    fun onClickCancel(view: View) {
        dismiss()
    }
    /*Onclick*/
    fun onClickConfirm(view: View) {

        var firstName = binding.eName.text.toString().trim();
        if (firstName.isNullOrEmpty()){
            return;
        }


        profileViewModel.setFirstName(firstName)?.observe(viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    if (dataInput.isSuccess==true){
                        returnContent?.returnContent(firstName)
                        dismiss()
                        Toast.makeText(requireContext(),dataInput.message,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(requireContext(),dataInput.message,Toast.LENGTH_SHORT).show();
                    }
                }
                override fun onException(exception: Exception) {

                }
            }))

    }

}