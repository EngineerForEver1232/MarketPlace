package com.pedpo.pedporent.view.addMarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.transition.TransitionManager
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityGeneralAddmarketBinding
import com.pedpo.pedporent.listener.*
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.model.market.editMarket.EditMarketTO
import com.pedpo.pedporent.model.transfer.TransferDataMarket
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.dialog.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class GeneralSpecificationActivity : AppCompatActivity(), ReturnContentInt, ReturnPrice,
    ReturnContent, ReturnContentSelector , ReturnYear{

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    lateinit var binding: ActivityGeneralAddmarketBinding
    private var market: EditMarketTO? = null;
    var typeOfguaranteeTO = TypeOfguaranteeTO();

    @Inject
    lateinit var prefApp: PrefApp

    //    var categoryBase: String? = null;
    var isHome = false;
    var isCar = false;

    var typeMarket: String? = null;
    var validation = true;

    internal var transferDataMarket = TransferDataMarket();

    var listTypeShowPrice = arrayListOf(ContextApp.Y,ContextApp.M, ContextApp.D,ContextApp.H);

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralAddmarketBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root);

        val adapterFilter = ArrayAdapter.createFromResource(this , R.array.type_price , R.layout.spinner_item_renteral)
        binding.spinnerTypePrice.adapter = adapterFilter ;

        binding.spinnerTypePrice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                transferDataMarket.showType = listTypeShowPrice[position]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET) ?: ""

        binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT ;
        binding.linearPriceRent.isVisible = typeMarket == ContextApp.RENT ;

        if (prefApp.getNameCountry() != "Iran" && prefApp.getNameCountry() != "ایران")
            binding.layoutDeposit.isVisible = false ;


        when (typeMarket) {
            ContextApp.RENT -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.rent)} - ${getString(R.string.ad_registration)}"
            ContextApp.SALE -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.sell)} - ${getString(R.string.ad_registration)}"
            else -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.service)} - ${getString(R.string.ad_registration)}"
        }

        typeOfguaranteeTO =
            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
                ?: TypeOfguaranteeTO();

        transferDataMarket.categoryName = intent?.getStringExtra(ContextApp.CATEGORY_NAME) ?: "";
        transferDataMarket.categoryID = intent?.getStringExtra(ContextApp.CATEGORY_ID) ?: "";
        transferDataMarket.subCategroyID =
            intent?.getStringExtra(ContextApp.SUB_CATEGORY_ID) ?: "";
        binding.labelCategory.text = transferDataMarket.categoryName;

        Log.i(
            "testAddMarkt", "onSuccess 1 : " +
                    "${transferDataMarket.categoryID} \r\t " +
                    "${transferDataMarket.categoryName}"
        )



        isHome = intent?.getBooleanExtra(ContextApp.IS_HOME, false) ?: false;
        binding.linearHouse.isVisible = isHome == true && typeMarket != ContextApp.SERVICE
//        binding.lineHouse.isVisible = isHome == true && typeMarket != ContextApp.SERVICE

        isCar = intent?.getBooleanExtra(ContextApp.IS_CAR, false) ?: false;
        binding.linearCar.isVisible = isCar == true && typeMarket != ContextApp.SERVICE
        binding.lineCar.isVisible = isCar == true && typeMarket != ContextApp.SERVICE

