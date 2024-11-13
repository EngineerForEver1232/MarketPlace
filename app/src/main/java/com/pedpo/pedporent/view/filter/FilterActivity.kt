package com.pedpo.pedporent.view.filter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityFilterBinding
import com.pedpo.pedporent.listener.*
import com.pedpo.pedporent.model.filter.FilterTransfer
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.NumberBadRoomsAdapter
import com.pedpo.pedporent.view.addMarket.CategoryActivity
import com.pedpo.pedporent.view.dialog.SelectorFragDialog
import com.pedpo.pedporent.view.dialog.ShowAreaDialog
import com.pedpo.pedporent.widget.ButtonSwitch
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


@AndroidEntryPoint
class FilterActivity : AppCompatActivity() , OnReturnPlace ,ReturnBadRoom,ReturnBathRoom,
    ReturnContentSelector {

    lateinit var binding : ActivityFilterBinding;
    private var city :String?=null;
    private var filterTransfer:FilterTransfer?=null;
    @Inject
    lateinit var adapterNumberRoom:NumberBadRoomsAdapter;
    @Inject
    lateinit var adapterBathRooms:NumberBadRoomsAdapter;
    var page:String?=null;

    var listRenteral = arrayListOf<String>("RentPriceDay","HourlyRentalPrice",
        "MonthlyRentalPrice","YearlyRentPrice")

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater);
        binding.listener = this;
        setContentView(binding.root);
        setToolbar();

        var adapterFilter = ArrayAdapter.createFromResource(this,R.array.rental,R.layout.spinner_item_renteral)
        binding.spinnerRenteral.adapter = adapterFilter;

        page = intent.getStringExtra(ContextApp.PAGE)?: "Filter" ;

        if (page == ContextApp.LISEENING){
            supportActionBar?.title = getString(R.string.listenning);

            if (intent.getStringExtra(ContextApp.TYPE_API)== ContextApp.RENT){
                filterTransfer?.typePrice = ContextApp.RENT;
                onClickBtnCategoryRent(binding.btnCategoryRent);
            }else if (intent.getStringExtra(ContextApp.TYPE_API)== ContextApp.SALE){
                filterTransfer?.typePrice = ContextApp.SALE;
                onClickBtnCategorySale(binding.btnCategorySale);
            }else if (intent.getStringExtra(ContextApp.TYPE_API)== ContextApp.SERVICE){
                filterTransfer?.typePrice = ContextApp.SERVICE;
                onClickBtnCategoryService(binding.btnCategoryService);
            }
        }else{
            supportActionBar?.title = getString(R.string.filter);
        }



        binding.spinnerRenteral.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterTransfer?.typeOFPrice = listRenteral[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }


        filterTransfer = FilterTransfer("",null,null,ContextApp.RENT,0,
            0,false, free = false, registerDate = null, iP = ""
        );

        binding.rangeSlider.addOnChangeListener { rangeSlider, value, fromUser ->

            binding.tFrom.text ="$"+ rangeSlider.values[0].toInt().toString()
            binding.tTo.text = "$"+rangeSlider.values[1].toInt().toString()
            binding.tLine.text ="-"

            filterTransfer?.priceFrom =rangeSlider.values[0].toLong();
            filterTransfer?.priceTo =rangeSlider.values[1].toLong();


            if (rangeSlider.values[0].toInt()<=0){
                filterTransfer?.priceFrom =0;


                binding.tFrom.text =getString(R.string.under)

                binding.tFrom.isVisible = true;
                binding.tTo.isVisible = true;
            }
            if (rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                filterTransfer?.priceTo =0;

                binding.tTo.text =getString(R.string.over)

                binding.tFrom.isVisible = true;
                binding.tTo.isVisible = true;
            }

            if (rangeSlider.values[0].toInt()<=0 && rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                binding.tLine.text =getString(R.string.any_price)
                binding.tFrom.isVisible = false;
                binding.tTo.isVisible = false;

                filterTransfer?.priceFrom =0;
                filterTransfer?.priceTo =0;

            }


        }

        changeRangeMetrazh()
        changeRangeYearConst()
        changeRangeKilometrCar()

        recyclerNumberRoom()
        recyclerNumberBathRooms()
    }


    fun recyclerNumberRoom(){
        binding?.recyclerBadroom.layoutManager = LinearLayoutManager(this@FilterActivity,LinearLayoutManager.HORIZONTAL,false)
        binding?.recyclerBadroom.adapter = adapterNumberRoom;
        adapterNumberRoom.returnBadRoom = this

    }

    fun recyclerNumberBathRooms(){
        binding?.recyclerBathRooms.layoutManager = LinearLayoutManager(this@FilterActivity,LinearLayoutManager.HORIZONTAL,false)
        binding?.recyclerBathRooms.adapter = adapterBathRooms;
        adapterBathRooms?.returnBathRoom = this;

    }


    fun setToolbar(){
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    fun changeRangeMetrazh(){

        binding.rangeMetrazhSlider.addOnChangeListener { rangeSlider, value, fromUser ->

            binding.tMetrazhFrom.text = rangeSlider.values[0].toInt().toString()
            binding.tMetrazhTo.text =  rangeSlider.values[1].toInt().toString()
            binding.tMLine.text ="-"

            filterTransfer?.meterOfHouseFrom =rangeSlider.values[0].toInt();
            filterTransfer?.meterOfHouseTo =rangeSlider.values[1].toInt();


            if (rangeSlider.values[0].toInt()<=0){
                filterTransfer?.meterOfHouseFrom =0;


                binding.tMetrazhFrom.text =getString(R.string.under)

                binding.tMetrazhFrom.isVisible = true;
                binding.tMetrazhTo.isVisible = true;
            }
            if (rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                filterTransfer?.meterOfHouseTo =0;

                binding.tMetrazhTo.text =getString(R.string.over)

                binding.tMetrazhFrom.isVisible = true;
                binding.tMetrazhTo.isVisible = true;
            }

            if (rangeSlider.values[0].toInt()<=0 && rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                binding.tMLine.text = getString(R.string.any_metrazh)
                binding.tMetrazhFrom.isVisible = false;
                binding.tMetrazhTo.isVisible = false;

                filterTransfer?.meterOfHouseFrom =0;
                filterTransfer?.meterOfHouseTo =0;

            }
        }
    }

    fun changeRangeYearConst(){

        binding.rangeYearConsSlider.addOnChangeListener { rangeSlider, value, fromUser ->

            binding.tYearConsFrom.text = rangeSlider.values[0].toInt().toString()
            binding.tYearConsTo.text =  rangeSlider.values[1].toInt().toString()
            binding.tYearConsLine.text ="-"

            filterTransfer?.yearOfHouseFrom =rangeSlider.values[0].toInt();
            filterTransfer?.yearOfHouseTo =rangeSlider.values[1].toInt();


            if (rangeSlider.values[0].toInt()<=0){
                filterTransfer?.yearOfHouseFrom =0;


                binding.tYearConsFrom.text =getString(R.string.under)

                binding.tYearConsFrom.isVisible = true;
                binding.tYearConsTo.isVisible = true;
            }
            if (rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                filterTransfer?.yearOfHouseTo =0;

                binding.tYearConsTo.text =getString(R.string.over)

                binding.tYearConsFrom.isVisible = true;
                binding.tYearConsTo.isVisible = true;
            }

            if (rangeSlider.values[0].toInt()<=0 && rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                binding.tYearConsLine.text = getString(R.string.any_year)
                binding.tYearConsFrom.isVisible = false;
                binding.tYearConsTo.isVisible = false;

                filterTransfer?.yearOfHouseFrom =0;
                filterTransfer?.yearOfHouseTo =0;

            }


        }


    }

    fun changeRangeKilometrCar(){

        binding.rangeKmCarSlider.addOnChangeListener { rangeSlider, value, fromUser ->

            binding.tKMCarFrom.text = rangeSlider.values[0].toInt().toString()
            binding.tKmCarTo.text =  rangeSlider.values[1].toInt().toString()
            binding.tKMCarLine.text ="-"

//            filterTransfer?.yearOfHouseFrom =rangeSlider.values[0].toInt();
//            filterTransfer?.yearOfHouseTo =rangeSlider.values[1].toInt();


            if (rangeSlider.values[0].toInt()<=0){
//                filterTransfer?.yearOfHouseFrom =0;


                binding.tKMCarFrom.text =getString(R.string.under)

                binding.tKMCarFrom.isVisible = true;
                binding.tKmCarTo.isVisible = true;
            }
            if (rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
//                filterTransfer?.yearOfHouseTo =0;

                binding.tKmCarTo.text =getString(R.string.over)

                binding.tKMCarFrom.isVisible = true;
                binding.tKmCarTo.isVisible = true;
            }

            if (rangeSlider.values[0].toInt()<=0 && rangeSlider.values[1].toInt()>=getString(R.integer.toPrice).toInt()){
                binding.tKMCarLine.text = getString(R.string.any)
                binding.tKMCarFrom.isVisible = false;
                binding.tKmCarTo.isVisible = false;

//                filterTransfer?.yearOfHouseFrom =0;
//                filterTransfer?.yearOfHouseTo =0;

            }


        }


    }

    /*OnClick onClick DropDown */
    fun onClickDropDown(view: View) {
        if (isHome) {
            binding.linearAdvancedHome.isVisible = !binding.linearAdvancedHome.isVisible;
            filterTransfer?.isAdvanced = !binding.linearAdvancedHome.isVisible;
        }else if (isCar){
            binding.constraintKilometerCar.isVisible = !binding.constraintKilometerCar.isVisible;
            filterTransfer?.isAdvanced = !binding.constraintKilometerCar.isVisible;
        }
    }

    fun onCheckedPrice(boolean: Boolean){
        when(boolean){
            true->{
                binding.constraintSliderPrice.isVisible = true;
                if (filterTransfer?.typePrice!=ContextApp.SALE)
                    binding.constraintSpinner.isVisible = true;


                filterTransfer?.priceTo = 0;
                filterTransfer?.priceFrom =0;


            }
            false->{

                binding.constraintSliderPrice.isVisible = false;
                if (filterTransfer?.typePrice!=ContextApp.SALE)
                    binding.constraintSpinner.isVisible = false;

                filterTransfer?.priceTo = null;
                filterTransfer?.priceFrom =null;


            }
        }
    }

    fun onCheckedFree(boolean: Boolean){
        filterTransfer?.free = boolean
    }
    fun onCheckedAgreement(boolean: Boolean){
        filterTransfer?.priceAgree = boolean
    }


    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        binding.constraintBtnPrice.isVisible = true;
        binding.constraintSpinner.isVisible = true;

        filterTransfer?.typePrice = ContextApp.RENT
        ButtonSwitch(this@FilterActivity).btnSwiche(
            binding.tr,binding.tS,binding.tService,
            binding.lineBottomRent,binding.lineBottomSale,binding.lineBottomService,
            binding.img,binding.imgSale, binding.imgService,1
        )
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {
        binding.constraintBtnPrice.isVisible = true;
        binding.constraintSpinner.isVisible = false;

        filterTransfer?.typePrice = ContextApp.SALE
        ButtonSwitch(this@FilterActivity).btnSwiche(
            binding.tS,binding.tr,binding.tService,
            binding.lineBottomSale,binding.lineBottomRent,binding.lineBottomService,
            binding.imgSale,binding.img, binding.imgService,2
        )
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {
        binding.constraintBtnPrice.isVisible = true;
        binding.constraintSpinner.isVisible = false;

        filterTransfer?.typePrice = ContextApp.SERVICE
        ButtonSwitch(this@FilterActivity).btnSwiche(
            binding.tService,binding.tr,binding.tS,
            binding.lineBottomService,binding.lineBottomRent,binding.lineBottomSale,
            binding.imgService,binding.img, binding.imgSale,3
        )
    }

    /* OnClick */
    open fun onClickCategory(view: View) {
//        binding.consErrorCategory.visibility = View.GONE
        var intent = Intent(this, CategoryActivity::class.java);
        intent.putExtra(ContextApp.PAGE,ContextApp.PAGE_FILTER);
        intent.putExtra(ContextApp.TYPE_MARKET,filterTransfer?.typePrice);
        activityResultLauncherAddMarket.launch(intent)
//        activityResultLauncherAddMarket.launch(Intent(this, CategoryActivityOld::class.java))
    }
    var isHome = false;
    var isCar = false;
    /**Loucher Add Market **/
    var activityResultLauncherAddMarket =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {

                }
                1 -> {
                    binding?.hintCategory.isVisible = true;

                    binding.labelCategory.text = result.data?.getStringExtra(ContextApp.CATEGORY_NAME);
                    filterTransfer?.categoryName = result.data?.getStringExtra(ContextApp.CATEGORY_NAME);

                    binding.linearAdvancedHome.isVisible = false
                    filterTransfer?.typeAdvanced = result.data?.getStringExtra(ContextApp.TYPE_CATEGORY)
                    filterTransfer?.categoryID = result.data?.getStringExtra(ContextApp.CATEGORY_ID)
                    filterTransfer?.subCategoryID = result.data?.getStringExtra(ContextApp.SUB_CATEGORY_ID)

                    isHome = result.data?.getBooleanExtra(ContextApp.IS_HOME, false) ?: false;
                    isCar = result.data?.getBooleanExtra(ContextApp.IS_CAR, false) ?: false;

                    Log.i("testFilter", "" +
                            "${filterTransfer?.typeAdvanced} \r\n" +
                            "${filterTransfer?.categoryID} \n" +
                            "${isHome} \n" +
                            "${isCar} \n" +
                            "${result.data?.getStringExtra(ContextApp.CATEGORY_NAME)}" +
                            " ")

                    if (isHome && page != ContextApp.LISEENING){
                        binding.dropDownAdvanced.isVisible = true;
                        binding.constraintKilometerCar.isVisible = false;
                        binding.constraintMetrazh.isVisible = true;
                        binding.constraintYearHome.isVisible = true;
                    }else if (isCar && page != ContextApp.LISEENING){

                        binding.dropDownAdvanced.isVisible = true
                        binding.constraintKilometerCar.isVisible = true
                        binding.constraintMetrazh.isVisible = false
                        binding.constraintYearHome.isVisible = false
                    }else{
                        binding.dropDownAdvanced.isVisible = false
                        binding.constraintKilometerCar.isVisible = false

                    }

                }
                ContextApp.FINISH -> {
                    finish()
                }
            }
        }

    /*OnClick*/
    fun onClickSubmit(view: View) {
        var result = Intent(this@FilterActivity,FilterActivity::class.java);
//        var filterTransfer = FilterTransfer(
//            title = "",
//            cityID = cityID?:"",
//            categoryID = categoryID,
//            type = "Sale",
//            priceFrom = 200000,
//            priceTo = 500000,
//            priceAgree = false,
//            free = false,
//            registerDate = null,
//            iP = GettingIP(this).deviceIpAddress?:"ip"
//        );
        Log.i("testFilter",  "FilterActivity :  \r\n title ${filterTransfer?.title} " +
                "\n cityId ${filterTransfer?.cityID}" +
                "\n category ${filterTransfer?.categoryID} " +
                "\n type ${filterTransfer?.typePrice ?: ""}         " +
                "\n priceFrom ${filterTransfer?.priceFrom ?: null}" +
                "\n priceTO ${filterTransfer?.priceTo ?: null}" +
                "\n agree  ${filterTransfer?.priceAgree ?: false}" +
                "\n free ${filterTransfer?.free ?: false}" +
                "\n date  ${filterTransfer?.registerDate ?: null}" +
                "\n advanced  ${filterTransfer?.typeAdvanced ?: null}" +
                "\n ip ${filterTransfer?.iP}"
        )
        if (page == ContextApp.LISEENING) {

            if (filterTransfer?.categoryID.isNullOrEmpty()){
                Toast.makeText(this@FilterActivity , getString(R.string.choose_category) ,Toast.LENGTH_SHORT ).show()
                return
            }
            if (filterTransfer?.cityID.isNullOrEmpty()){
                Toast.makeText(this@FilterActivity , getString(R.string.choose_city) ,Toast.LENGTH_SHORT ).show()
                return
            }

        }

            result.putExtra("result",filterTransfer)
        setResult(1,result)
        finish()
    }
    /*OnClick*/
    fun onClickCity(view: View) {
        var showAreaDialog = ShowAreaDialog();
        showAreaDialog?.onReturnPlace = this;
        showAreaDialog.show(supportFragmentManager,"area")
    }

    /*onClick Parking */
    fun onClickParking(view: View) {
        var realValue = SelectorFragDialog(this).newInstance(ContextApp.PARKING);
        realValue.show(supportFragmentManager, "parking");
    }
    override fun returnContentSelector(type: String?, content: String?, position: Int?) {
        if (type == ContextApp.PARKING) {
            binding.strokeParking.visibility = View.INVISIBLE;
            binding.tParking.text = content;
            filterTransfer?.parkingTypeID =  (position!! + 1).toString();
        }
    }


    override fun onReturnPlace(placeTO: PlaceTO) {
        binding?.hintArea?.isVisible = true;
        binding?.labelCity?.text = placeTO?.name;
        city = placeTO.name;
//                cityID = placeTO.id;
        filterTransfer?.cityID =placeTO.id!!;
    }

    override fun returnBadRooms(badRooms: Int?) {
        filterTransfer?.rooms = badRooms?:0;

    }

    override fun returnBathRooms(bathRooms: Int?) {
        filterTransfer?.bathrooms = bathRooms?:0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMessage(event: MessageEvent) {
//        Toast.makeText(this@FilterActivity,event.name,Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}