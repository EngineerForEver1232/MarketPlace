package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.BottomSheetDialogBinding
import com.pedpo.pedporent.listener.ClickAdapterPlace
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PlaceAdapter
import com.pedpo.pedporent.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShowAreaDialog : BottomSheetDialogFragment(), ClickAdapterPlace {


    @Inject
    lateinit var prefApp: PrefApp

    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage

    @Inject
    lateinit var adapterPlace: PlaceAdapter

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    var pages = ArrayList<String>();

    private val viewModel: LocationViewModel by viewModels();
    var onReturnPlace: OnReturnPlace? = null;

    private var cityID: String? = null;
    private var city: String? = null;
//    private var adapterPlace: PlaceAdapter? = null;

    private lateinit var binding: BottomSheetDialogBinding;
    private var provinceID = "";
    private var countryID = "";
    private var type = "";




    private fun setupBackPressListener() {
        this.view?.isFocusableInTouchMode = true
        this.view?.requestFocus()
        this.view?.setOnKeyListener { _, keyCode, event ->
//            Log.i("clickKeyBack", "setupBackPressListener: ${pages.size}")

            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {

                if (pages.isNotEmpty())
                    pages.removeLast()

                if (pages.isEmpty())
                    dismiss()
                else {
                    if (pages.size == 1)
                        country(adStack = false)
                    else if (pages.size == 2)
                        provinces(countryID, adStack = false)
                }

                true
            } else
                false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressListener()

        val resource = MyContextWrapper.refrshWrap(requireContext())
//        isCancelable = false

        binding.tTitle.text = resource.getString(R.string.please_choose_location)

        val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_vertical_category
            )!!
        )

//        adapterPlace = PlaceAdapter(requireContext());
        adapterPlace.clickAdapterPlace = this;

        binding.recycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(divider)
            adapter = adapterPlace
        }


        country(adStack = true)
        writeCity()

    }



    private fun country(adStack: Boolean) {

        binding?.progressBar.visibility = View.VISIBLE
        viewModel.getCountries()?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<PlaceData> {
                override fun onSuccess(dataInput: PlaceData) {
                    if (dataInput?.isSuccess == true) {
                        binding?.progressBar.visibility = View.INVISIBLE

                        adapterPlace?.updateAdapter(dataInput.data!!, dataInput.typePlace)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (adStack) {
                        pages.clear()
                        pages.add("")
                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun provinces(idCountry: String, title: String? = "", adStack: Boolean) {
        viewModel.getProvinces(idCountry, title = title)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<PlaceData> {
                override fun onSuccess(dataInput: PlaceData) {

                    if (dataInput.isSuccess == true) {
                        binding.layoutSearch.isVisible = true;
                        adapterPlace.updateAdapter(
                            dataInput.data ?: listOf(),
                            dataInput.typePlace
                        );

                        if (adStack)
                            pages.add(idCountry ?: "")

                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun cities(idProvince: String, title: String?, adStack: Boolean) {
        viewModel.getCities(idProvince, title).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<PlaceData> {
                override fun onSuccess(dataInput: PlaceData) {
                    if (dataInput.isSuccess == true) {

                        adapterPlace.updateAdapter(
                            dataInput.data ?: arrayListOf(),
                            dataInput.typePlace
                        )

                        if (adStack)
                            pages.add(idProvince ?: "")

                    } else {
                        Toast.makeText(
                            requireContext(),
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onException(exception: Exception) {

                }
            })
        )
    }

    private fun writeCity() {
        binding.eSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (type == ContextApp.CITY)
                    cities(provinceID, char.toString() ?: "", adStack = false)
                else if (type == ContextApp.PROVINCE)
                    provinces(idCountry = countryID, char.toString(), adStack = false)

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private var nameCountry: String? = null

    /*Onclick Adapter Place */
    override fun OnItemClickListenerAdapter(placeTO: PlaceTO, typePlace: String?) {

        when (typePlace) {
            ContextApp.COUNTRY -> {
                type = ContextApp.PROVINCE;
                provinces(idCountry = placeTO.id ?: "", adStack = true)
                countryID = placeTO.id ?: "";
                nameCountry = placeTO.name
            }
            ContextApp.PROVINCE -> {
                type = ContextApp.CITY;

                provinceID = placeTO.id ?: ""
                binding.eSearch.text?.clear()
                cities(placeTO.id ?: "", "", adStack = true)
            }
            ContextApp.CITY -> {

                city = placeTO.name;
                cityID = placeTO.id;
                prefApp.setCityID(cityID);
                prefApp.setNameCity(name = placeTO.persianName, nameEnglish = placeTO.englishName)
                onReturnPlace?.onReturnPlace(placeTO = placeTO)
                prefApp.setNameCountry(country = nameCountry)

                dismiss();

            }
        }

    }


}