//        binding.groupLand.visibility = View.GONE;

        onClickAppBar()

        binding.constraintArea.setOnClickListener {
            val intent = Intent(this, LocationChooseActivity::class.java);
            intent.putExtra(ContextApp.MARKET, market);
            intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

            activityResultLauncherGeneral.launch(intent)
        }
    }


    /*OnClick*/
    private fun onClickAppBar() {
        binding.actionBar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.actionBar.icClose.setOnClickListener {
            setResult(ContextApp.FINISH)
            finish()
        }
    }

    open fun onClickBtnConfirm(view: View) {

        if (!validation())
            return

        val intent = Intent(this@GeneralSpecificationActivity, AddMarketActivity::class.java)

        intent.putExtra(ContextApp.ASSIGNMENT, transferDataMarket.assignment)
        intent.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
        intent.putExtra(ContextApp.TRANFER_DATA_MARKET, transferDataMarket)
        intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

        activityResultLauncherGeneral.launch(intent)

    }

    open fun validation(): Boolean {

        validation = true;

        if (transferDataMarket.cityID.isNullOrEmpty()) {
            showError(binding.constraintArea, binding.hintArea , getString(R.string.enter_the_area))
            validation = false;
        }
        if (transferDataMarket.assignment == ContextApp.AN_AGREEMENT || transferDataMarket.assignment == ContextApp.FREE) {

        } else {

            if (typeMarket == ContextApp.SALE) {
                if (transferDataMarket.salePrice.toString().isEmpty()) {
                    showError(binding.constraintRealValue, binding.hintRealValue , getString(R.string.enter_the_price))
                    validation = false;
                }

            } else if (typeMarket == ContextApp.SERVICE) {
                if (transferDataMarket.commodityPrice.isNullOrEmpty()
                ) {
                    showError(binding.constraintRealValue, binding.hintRealValue,getString(R.string.enter_the_price))
                    validation = false;

                }
            } else {
                if (transferDataMarket.showType.isNullOrEmpty()){
                    validation = false;
                }else{
                    when(transferDataMarket.showType){
                        "Y" ->{
                            validation = transferDataMarket.yearlyRentPrice != 0L ;
                            if (transferDataMarket.yearlyRentPrice == 0L)
                                showError(constraint = binding.constraintYearInner, hint = binding.hintYearPrice,getString(R.string.enter_the_price)) ;
                            else
                                fixError(binding.constraintYearInner, binding.hintYearPrice, getString(R.string.year_price))

                            fixError(binding.constraintMonthInner, binding.hintMonthlyPRice, getString(R.string.monthly_price))
                            fixError(binding.constraintDaily, binding.hintDaily, getString(R.string.daily_rental_price))
                            fixError(binding.constraintHourInner, binding.hintHourPrice, getString(R.string.hour_price))

                        }
                        "M"->{
                            validation = transferDataMarket.monthlyRentalPrice != 0L;
                            if (transferDataMarket.monthlyRentalPrice == 0L)
                                showError(constraint = binding.constraintMonthInner, hint = binding.hintMonthlyPRice,getString(R.string.enter_the_price)) ;
                            else
                                fixError(binding.constraintMonthInner, binding.hintMonthlyPRice, getString(R.string.monthly_price))

                            fixError(binding.constraintYearInner, binding.hintYearPrice, getString(R.string.year_price))
                            fixError(binding.constraintDaily, binding.hintDaily, getString(R.string.daily_rental_price))
                            fixError(binding.constraintHourInner, binding.hintHourPrice, getString(R.string.hour_price))
                        }
                        "D"->{
                            validation = !transferDataMarket.priceDayli.isNullOrEmpty();
                            if (transferDataMarket.priceDayli.isNullOrEmpty())
                                showError(constraint = binding.constraintDaily, hint = binding.hintDaily,getString(R.string.enter_the_price)) ;
                            else
                                fixError(binding.constraintDaily, binding.hintDaily, getString(R.string.daily_rental_price))

                            fixError(binding.constraintYearInner, binding.hintYearPrice, getString(R.string.year_price))
                            fixError(binding.constraintMonthInner, binding.hintMonthlyPRice, getString(R.string.monthly_price))
                            fixError(binding.constraintHourInner, binding.hintHourPrice, getString(R.string.hour_price))
                        }
                        "H"->{
                            validation = transferDataMarket.hourlyRentalPrice != 0L;
                            if (transferDataMarket.hourlyRentalPrice == 0L)
                                showError(constraint = binding.constraintHourInner, hint = binding.hintHourPrice,getString(R.string.enter_the_price));
                            else
                                fixError(binding.constraintHourInner, binding.hintHourPrice, getString(R.string.hour_price))

                            fixError(binding.constraintYearInner, binding.hintYearPrice, getString(R.string.year_price))
                            fixError(binding.constraintMonthInner, binding.hintMonthlyPRice, getString(R.string.monthly_price))
                            fixError(binding.constraintDaily, binding.hintDaily, getString(R.string.daily_rental_price))
                        }
                    }
                }
//                if (
////                    transferDataMarket.commodityPrice.isNullOrEmpty() &&
//                    transferDataMarket.priceDayli.isNullOrEmpty() &&
//                    transferDataMarket.hourlyRentalPrice == 0L &&
//                    transferDataMarket.monthlyRentalPrice == 0L &&
//                    transferDataMarket.yearlyRentPrice == 0L
//                ) {
//                    binding.tPriceError.isVisible = true;
//                    validation = false;
//
//                }
            }
        }


        if (isHome && typeMarket != ContextApp.SERVICE) {

            if (transferDataMarket.meterOfHouse == 0) {
                showError(constraint = binding.constraintMetrazh, hint = binding.hintMetrazh,getString(R.string.meter_of_house))
                validation = false;
            }
            if (transferDataMarket.rooms == 0) {
                showError(constraint = binding.constraintRoom, hint = binding.hintBadroom , getString(R.string.total_rooms) )
                validation = false;
            }
            if (transferDataMarket.bathrooms == 0) {
                showError(constraint = binding.constraintbadRoom, hint = binding.hintBathroom,getString(
                                    R.string.total_bath))
                validation = false;
            }
            if (transferDataMarket.yearOfHouse.isNullOrEmpty()) {
                showError(
                    constraint = binding.constraintYearConstruction ,
                    hint = binding.hintYearHome,
                    textError = getString(R.string.year_construction)
                )
                validation = false;
            }
            if (transferDataMarket.parkingID.isNullOrEmpty()) {
                showError(constraint = binding.constraintParking, hint = binding.hintParking , textError = getString(R.string.type_parking))
                validation = false;
            }
            if (transferDataMarket.heatingID.isNullOrEmpty()) {
                showError(constraint = binding.constraintHeating, hint = binding.hintHeating , textError = getString(R.string.enter_type_heating))
                validation = false;
            }
            if (transferDataMarket.coolingID.isNullOrEmpty()) {
                showError(constraint = binding.constraintCooling, hint = binding.hintCooling, textError = getString(R.string.enter_type_cooling))
                validation = false;
            }

//            if (boolHouse)
//                return;

        } else if (isCar) {
            if (transferDataMarket.kilometerOfCar == 0) {
                showError(constraint = binding.constraintKm, hint = binding.hintKM, textError = getString(R.string.enter_km))
                validation = false;
            }
            if (transferDataMarket.fuelType.isNullOrEmpty()) {
                showError(constraint = binding.constraintTypeFuel, hint = binding.hintFuelType , textError = getString(R.string.enter_type_fuel))
                validation = false;
            }
            if (transferDataMarket.yearOfCar.isNullOrEmpty()) {
                showError(
                    constraint = binding.constraintYearConstructionCar,
                    hint = binding.hintYearConstrCar,
                    textError = getString(R.string.year_constra_car)
                )
                validation = false;
            }
        }

        if (validation)
            if (transferDataMarket.assignment == ContextApp.AN_AGREEMENT || transferDataMarket.assignment == ContextApp.FREE) {
//                clearValidationPrice()
            }

        return validation
    }

    fun showError(constraint: ConstraintLayout, hint: MaterialTextView?,textError:String) {
        constraint.setBackgroundResource(R.drawable.border_stroke_error)

        if (hint!=null) {
            scrollToView(binding.nestedScroll, hint)
            hint.isVisible = true
            hint.setText(R.string.enter_this_item_with_star)
            hint.setText("* $textError *")
            hint.setTextColor(
                ContextCompat.getColor(
                    this@GeneralSpecificationActivity,
                    R.color.red_error
                )
            )
        }

    }


    fun fixError(constraint: ConstraintLayout, hint: MaterialTextView, text: String) {
        constraint.setBackgroundResource(R.drawable.border_stroke)

        hint.setTextColor(
            ContextCompat.getColor(
                this@GeneralSpecificationActivity,
                R.color.gray_standard
            )
        )
        hint.text = text
        hint.isVisible = false
    }

    fun scrollToView(scrollView: NestedScrollView, view: View) {
        view.requestFocus()
        val scrollBounds = Rect()
        scrollView.getHitRect(scrollBounds)
        if (!view.getLocalVisibleRect(scrollBounds)) {
            Handler().post {
                val toScroll = getRelativeTop(view) - getRelativeTop(scrollView)
                (scrollView as NestedScrollView).smoothScrollTo(0, toScroll - 120)
            }
        }
    }

    fun getRelativeTop(myView: View): Int {
        return if (myView.parent === myView.rootView) myView.top else myView.top + getRelativeTop(
            myView.parent as View
        )
    }


    internal var activityResultLauncherGeneral =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                city = result.data?.getStringExtra(ContextApp.CITY)
