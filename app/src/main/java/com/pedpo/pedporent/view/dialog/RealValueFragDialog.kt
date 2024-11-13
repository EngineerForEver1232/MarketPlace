package com.pedpo.pedporent.view.dialog

import android.content.Context
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.RealValueDialogFragmentBinding
import com.pedpo.pedporent.listener.ReturnPrice
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RealValueFragDialog : DialogFragment() {


    @Inject
    lateinit var numberFormatDigits: NumberFormatDigits
    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage

    private lateinit var binding: RealValueDialogFragmentBinding;
    var returnContent: ReturnPrice? = null;
    private var type: String? = null;
    private var value: String? = null;



    fun newInstance(type: String?,value:String?): RealValueFragDialog {
        val frag = RealValueFragDialog();
        val bundle = Bundle();
        bundle.putString(ContextApp.TYPE, type)
        bundle.putString(ContextApp.VALUE, value)
        frag.arguments = bundle;
        return frag;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

        type = arguments?.getString(ContextApp.TYPE)
        value = arguments?.getString(ContextApp.VALUE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = RealValueDialogFragmentBinding.inflate(layoutInflater, container, false);
        binding.listener = this;
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog);
//        var fArray = arrayOfNulls<InputFilter>(1)
//        fArray[0] = LengthFilter(10)


        when(type){
            ContextApp.PRICE_HOUR ,ContextApp.PRICE_DAILY,
                    ContextApp.REAL_VALUE,ContextApp.PRICE_MONTH,ContextApp.PRICE_YEAR
            ->{
                if (!value.isNullOrEmpty() && value != "null" && value.toString() !="0")
                binding.ePrice.setText(numberFormatDigits.convertToDigist(value?.toLong()?:0) , TextView.BufferType.EDITABLE)
            }
            else-> {
                if (!value.isNullOrEmpty() && value != "null" && value.toString() !="0")
                binding.ePrice.setText(value ?: "", TextView.BufferType.EDITABLE)
            }

        }

        binding.ePrice.requestFocus()
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.ePrice.postDelayed({
        inputMethodManager.showSoftInput(binding.ePrice, InputMethodManager.HIDE_IMPLICIT_ONLY);
        },200)


        if (type==ContextApp.REAL_VALUE) {
            binding.tTitle.text = getString(R.string.enter_the_real_value_of_the_goods)
            binding.ePrice.filters = arrayOf(LengthFilter(15));
            numberFormatDigits.convertEditTextNumber(binding.ePrice, prefManagerLanguage = prefManagerLanguage,16)

        } else if (type==ContextApp.PRICE_HOUR) {
            binding.tTitle.text = getString(R.string.hour_price);
            binding.ePrice.filters = arrayOf(LengthFilter(11));
            numberFormatDigits.convertEditTextNumber(binding.ePrice, prefManagerLanguage = prefManagerLanguage,12)
        } else if (type==ContextApp.PRICE_MONTH) {
            binding.tTitle.text = getString(R.string.monthly_price)
            binding.ePrice.filters = arrayOf(LengthFilter(11));
            numberFormatDigits.convertEditTextNumber(binding.ePrice, prefManagerLanguage = prefManagerLanguage,12)
        } else if (type==ContextApp.PRICE_YEAR) {
            binding.tTitle.text = getString(R.string.year_price)
            binding.ePrice.filters = arrayOf(LengthFilter(11));
            numberFormatDigits.convertEditTextNumber(binding.ePrice, prefManagerLanguage = prefManagerLanguage,12)
        } else if (type==ContextApp.METRAZH_HOME) {
            binding.tTitle.text = getString(R.string.metrazh_home)
            binding.ePrice.filters = arrayOf(LengthFilter(7));

        } else if (type==ContextApp.ROOM) {
            binding.tTitle.text = getString(R.string.bedrooms)
            binding.ePrice.filters = arrayOf(LengthFilter(2));

        } else if (type==ContextApp.BAD_ROOM) {
            binding.ePrice.filters = arrayOf(LengthFilter(2));
            binding.tTitle.text = getString(R.string.bathroom)

        } else if (type==ContextApp.YEAR_CONNSTRUCTION) {
            binding.tTitle.text = getString(R.string.year_construction)
            binding.ePrice.filters = arrayOf(LengthFilter(5));

        } else if (type==ContextApp.KM) {
            binding.tTitle.text = getString(R.string.kilometer_car)
            binding.ePrice.filters = arrayOf(LengthFilter(10));
        } else if (type?.equals(ContextApp.TYPE_FUEL)!!) {
            binding.tTitle.text = getString(R.string.fuel_type)
            binding.ePrice.filters = arrayOf(LengthFilter(15));
            binding.ePrice.inputType =
                InputType.TYPE_CLASS_TEXT
        } else if (type==ContextApp.YEAR_CONNSTRUCTION_CAR) {
            binding.tTitle.text = getString(R.string.year_constra_car);
            binding.ePrice.filters = arrayOf(LengthFilter(10));

        } else{
            binding.tTitle.text = getString(R.string.enter_the_price_daily)
            binding.ePrice.filters = arrayOf(LengthFilter(11));
            numberFormatDigits.convertEditTextNumber(binding.ePrice, prefManagerLanguage = prefManagerLanguage,12)

        }
    }

    /*onClick Confirm*/
    fun onClickConfirm(view: View) {
//        if (binding.ePrice.text.toString().trim().isNullOrEmpty()) {
//            binding.ePrice.requestFocus();
//            return
//        }
        if (type?.equals(ContextApp.REAL_VALUE)!!) {
            returnContent?.returnPriceReal(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.PRICE_HOUR)!!) {
            returnContent?.returnPriceHour(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.PRICE_MONTH)!!) {
            returnContent?.returnPriceMonth(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.PRICE_YEAR)!!) {
            returnContent?.returnPriceYear(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.METRAZH_HOME)!!) {
            returnContent?.returnMetrazhHome(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.ROOM)!!) {
            returnContent?.returnRoom(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.BAD_ROOM)!!) {
            returnContent?.returnBadRoom(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.YEAR_CONNSTRUCTION)!!) {
            returnContent?.returnYearConstruction(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.KM)!!) {
            returnContent?.returnKM(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.TYPE_FUEL)!!) {
            returnContent?.returnTypeFuel(binding.ePrice.text.toString().trim())
        } else if (type?.equals(ContextApp.YEAR_CONNSTRUCTION_CAR)!!) {
            returnContent?.returnYearConstructionCar(binding.ePrice.text.toString().trim())
        } else {
//            binding.tTitle.text = "";
            returnContent?.returnPriceDayli(binding.ePrice.text.toString().trim());
        }
        dismiss()
    }

    /*onClick Cancel*/
    fun onClickCancel(view: View) {
        dismiss()
    }


}