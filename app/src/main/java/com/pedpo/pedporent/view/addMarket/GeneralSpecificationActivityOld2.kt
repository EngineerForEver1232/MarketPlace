//package com.example.pedporent.view.addMarket
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.view.isVisible
//import com.example.pedporent.R
//import com.example.pedporent.databinding.ActivityGeneralAddmarketBinding
//import com.example.pedporent.listener.ReturnContent
//import com.example.pedporent.listener.ReturnContentInt
//import com.example.pedporent.listener.ReturnContentSelector
//import com.example.pedporent.listener.ReturnPrice
//import com.example.pedporent.model.market.TypeOfguaranteeTO
//import com.example.pedporent.model.market.editMarket.EditMarketTO
//import com.example.pedporent.model.transfer.TransferDataMarket
//import com.example.pedporent.utills.ContextApp
//import com.example.pedporent.utills.MyContextWrapper
//import com.example.pedporent.view.dialog.AssignmentFragDialog
//import com.example.pedporent.view.dialog.RealValueFragDialog
//import com.example.pedporent.view.dialog.SecurityDepositFragDialog
//import com.example.pedporent.view.dialog.SelectorFragDialog
//import com.google.android.material.textview.MaterialTextView
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class GeneralSpecificationActivityOld2 : AppCompatActivity(), ReturnContentInt, ReturnPrice,
//    ReturnContent, ReturnContentSelector {
//
//
//    lateinit var binding: ActivityGeneralAddmarketBinding
//    private var market: EditMarketTO? = null;
//    var typeOfguaranteeTO = TypeOfguaranteeTO();
//    var stringBuilder: StringBuilder? = null;
//
//    var categoryBase: String? = null;
//
////    private var priceDayli: String? = null;
////    private var realValuePrice: String? = null;
////    private var priceHour: String? = null;
////    private var priceMonth: String? = null;
////    private var priceYear: String? = null;
////
////    private var metrazh: String? = null;
////    private var roomNumber: String? = null;
////    private var badRoomNumber: String? = null;
////    private var yearConstruction: String? = null;
////
////    private var km: String? = null;
////    private var typeFuel: String? = null;
////    private var yearConstructionCar: String? = null;
//
//
//    private var transferDataMarket = TransferDataMarket();
//
////    var categoryMarketID: String? = null;
////    var subCategoryMarketID: String? = null;
////    private var resultCategory: String? = null;
//
//    //    private var city: String? = null;
////    private var cityID: String? = null;
////    private var lat: String? = null;
////    private var lng: String? = null;
//
////    private var assignment: String? = null;
//
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityGeneralAddmarketBinding.inflate(layoutInflater)
//        binding.listener = this;
//        setContentView(binding.root);
//
//
//        market = intent.getParcelableExtra<EditMarketTO>(ContextApp.MARKET);
//        typeOfguaranteeTO =
//            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
//                ?: TypeOfguaranteeTO();
//
//
//        if (market != null) {
//            binding.nameArea.text = market?.cityName ?: "Select";
////            cityID = market?.cityID;
//            transferDataMarket.city = market?.cityName;
//            transferDataMarket.cityID = market?.cityID;
//
////            lng = market?.longitude;
////            lat = market?.latitude;
//
//            transferDataMarket.lng = market?.longitude;
//            transferDataMarket.lat = market?.latitude;
////            typeOfguaranteeTO = market?.typeOfguaranteeTO!!
//
//
//            if (market?.free == true) {
//                binding.tAssignmentType.text = getString(R.string.free);
//            } else if (market?.priceAgree == true) {
//                binding.tAssignmentType.text = getString(R.string.an_agreement);
//            } else if (!market?.rentPrice.isNullOrEmpty()) {
//                binding.constraintResultDaily.isVisible = true;
//                binding.constraintResultRealValue.isVisible = true;
//
//                binding.constraintRealValue.isVisible = true;
//                binding.constraintDaily.isVisible = true;
//
//                binding.tAssignmentType.text = getString(R.string.price2);
//                binding.layoutDeposit.isVisible = true;
//                binding.constraintResultDeposit.isVisible = true;
//
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.nationalCard}")
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.identityCard}")
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.promissoryNote}")
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.check}")
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.other}")
//                Log.i("adMarketG", "returnContent: ${typeOfguaranteeTO.otherText}")
//
//                stringBuilder = StringBuilder();
//
//                if (typeOfguaranteeTO.nationalCard == true)
//                    stringBuilder?.append("${getString(R.string.national_id_card)}  , ")
//                if (typeOfguaranteeTO.identityCard == true)
//                    stringBuilder?.append(" ${getString(R.string.certificate)} , ")
//                if (typeOfguaranteeTO.promissoryNote == true)
//                    stringBuilder?.append(" ${getString(R.string.promissory_note)} , ")
//                if (typeOfguaranteeTO.check == true)
//                    stringBuilder?.append(" ${getString(R.string.check)} ")
//                if (typeOfguaranteeTO.other == true)
//                    stringBuilder?.append(typeOfguaranteeTO.otherText)
//
//                binding.tResultDeposit.text = stringBuilder?.toString()
//
//                transferDataMarket.priceDayli = market?.rentPrice ?: "";
//                transferDataMarket.realValuePrice = market?.commodityPrice ?: "";
//
//                binding.tResultDaily.text = market?.rentPrice ?: "";
//                binding.tResultRealValue.text = market?.commodityPrice ?: "";
//            }
//        }
//
//
//
//        transferDataMarket.categoryName = intent?.getStringExtra(ContextApp.CATEGORY_NAME) ?: "";
//        categoryBase = intent?.getStringExtra(ContextApp.TYPE_CATEGORY) ?: "";
//
//        transferDataMarket.categoryMarketID = intent?.getStringExtra(ContextApp.CATEGORY_ID) ?: "";
//
//        transferDataMarket.subCategoryMarketID =
//            intent?.getStringExtra(ContextApp.SUB_CATEGORY_ID) ?: "";
//
//
//        binding.linearHouse.isVisible = categoryBase == ContextApp.CATEGORY_HOUSE;
//        binding.lineHouse.isVisible = categoryBase == ContextApp.CATEGORY_HOUSE;
//
//        binding.linearCar.isVisible = categoryBase == ContextApp.CATEGORY_CAR;
//        binding.lineCar.isVisible = categoryBase == ContextApp.CATEGORY_CAR;
//
////        binding.nameCategory.text = resultCategory;
//        binding.nameCategory.text = transferDataMarket.categoryName;
//
//        onClickAppBar()
//
//        binding.constraintArea.setOnClickListener {
//            var intent = Intent(this, LocationChooseActivity::class.java);
//            intent.putExtra(ContextApp.MARKET, market);
//            activityResultLauncherGeneral.launch(intent)
//        }
//    }
//
//
//    /*OnClick*/
//    private fun onClickAppBar() {
//        binding.actionBar.imgBack.setOnClickListener {
//            onBackPressed()
//        }
//        binding.actionBar.icClose.setOnClickListener {
//            setResult(ContextApp.FINISH)
//            finish()
//        }
//    }
//
//
//    private var activityResultLauncherGeneral =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
////                city = result.data?.getStringExtra(ContextApp.CITY)
////                cityID = result.data?.getStringExtra(ContextApp.CITY_ID);
//                transferDataMarket.city = result.data?.getStringExtra(ContextApp.CITY)
//                transferDataMarket.cityID = result.data?.getStringExtra(ContextApp.CITY_ID);
//
//                binding.nameArea?.text = result.data?.getStringExtra(ContextApp.CITY)
////                lat = result.data?.getStringExtra(ContextApp.LATITUDE)
////                lng = result.data?.getStringExtra(ContextApp.LONGITUDE)
//                transferDataMarket.lat = result.data?.getStringExtra(ContextApp.LATITUDE)
//                transferDataMarket.lng = result.data?.getStringExtra(ContextApp.LONGITUDE)
//
//            } else if (result.resultCode == ContextApp.FINISH) {
//                setResult(ContextApp.FINISH)
//                finish()
//            }
//        }
//
//    fun setErrorValidation(constraint:ConstraintLayout){
//        constraint.isVisible = true;
//        constraint.setBackgroundResource(R.drawable.border_result_line_error)
//        constraint.requestFocus();
//    }
//    fun setFixError(constraint:ConstraintLayout){
//        constraint.isVisible = true;
//        constraint.setBackgroundResource(R.drawable.border_result_line)
//    }
//
//    fun onClickBtnConfirm(view: View) {
//
//        if (transferDataMarket.cityID.isNullOrEmpty()) {
//            Toast.makeText(this, "please Enter erea", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (transferDataMarket.assignment == ContextApp.AN_AGREEMENT || transferDataMarket.assignment == ContextApp.FREE) {
//        } else {
//
//            if (transferDataMarket.priceDayli.isNullOrEmpty() || transferDataMarket.realValuePrice.isNullOrEmpty()) {
//                Toast.makeText(this, "please Enter Price", Toast.LENGTH_SHORT).show()
//                return;
//            }
//        }
//
//
//        if (categoryBase == ContextApp.CATEGORY_HOUSE) {
//            var boolHouse = false;
//            if (binding.tResultRoom.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResultRoom)
////                binding.constraintResultRoom.isVisible = true;
////                binding.constraintResultRoom.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultRoom.requestFocus();
//                boolHouse = true;
//            }
//            if (binding.tResultbadRoom.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResulbadRoom)
////                binding.constraintResulbadRoom.isVisible = true;
////                binding.constraintResulbadRoom.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultbadRoom.requestFocus();
//                boolHouse = true;
//            }
//            if (binding.tResultYearConstruction.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResulYearConstruction)
////                binding.constraintResulYearConstruction.isVisible = true;
////                binding.constraintResulYearConstruction.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultYearConstruction.requestFocus();
//
//                boolHouse = true;
//            }
//            if (binding.tResultMetrazh.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResultMetrazh)
////                binding.constraintResultMetrazh.isVisible = true;
////                binding.constraintResultMetrazh.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultMetrazh.requestFocus();
//
//                boolHouse = true;
//            }
//            if (binding.tResultParking.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResulParking)
////                binding.constraintResulParking.isVisible = true;
////                binding.constraintResulParking.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultParking.requestFocus();
//
//                boolHouse = true;
//            }
//            if (binding.tResultCooling.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResulCooling)
////                binding.constraintResulCooling.isVisible = true;
////                binding.constraintResulCooling.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultCooling.requestFocus();
//
//                boolHouse = true;
//            }
//            if (binding.tResultHeating.text.isNullOrEmpty()) {
//                setErrorValidation(binding.constraintResulHeating)
////                binding.constraintResulHeating.isVisible = true;
////                binding.constraintResulHeating.setBackgroundResource(R.drawable.border_result_line_error)
////                binding.tResultHeating.requestFocus();
//                boolHouse = true;
//            }
//
//            if (boolHouse)
//                return;
//
//        } else if (categoryBase == ContextApp.CATEGORY_CAR) {
//
//        }
//
//
//        var intent = Intent()
////        intent.putExtra(ContextApp.CATEGORY, resultCategory)
////        intent.putExtra(ContextApp.CITY, city)
////        intent.putExtra(ContextApp.CITY_ID, cityID)
////        intent.putExtra(ContextApp.CATEGORY_ID, categoryMarketID)
////        intent.putExtra(ContextApp.SUB_CATEGORY_ID, subCategoryMarketID)
////        intent.putExtra(ContextApp.LATITUDE, lat)
////        intent.putExtra(ContextApp.LONGITUDE, lng)
////        intent.putExtra(ContextApp.PRICE_DAILY, priceDayli)
//
//
////        intent.putExtra(ContextApp.REAL_VALUE, realValuePrice)
//        intent.putExtra(ContextApp.ASSIGNMENT, transferDataMarket.assignment)
//        intent.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
//        intent.putExtra(ContextApp.TRANFER_DATA_MARKET, transferDataMarket)
//        setResult(ContextApp.RESULT_Ad, intent)
//        finish()
//    }
//
//
//    override fun returnContent(
//        content: String?,
//        textOther: String?,
//        typeOfguaranteeTO: TypeOfguaranteeTO?,
//    ) {
//        stringBuilder = StringBuilder();
//
//        typeOfguaranteeTO?.otherText = textOther;
//        this.typeOfguaranteeTO = typeOfguaranteeTO!!;
////        binding.selectCessionType.visibility = View.INVISIBLE
////        binding.lineStrokeDeposit.visibility = View.INVISIBLE
//        binding.constraintResultDeposit.visibility = View.VISIBLE
//
//
//
//        if (typeOfguaranteeTO.nationalCard == true)
//            stringBuilder?.append("${getString(R.string.national_id_card)}  , ")
//        if (typeOfguaranteeTO.identityCard == true)
//            stringBuilder?.append(" ${getString(R.string.certificate)} , ")
//        if (typeOfguaranteeTO.promissoryNote == true)
//            stringBuilder?.append(" ${getString(R.string.promissory_note)} , ")
//        if (typeOfguaranteeTO.check == true)
//            stringBuilder?.append(" ${getString(R.string.check)} ")
//        if (typeOfguaranteeTO.other == true)
//            stringBuilder?.append(typeOfguaranteeTO.otherText)
//
//        binding.tResultDeposit.text = stringBuilder?.toString()
//
//
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.nationalCard}")
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.identityCard}")
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.promissoryNote}")
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.check}")
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.other}")
//        Log.i("testPaging", "returnContent: ${typeOfguaranteeTO?.otherText}")
//
//    }
//
//
//    /**override Return Price Dayli*/
//    override fun returnPriceDayli(priceDayli: String?) {
//        binding.tResultDaily.text = priceDayli;
//        binding.constraintResultDaily.isVisible = true;
////        this.priceDayli = priceDayli;
//        transferDataMarket.priceDayli = priceDayli;
//    }
//
//    /**override Return Price Real*/
//    override fun returnPriceReal(priceRealy: String?) {
//        binding.tResultRealValue.text = priceRealy;
//        binding.constraintResultRealValue.isVisible = true;
////        this.realValuePrice = priceRealy;
//        transferDataMarket.realValuePrice = priceRealy;
//    }
//
//    /**override Return Price Hour */
//    override fun returnPriceHour(priceHour: String?) {
//        binding.tResultHour.text = priceHour;
//        binding.constraintResulHour.isVisible = true;
//
//        transferDataMarket.priceHour = priceHour;
//    }
//
//    /**override Return Price Month */
//    override fun returnPriceMonth(priceMonth: String?) {
//        binding.tResultMonth.text = priceMonth;
//        binding.constraintResulMonth.isVisible = true;
//
//        transferDataMarket.priceMonth = priceMonth;
//    }
//
//    /**override Return Price Year */
//    override fun returnPriceYear(priceYear: String?) {
//        binding.tResultYear.text = priceYear;
//        binding.constraintResulYear.isVisible = true;
//        transferDataMarket.priceYear = priceYear;
//    }
//
//    /**override Return metrazh */
//    override fun returnMetrazhHome(metrazh: String?) {
//        setFixError(binding.constraintResultMetrazh)
//        binding.tResultMetrazh.text = metrazh;
//        binding.constraintResultMetrazh.isVisible = true;
//        transferDataMarket.metrazh = metrazh;
//    }
//
//    /**override Return room */
//    override fun returnRoom(roomNumber: String?) {
//        setFixError(binding.constraintResultRoom)
//        binding.tResultRoom.text = roomNumber;
//        binding.constraintResultRoom.isVisible = true;
//        transferDataMarket.rooms = roomNumber;
//    }
//
//    /**override Return badRoom */
//    override fun returnBadRoom(badRoomNumber: String?) {
//        setFixError(binding.constraintResulbadRoom)
//        binding.tResultbadRoom.text = badRoomNumber;
//        binding.constraintResulbadRoom.isVisible = true;
//        transferDataMarket.bathrooms = badRoomNumber;
//    }
//
//    /**override Return YearConstruction */
//    override fun returnYearConstruction(yearConstruction: String?) {
//        setFixError(binding.constraintResulYearConstruction)
//        binding.tResultYearConstruction.text = yearConstruction;
//        binding.constraintResulYearConstruction.isVisible = true;
//        transferDataMarket.yearConstructionHouse = yearConstruction;
//    }
//
//    /**override Return Km */
//    override fun returnKM(km: String?) {
//        setFixError(binding.constraintResultKm)
//        binding.tResultKm.text = km;
//        binding.constraintResultKm.isVisible = true;
//        transferDataMarket.kilometerOfCar = km;
//    }
//
//    /**override Return TypeFuel */
//    override fun returnTypeFuel(typeFuel: String?) {
//        setFixError(binding.constraintResultTypeFuel)
//        binding.tResultTypeFuel.text = typeFuel;
//        binding.constraintResultTypeFuel.isVisible = true;
//        transferDataMarket.fuelType = typeFuel;
//    }
//
//    /**override Return YearConstructionCar */
//    override fun returnYearConstructionCar(yearConstructionCar: String?) {
//        setFixError(binding.constraintResulYearConstructionCar)
//        binding.tresultYearBuildCar.text = yearConstructionCar;
//        binding.constraintResulYearConstructionCar.isVisible = true;
//        transferDataMarket.yearConstructionCar = yearConstructionCar;
//    }
//
//
//
//    /**override Return type price */
//    override fun returnContent(content: String?) {
//        when (content) {
//            ContextApp.AN_AGREEMENT -> {
//                binding.tAssignmentType.text = getString(R.string.an_agreement);
//                binding.layoutDeposit.isVisible = true;
//
//                binding.linearPrice.isVisible = false;
//
////                this.priceDayli = "";
//                transferDataMarket.priceDayli = ""
//                transferDataMarket.realValuePrice = ""
////                this.realValuePrice = "";
//                binding.tResultDaily.text = "";
//                binding.tResultRealValue.text = "";
//
//            }
//            ContextApp.FREE -> {
//                binding.tAssignmentType.text = getString(R.string.free);
//                binding.layoutDeposit.isVisible = false
//                binding.constraintResultDeposit.isVisible = false
//
//                binding.linearPrice.isVisible = false;
//
//
//                binding.tResultDaily.text = "";
//                binding.tResultDeposit.text = "";
//                binding.tResultRealValue.text = "";
//
////                this.priceDayli = "";
//                transferDataMarket.priceDayli = ""
//                transferDataMarket.realValuePrice = ""
////                this.realValuePrice = "";
////                this.assignment
//            }
//            else -> {
//                binding.tAssignmentType.text = getString(R.string.price2);
//                binding.layoutDeposit.isVisible = true;
//
//                binding.linearPrice.isVisible = true;
//
//
//            }
//        }
//        transferDataMarket.assignment = content;
//    }
//
//    /**override Return selector */
//    override fun returnContentSelector(type: String?, content: String?, position: Int?) {
//        if (type == ContextApp.PARKING) {
//            setFixError(binding.constraintResulParking)
//
//            transferDataMarket.parkingTypeID = (position!! + 1).toString()
//
//            binding.tResultParking.text = content;
//            binding.constraintResulParking.isVisible = true;
//        } else if (type == ContextApp.HEATING) {
//            setFixError(binding.constraintResulHeating)
//
//            transferDataMarket.heatingID = (position!! + 1).toString()
//
//            binding.tResultHeating.text = content;
//            binding.constraintResulHeating.isVisible = true;
//
//        } else if (type == ContextApp.COOLING) {
//            setFixError(binding.constraintResulCooling)
//            transferDataMarket.coolingID = (position!! + 1).toString()
//
//            binding.tResultCooling.text = content;
//            binding.constraintResulCooling.isVisible = true;
//
//        }
//
//    }
//
//
//
//
//    /*onClick Deposit*/
//    fun onClickDeposit(view: View) {
//        var dialogDeposit = SecurityDepositFragDialog()
//        dialogDeposit.returnContentInt = this;
//        dialogDeposit.show(supportFragmentManager, "deposit")
//    }
//
//    /*onClick RealValue*/
//    fun onClickRealValue(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.REAL_VALUE)
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "realValue");
//    }
//
//    /*onClick Daily*/
//    fun onClickDaily(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.PRICE_DAILY);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "price");
//    }
//
//
//    /*onClick onClickHour */
//    fun onClickHour(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.PRICE_HOUR);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "priceHour");
//    }
//
//    /*onClick onClickMonth */
//    fun onClickMonth(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.PRICE_MONTH);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "priceMonth");
//    }
//
//    /*onClick onClickYear */
//    fun onClickYear(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.PRICE_YEAR);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "priceYear");
//    }
//
//    /*onClick METRAZH_HOME */
//    fun onClickMetrazhHome(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.METRAZH_HOME);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "perazh");
//    }
//
//    /*onClick Room */
//    fun onClickRoom(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.ROOM);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "room");
//    }
//
//    /*onClick BadRoom */
//    fun onClickBadRoom(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.BAD_ROOM);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "onClickBadRoom");
//    }
//
//    /*onClick yearConstruction */
//    fun onClickyearConstruction(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.YEAR_CONNSTRUCTION);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "yearConstruction");
//    }
//
//    /*onClick Km */
//    fun onClickKm(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.KM);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "km");
//    }
//
//    /*onClick TypeFuel */
//    fun onClickTypeFuel(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.TYPE_FUEL);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "typeFuel");
//    }
//
//    /*onClick YearConstructionCar */
//    fun onClickYearConstructionCar(view: View) {
//        var realValue = RealValueFragDialog().newInstance(ContextApp.YEAR_CONNSTRUCTION_CAR);
//        realValue.returnContent = this;
//        realValue.show(supportFragmentManager, "yearConstructionCar");
//    }
//
//    /*onClick Parking */
//    fun onClickParking(view: View) {
//        var realValue = SelectorFragDialog().newInstance(ContextApp.PARKING);
//        realValue.returnContentSelector = this;
//        realValue.show(supportFragmentManager, "parking");
//    }
//
//    /*onClick Heating */
//    fun onClickHeating(view: View) {
//        var realValue = SelectorFragDialog().newInstance(ContextApp.HEATING);
//        realValue.returnContentSelector = this;
//        realValue.show(supportFragmentManager, "heating");
//    }
//
//    /*onClick Cooling */
//    fun onClickCooling(view: View) {
//        var realValue = SelectorFragDialog().newInstance(ContextApp.COOLING);
//        realValue.returnContentSelector = this;
//        realValue.show(supportFragmentManager, "cooling");
//    }
//
//
//    /*onClick Assignment*/
//    fun onClickAssignment(view: View) {
//        var dialogAssignment = AssignmentFragDialog()
//        dialogAssignment.returnContent = this;
//        dialogAssignment.show(supportFragmentManager, "Assignment")
//    }
//
//
//}