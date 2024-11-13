package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogFragDepositBinding
import com.pedpo.pedporent.listener.MessageEvent
import com.pedpo.pedporent.listener.ReturnContentSelector
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.view.adapter.SelectorAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class SelectorFragDialog constructor(private val returnContentSelector: ReturnContentSelector?):
    DialogFragment(), ReturnContentSelector {


    private lateinit var binding: DialogFragDepositBinding;

    @Inject
    lateinit var selectorAdapter: SelectorAdapter;

//    var returnContentSelector: ReturnContentSelector? = null;

    fun newInstance(type: String): SelectorFragDialog {
        var selectorFragDialog = SelectorFragDialog(returnContentSelector);
        var bundle = Bundle()
        bundle.putString("type", type)

        selectorFragDialog.arguments = bundle;

        return selectorFragDialog;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
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



        var recycler = binding.recycler;
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recycler.adapter = selectorAdapter;
        selectorAdapter.returnContent = this;


        if (arguments?.get("type") == ContextApp.PARKING){

            selectorAdapter.updateAdapter(resources?.getStringArray(R.array.parkings))
            binding.tTitle.text = getString(R.string.parking);
            binding?.btnApply.isVisible = false;
        }else if(arguments?.get("type") == ContextApp.HEATING){
            selectorAdapter.updateAdapter(resources?.getStringArray(R.array.heating))
            binding.tTitle.text = getString(R.string.heating);
            binding?.btnApply.isVisible = false;
        }else if(arguments?.get("type") == ContextApp.COOLING){
            selectorAdapter.updateAdapter(resources?.getStringArray(R.array.cooling))
            binding.tTitle.text = getString(R.string.cooling);
            binding?.btnApply.isVisible = false;
        }
        selectorAdapter.returnContent = this;

        binding.btnApply.setOnClickListener {


        }

    }


    var content: String? = null;


    override fun returnContentSelector(
        type:String?,
        content: String?,
        position:Int?
    ) {
        this.content = content;

        if (arguments?.get("type") == ContextApp.PARKING){
            returnContentSelector?.returnContentSelector(ContextApp.PARKING,content,position = position);

            EventBus.getDefault().post(MessageEvent(content?:"test"))

        }else if(arguments?.get("type") == ContextApp.HEATING){
            returnContentSelector?.returnContentSelector(ContextApp.HEATING,content,position = position);

        }else if(arguments?.get("type") == ContextApp.COOLING){
            returnContentSelector?.returnContentSelector(ContextApp.COOLING,content,position = position);

        }

        dismiss()


    }


}