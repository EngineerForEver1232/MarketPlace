package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogFragAssignmentBinding
import com.pedpo.pedporent.databinding.DialogYearBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.listener.ReturnYear
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.view.adapter.AssignmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class YearDialog : DialogFragment() {

    private lateinit var binding: DialogYearBinding;

    private var returnYear: ReturnYear? = null;
    private var calendar: Calendar? = null


    fun newInstance(type:String):YearDialog{
        val fragment = YearDialog();
        val bundle = Bundle();
        bundle.putString(ContextApp.TYPE,type)

        fragment.arguments = bundle;
        return fragment;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
        calendar = Calendar.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogYearBinding.inflate(layoutInflater)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog_year);

        binding?.yearPicker.minValue = 1900;
        binding?.yearPicker.maxValue = calendar?.get(Calendar.YEAR) ?: 2030;


//        binding?.yearPicker?.setOnValueChangedListener { numberPicker, i, i2 ->
//            returnYear?.returnYear(i.toString())
//        }
        binding?.btnApply?.setOnClickListener {
            returnYear?.returnYear(
                type = arguments?.getString(ContextApp.TYPE,ContextApp.HOME)?:"",
                binding?.yearPicker?.value.toString())
            dismiss()
        }

    }


   fun setYearReturn(returnYear: ReturnYear){
       this.returnYear = returnYear;
   }


}