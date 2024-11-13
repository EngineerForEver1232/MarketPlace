package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogFragDepositBinding
import com.pedpo.pedporent.listener.ReturnContentInt
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.view.adapter.DepositAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.view.isVisible
import com.pedpo.pedporent.utills.ContextApp

@AndroidEntryPoint
class SecurityDepositFragDialog : DialogFragment(), ReturnContentInt {

    private lateinit var binding: DialogFragDepositBinding;

    @Inject
    lateinit var depositAdapter: DepositAdapter;

    var returnContentInt: ReturnContentInt? = null;

    var typeOfguaranteeTO = TypeOfguaranteeTO();
    var content: String? = null;

    fun newInstance(typeOfguaranteeTO: TypeOfguaranteeTO?): SecurityDepositFragDialog {
        var frag = SecurityDepositFragDialog();
        var bundle = Bundle();
        bundle.putParcelable(ContextApp.TYPE, typeOfguaranteeTO)
        frag.arguments = bundle;
        return frag;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

        typeOfguaranteeTO = arguments?.get(ContextApp.TYPE) as TypeOfguaranteeTO
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogFragDepositBinding.inflate(layoutInflater)
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog);
        if (typeOfguaranteeTO.other == true) {
            binding?.layoutOther.isVisible = true;
            binding?.eOther.setText(typeOfguaranteeTO.otherText?:"",TextView.BufferType.EDITABLE)
        }

        var recycler = binding.recycler;
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recycler.adapter = depositAdapter;

        depositAdapter.updateAdapter(
            resources?.getStringArray(R.array.deposit),
            typeOfguaranteeTO = typeOfguaranteeTO
        )
        depositAdapter.returnContent = this

        binding.btnApply.setOnClickListener {

            if (typeOfguaranteeTO.other == true && binding.eOther.text.toString().trim()
                    .isNullOrEmpty()
            ) {
                binding.eOther.requestFocus()
                binding.eOther.error = getString(R.string.enter_this_item)
            } else {
                returnContentInt?.returnContent(
                    content,
                    binding.eOther.text.toString() ?: "",
                    typeOfguaranteeTO
                )
                dialog?.dismiss();
            }
        }

    }


    override fun returnContent(
        content: String?,
        textOther: String?,
        typeOfguaranteeTO: TypeOfguaranteeTO?,
    ) {
        this.typeOfguaranteeTO = typeOfguaranteeTO!!;
        this.content = content;
//        returnContentInt?.returnContent(content,binding.eOther.text.toString()?:"",typeOfguaranteeTO)

        binding.layoutOther.isVisible = typeOfguaranteeTO?.other ?: false;
        if (typeOfguaranteeTO?.other == false) {
            binding.eOther.text?.clear();
        }

    }


}