//                cityID = result.data?.getStringExtra(ContextApp.CITY_ID);
                transferDataMarket.cityName = result.data?.getStringExtra(ContextApp.CITY);
                transferDataMarket.cityID = result.data?.getStringExtra(ContextApp.CITY_ID);

                binding.labelCity.text = result.data?.getStringExtra(ContextApp.CITY)

                binding.hintArea.isVisible = true;

                if (prefApp.getNameCountry() == "Iran" || prefApp.getNameCountry() == "ایران") {
                    binding.layoutDeposit.isVisible =
                        !(typeMarket == ContextApp.SERVICE || typeMarket == ContextApp.SALE)
                } else {
                    binding.layoutDeposit.isVisible = false
                }

//                binding.layoutDeposit.isVisible =
//                    prefApp.getNameCountry() == "Iran" || prefApp.getNameCountry() == "ایران";
//                binding.layoutDeposit.isVisible = typeMarket != ContextApp.SERVICE


                fixError(
                    binding.constraintArea,
                    binding.hintArea,
                    getString(R.string.choose_location)
                )
//                lat = result.data?.getStringExtra(ContextApp.LATITUDE)
//                lng = result.data?.getStringExtra(ContextApp.LONGITUDE)
                transferDataMarket.latitude = result.data?.getStringExtra(ContextApp.LATITUDE)
                transferDataMarket.longitude = result.data?.getStringExtra(ContextApp.LONGITUDE)

            } else if (result.resultCode == ContextApp.FINISH) {
                setResult(ContextApp.FINISH)
                finish()
            }
        }


    override fun returnContent(
        content: String?,
        textOther: String?,
        typeOfguaranteeTO: TypeOfguaranteeTO?
    ) {


        typeOfguaranteeTO?.otherText = textOther;
        this.typeOfguaranteeTO = typeOfguaranteeTO!!;
        binding.selectCessionType.visibility = View.INVISIBLE

        binding.spinnerRenteral.visibility = View.VISIBLE
        binding.lineStrokeDeposit.visibility = View.INVISIBLE


        var arr = arrayListOf<String>()
        if (typeOfguaranteeTO.nationalCard == true) {
            arr.add(getString(R.string.national_id_card))
        }
        if (typeOfguaranteeTO.identityCard == true) {
            arr.add(getString(R.string.certificate))
        }
        if (typeOfguaranteeTO.promissoryNote == true) {
            arr.add(getString(R.string.promissory_note))
        }
        if (typeOfguaranteeTO.check == true) {
            arr.add(getString(R.string.check))
        }
        if (typeOfguaranteeTO.other == true) {
            arr.add(typeOfguaranteeTO.otherText.toString())
        }

//        var adapterFilter = ArrayAdapter.createFromResource(this,arr.toArray().size,R.layout.spinner_item_renteral)
        var adapterFilter =
            ArrayAdapter(this@GeneralSpecificationActivity, R.layout.spinner_item_renteral, arr)
        binding.spinnerRenteral.adapter = adapterFilter;





        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.nationalCard}")
        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.identityCard}")
        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.promissoryNote}")
        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.check}")
        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.other}")
        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.otherText}")

    }


    /**override Return Price Dayli*/
    override fun returnPriceDayli(priceDayli: String?) {
        if (priceDayli?.isNullOrEmpty() == true) {

            binding.tDaily.text = "";
            transferDataMarket.priceDayli = ""
            binding.hintDaily.isVisible = false

        } else {
            binding.tPriceError.isVisible = false;
            binding.tDaily.text = priceDayli;
            transferDataMarket.priceDayli = numberFormatDigits.clearNumber(priceDayli)
            binding.hintDaily.isVisible = true
        }

    }

    @Inject
    lateinit var numberFormatDigits: NumberFormatDigits

    /**override Return Price Real*/
    override fun returnPriceReal(priceRealy: String?) {
        if (priceRealy?.isNullOrEmpty() == true) {
            binding.tReal.text = "";
            transferDataMarket.salePrice = 0
            transferDataMarket.commodityPrice = "";
            binding.hintRealValue.isVisible = false
            return
        }

        binding.tReal.text = priceRealy;

//        fixError(binding.constraintRealValue,binding.hintRealValue,getString(R.string.the_real_value_of_the_goods))

        binding.hintRealValue.isVisible = true
        if (typeMarket == ContextApp.SALE)
            transferDataMarket.salePrice =
                if (priceRealy.isNullOrEmpty()) 0 else numberFormatDigits.clearNumber(priceRealy)
                    ?.toLong();
        else
            transferDataMarket.commodityPrice = numberFormatDigits.clearNumber(priceRealy);


    }

    /**override Return Price Hour */
    override fun returnPriceHour(priceHour: String?) {

        if (priceHour?.isNullOrEmpty() == true) {
            binding.tHours.text = "";
            transferDataMarket.hourlyRentalPrice = 0L;

            binding.hintHourPrice.isVisible = true;

        } else {

            binding.tPriceError.isVisible = false;

            binding.tHours.text = priceHour;
            transferDataMarket.hourlyRentalPrice =
                numberFormatDigits.clearNumber(priceHour)?.toLong();

            binding.hintHourPrice.isVisible = true;
        }


    }

    /**override Return Price Month */
    override fun returnPriceMonth(priceMonth: String?) {
        if (priceMonth?.isNullOrEmpty() == true) {
            binding.tPriceMonth.text = "";
            binding.hintMonthlyPRice.isVisible = false;
            transferDataMarket.monthlyRentalPrice = 0;

        } else {
            binding.tPriceError.isVisible = false;
            binding.tPriceMonth.text = priceMonth;
            transferDataMarket.monthlyRentalPrice =
                numberFormatDigits.clearNumber(priceMonth)?.toLong();
            binding.hintMonthlyPRice.isVisible = true;
        }
    }


    /**override Return Price Year */
    override fun returnPriceYear(priceYear: String?) {
        if (priceYear?.isNullOrEmpty() == true) {
            binding.tPriceYear.text = "";
            transferDataMarket.yearlyRentPrice = 0L
            binding.hintYearPrice.isVisible = false;

        } else {
            fixError(binding.constraintYearInner, binding.hintYearPrice, getString(R.string.year_price))
            binding.tPriceError.isVisible = false;
            binding.tPriceYear.text = priceYear;
            transferDataMarket.yearlyRentPrice = numberFormatDigits.clearNumber(priceYear)?.toLong();
            binding.hintYearPrice.isVisible = true;

        }
    }

    /**override Return metrazh */
    override fun returnMetrazhHome(metrazh: String?) {
//        setFixError(binding.constraintResultMetrazh)
//        binding.selectMetrazh.text = metrazh;
        fixError(
            binding.constraintMetrazh,
            binding.hintMetrazh,
            getString(R.string.meter_of_house)
        )

        binding.tMetrazh.text = metrazh;
        transferDataMarket.meterOfHouse = metrazh?.toInt();
        binding.hintMetrazh.isVisible = true;
    }

    /**override Return room */
    override fun returnRoom(roomNumber: String?) {
        fixError(binding.constraintRoom, binding.hintBadroom, getString(R.string.badroom))

        binding.tBadRoom.text = roomNumber;
        transferDataMarket.rooms = roomNumber?.toInt();
        binding.hintBadroom.isVisible = true;

    }

    /**override Return badRoom */
    override fun returnBadRoom(badRoomNumber: String?) {
//        setFixError(binding.constraintResulbadRoom)
        fixError(binding.constraintbadRoom, binding.hintBathroom, getString(R.string.bathroom))

        binding.tBathRoom.text = badRoomNumber;
        transferDataMarket.bathrooms = badRoomNumber?.toInt();

        binding.hintBathroom.isVisible = true;

    }

    /**override Return YearConstruction */
    override fun returnYearConstruction(yearConstruction: String?) {
//        setFixError(binding.constraintResulYearConstruction)
//        binding.tBuildYear.text = yearConstruction;
        fixError(
            binding.constraintYearConstruction,
            binding.hintYearHome,
            getString(R.string.year_construction)
        )
        binding.tYearHome.text = yearConstruction;
        transferDataMarket.yearOfHouse = yearConstruction;
        binding.hintYearHome.isVisible = true;

    }

    /**override Return Km */
    override fun returnKM(km: String?) {
//        setFixError(binding.constraintResultKm)
//        binding.realKm.text = km;
        fixError(binding.constraintKm, binding.hintKM, getString(R.string.kilometer_car))

        binding.tKm.text = km;
        transferDataMarket.kilometerOfCar = km?.toInt();
        binding.hintKM.isVisible = true;

    }

    /**override Return TypeFuel */
    override fun returnTypeFuel(typeFuel: String?) {
//        setFixError(binding.constraintResultTypeFuel)
//        binding.tTypeFuel.text = typeFuel;
        fixError(binding.constraintTypeFuel, binding.hintFuelType, getString(R.string.fuel_type))
        binding.tFule.text = typeFuel;
        transferDataMarket.fuelType = typeFuel;
        binding.hintFuelType.isVisible = true;

    }

    /**override Return YearConstructionCar */
    override fun returnYearConstructionCar(yearConstructionCar: String?) {
//        setFixError(binding.constraintResulYearConstructionCar)
//        binding.tYearBuildCar.text = yearConstructionCar;
        fixError(
            binding.constraintYearConstructionCar,
            binding.hintYearConstrCar,
            getString(R.string.year_constra_car)
        )
        binding.tYearCar.text = yearConstructionCar;
        transferDataMarket.yearOfCar = yearConstructionCar;
        binding.hintYearConstrCar.isVisible = true;

    }


    /**override Return type price */
    override fun returnContent(content: String?) {
//        animateToKeyframeTwo()
        binding.hintAssignment.isVisible = true;
        when (content) {
            ContextApp.AN_AGREEMENT -> {
                animateToKeyframeTwo()
                binding.tAssignment.text = getString(R.string.an_agreement);

                binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT
                binding.linearPrice.isVisible = false;

                transferDataMarket.free = false;
                transferDataMarket.priceAgree = true;

            }
            ContextApp.FREE -> {
                animateToKeyframeTwo()

                binding.tAssignment.text = getString(R.string.free);
                binding.layoutDeposit.isVisible = false

                binding.linearPrice.isVisible = false;


                transferDataMarket.free = true;
                transferDataMarket.priceAgree = false;

            }
            else -> {

                binding.tAssignment.text = getString(R.string.price2);

                binding.linearPrice.isVisible = true;

                if (prefApp.getNameCountry() != "Iran" && prefApp.getNameCountry() != "ایران")
                    binding.layoutDeposit.isVisible = false;
                else
                    binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT;

                animateToKeyframeTwo()


                transferDataMarket.free = false;
                transferDataMarket.priceAgree = false;

            }
        }

        transferDataMarket.assignment = content;

    }

    fun clearValidationPrice() {
        transferDataMarket.priceDayli = ""
        transferDataMarket.commodityPrice = ""
        transferDataMarket.hourlyRentalPrice = 0
        transferDataMarket.monthlyRentalPrice = 0
        transferDataMarket.yearlyRentPrice = 0

    }

    fun animateToKeyframeTwo() {
        val constraintSet = ConstraintSet()
//        constraintSet.load(this, R.layout.activity_language_country)
        TransitionManager.beginDelayedTransition(binding.constraintBase)
        constraintSet.applyTo(binding.constraintBase)
    }

    /**override Return selector */
    override fun returnContentSelector(type: String?, content: String?, position: Int?) {
        when (type) {
            ContextApp.PARKING -> {
                fixError(
                    binding.constraintParking,
                    binding.hintParking,
                    getString(R.string.parking)
                )
                transferDataMarket.parkingID = (position!! + 1).toString();

                binding.tParkingHome.text = content;
                binding.hintParking.isVisible = true;

            }
            ContextApp.HEATING -> {
                fixError(
                    binding.constraintHeating,
                    binding.hintHeating,
                    getString(R.string.heating)
                )
                transferDataMarket.heatingID = (position!! + 1).toString()

                binding.tHeatingH.text = content;
                binding.hintHeating.isVisible = true;

            }
            ContextApp.COOLING -> {
                fixError(
                    binding.constraintCooling,
                    binding.hintCooling,
                    getString(R.string.cooling)
                )
                transferDataMarket.coolingID = (position!! + 1).toString()

                binding.tCoolingH.text = content;
                binding.hintCooling.isVisible = true;

            }
        }

    }


    /*onClick Deposit*/
    fun onClickDeposit(view: View) {
        val dialogDeposit =
            SecurityDepositFragDialog().newInstance(typeOfguaranteeTO = typeOfguaranteeTO);
        dialogDeposit.returnContentInt = this;
        dialogDeposit.show(supportFragmentManager, "deposit")
    }

    /*onClick RealValue*/
    fun onClickRealValue(view: View) {

        val realValue = RealValueFragDialog().newInstance(
            ContextApp.REAL_VALUE,
            if (typeMarket == ContextApp.SALE) {
                if (transferDataMarket.salePrice == null) {
                    null
                } else
                    transferDataMarket.salePrice.toString() ?: null
            } else
                transferDataMarket.commodityPrice ?: ""

        )

        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "realValue");
    }

    /*onClick Daily*/
    fun onClickDaily(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.PRICE_DAILY,
            transferDataMarket.priceDayli ?: ""
        );

        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "price");
    }


    /*onClick onClickHour */
    fun onClickHour(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.PRICE_HOUR,
            transferDataMarket.hourlyRentalPrice.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "priceHour");
    }

    /*onClick onClickMonth */
    fun onClickMonth(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.PRICE_MONTH,
            transferDataMarket.monthlyRentalPrice.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "priceMonth");
    }

    /*onClick onClickYear */
    fun onClickYear(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.PRICE_YEAR,
            transferDataMarket.yearlyRentPrice.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "priceYear");
    }

    /*onClick METRAZH_HOME */
    fun onClickMetrazhHome(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.METRAZH_HOME,
            transferDataMarket.meterOfHouse.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "perazh");
    }

    /*onClick Room */
    fun onClickRoom(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.ROOM,
            transferDataMarket.rooms.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "room");
    }

    /*onClick BadRoom */
    fun onClickBadRoom(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.BAD_ROOM,
            transferDataMarket.bathrooms.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "onClickBadRoom");
    }

    /*onClick yearConstruction */
    fun onClickyearConstruction(view: View) {

        val yearDialog = YearDialog().newInstance(ContextApp.HOME);
        yearDialog.setYearReturn(this);
        yearDialog.show(supportFragmentManager,"year")

//        var realValue = RealValueFragDialog().newInstance(
//            ContextApp.YEAR_CONNSTRUCTION,
//            transferDataMarket?.yearOfHouse ?: ""
//        );
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "yearConstruction");
    }


    /*onClick Km */
    fun onClickKm(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.KM,
            transferDataMarket?.kilometerOfCar.toString()
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "km");
    }

    /*onClick TypeFuel */
    fun onClickTypeFuel(view: View) {
        val realValue = RealValueFragDialog().newInstance(
            ContextApp.TYPE_FUEL,
            transferDataMarket?.fuelType ?: ""
        );
        realValue.returnContent = this;
        realValue.show(supportFragmentManager, "typeFuel");
    }

    /*onClick YearConstructionCar */
    fun onClickYearConstructionCar(view: View) {

        val yearDialog = YearDialog().newInstance(ContextApp.Car);
        yearDialog.setYearReturn(this);
        yearDialog.show(supportFragmentManager,"year")

//        var realValue = RealValueFragDialog().newInstance(
//            ContextApp.YEAR_CONNSTRUCTION_CAR,
//            transferDataMarket?.yearOfCar ?: ""
//        );
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "yearConstructionCar");
    }

    /*onClick Parking */
    fun onClickParking(view: View) {
        val realValue = SelectorFragDialog(this).newInstance(ContextApp.PARKING);
        realValue.show(supportFragmentManager, "parking");
    }

    /*onClick Heating */
    fun onClickHeating(view: View) {
        val realValue = SelectorFragDialog(this).newInstance(ContextApp.HEATING);
        realValue.show(supportFragmentManager, "heating");
    }

    /*onClick Cooling */
    fun onClickCooling(view: View) {
        val realValue = SelectorFragDialog(this).newInstance(ContextApp.COOLING);
        realValue.show(supportFragmentManager, "cooling");
    }


    /*onClick Assignment*/
    fun onClickAssignment(view: View) {
        val dialogAssignment = AssignmentFragDialog()
        dialogAssignment.returnContent = this;
        dialogAssignment.show(supportFragmentManager, "Assignment")
    }

    override fun returnYear(type: String?,content: String?) {

        when(type){
            ContextApp.HOME ->{
                fixError(
                    binding.constraintYearConstruction,
                    binding.hintYearHome,
                    getString(R.string.year_construction)
                )
                binding.tYearHome.text = content;
                transferDataMarket.yearOfHouse = content;
                binding.hintYearHome.isVisible = true;
            }
            ContextApp.Car->{
                fixError(
                    binding.constraintYearConstructionCar,
                    binding.hintYearConstrCar,
                    getString(R.string.year_constra_car)
                )
                binding.tYearCar.text = content;
                transferDataMarket.yearOfCar = content;
                binding.hintYearConstrCar.isVisible = true;
            }
        }


    }


}