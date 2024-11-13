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
//import androidx.core.view.isVisible
//import com.example.pedporent.R
//import com.example.pedporent.databinding.ActivityGeneralAddmarketBinding
//import com.example.pedporent.listener.ReturnContent
//import com.example.pedporent.listener.ReturnContentInt
//import com.example.pedporent.listener.ReturnPrice
//import com.example.pedporent.model.market.TypeOfguaranteeTO
//import com.example.pedporent.model.market.editMarket.EditMarketTO
//import com.example.pedporent.utills.ContextApp
//import com.example.pedporent.utills.MyContextWrapper
//import com.example.pedporent.view.dialog.AssignmentFragDialog
//import com.example.pedporent.view.dialog.RealValueFragDialog
//import com.example.pedporent.view.dialog.SecurityDepositFragDialog
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class GeneralSpecificationActivityOLD : AppCompatActivity(), ReturnContentInt, ReturnPrice,
//    ReturnContent {
//
//
//    lateinit var binding: ActivityGeneralAddmarketBinding
//    private var resultCategory: String? = null;
//    var categoryMarketID: String? = null;
//    var subCategoryMarketID: String? = null;
//    private var market: EditMarketTO? = null;
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
//        typeOfguaranteeTO = intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)?:TypeOfguaranteeTO();
//
//
//        if (market != null) {
//            binding.nameArea.text = market?.cityName;
//            cityID = market?.cityID;
//            lng = market?.longitude;
//            lat = market?.latitude;
////            typeOfguaranteeTO = market?.typeOfguaranteeTO!!
//
//
//            if (market?.free==true){
//                binding.tAssignmentType.text = getString(R.string.free);
//            }else if (market?.priceAgree==true){
//                binding.tAssignmentType.text = getString(R.string.an_agreement);
//            }else if (!market?.rentPrice.isNullOrEmpty()){
//                binding.constraintResultDaily.isVisible = true;
//                binding.constraintResultRealValue.isVisible = true;
//
//                binding.constraintRealValue.isVisible = true;
//                binding.constraintDaily.isVisible = true;
//
//                binding.tAssignmentType.text = getString(R.string.price2);
//                binding.layoutDeposit.isVisible = true;
//                binding.constraintResultDeposit.isVisible = true ;
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
//                this.priceDayli = market?.rentPrice?:"";
//                this.realValuePrice = market?.commodityPrice?:"";
//
//                binding.tResultDaily.text = market?.rentPrice?:"";
//                binding.tResultRealValue.text = market?.commodityPrice?:"";
//            }
//        }
//
//
//        resultCategory = intent?.getStringExtra(ContextApp.CATEGORY_NAME) ?: ""
//        categoryMarketID = intent?.getStringExtra(ContextApp.CATEGORY_ID) ?: ""
//        subCategoryMarketID = intent?.getStringExtra(ContextApp.SUB_CATEGORY_ID) ?: ""
//
//        binding.nameCategory.text = resultCategory;
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
//    private var city: String? = null;
//    private var cityID: String? = null;
//    private var lat: String? = null;
//    private var lng: String? = null;
//
//    private var activityResultLauncherGeneral =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                city = result.data?.getStringExtra(ContextApp.CITY)
//                cityID = result.data?.getStringExtra(ContextApp.CITY_ID);
//                binding.nameArea?.text = city
//                lat = result.data?.getStringExtra(ContextApp.LATITUDE)
//                lng = result.data?.getStringExtra(ContextApp.LONGITUDE)
//
//            } else if (result.resultCode == ContextApp.FINISH) {
//                setResult(ContextApp.FINISH)
//                finish()
//            }
//        }
//
//    fun onClickBtnConfirm(view: View) {
//
//        if (cityID.isNullOrEmpty()) {
//            Toast.makeText(this, "please Enter erea", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (assignment == ContextApp.AN_AGREEMENT || assignment == ContextApp.FREE) {
//        } else {
//
//            if (priceDayli.isNullOrEmpty() || realValuePrice.isNullOrEmpty()) {
//                Toast.makeText(this, "please Enter Price", Toast.LENGTH_SHORT).show()
//                return;
//            }
//        }
//
//        var intent = Intent()
//        intent.putExtra(ContextApp.CATEGORY, resultCategory)
//        intent.putExtra(ContextApp.CITY, city)
//        intent.putExtra(ContextApp.CITY_ID, cityID)
//        intent.putExtra(ContextApp.CATEGORY_ID, categoryMarketID)
//        intent.putExtra(ContextApp.SUB_CATEGORY_ID, subCategoryMarketID)
//        intent.putExtra(ContextApp.LATITUDE, lat)
//        intent.putExtra(ContextApp.LONGITUDE, lng)
//        intent.putExtra(ContextApp.PRICE_DAILY, priceDayli)
//        intent.putExtra(ContextApp.REAL_VALUE, realValuePrice)
//        intent.putExtra(ContextApp.ASSIGNMENT, assignment)
//        intent.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
//        setResult(ContextApp.RESULT_Ad, intent)
//        finish()
//    }
//
//
//    var typeOfguaranteeTO = TypeOfguaranteeTO();
//    var stringBuilder: StringBuilder? = null;
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
//    private var priceDayli: String? = null;
//    private var realValuePrice: String? = null;
//    private var assignment: String? = null;
//
//
//    /*Return Price Dayli*/
//    override fun returnPriceDayli(priceDayli: String?) {
//         binding.tResultDaily.text = priceDayli;
//        binding.constraintResultDaily.isVisible = true;
//        this.priceDayli = priceDayli;
//    }
//
//    /*Return Price Real*/
//    override fun returnPriceReal(priceRealy: String?) {
//         binding.tResultRealValue.text = priceRealy;
//        binding.constraintResultRealValue.isVisible = true;
//        this.realValuePrice = priceRealy;
//    }
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
//    /*onClick Assignment*/
//    fun onClickAssignment(view: View) {
//        var dialogAssignment = AssignmentFragDialog()
//        dialogAssignment.returnContent = this;
//        dialogAssignment.show(supportFragmentManager, "Assignment")
//    }
//
//
//    override fun returnContent(content: String?) {
//        when (content) {
//            ContextApp.AN_AGREEMENT -> {
//                binding.tAssignmentType.text = getString(R.string.an_agreement);
//                binding.layoutDeposit.isVisible = true
////                binding.constraintResultDeposit.isVisible = false
//                binding.constraintRealValue.isVisible = false;
//                binding.constraintResultRealValue.isVisible = false;
//
//                binding.constraintDaily.isVisible = false;
//                binding.constraintResultDaily.isVisible = false;
//
//                this.priceDayli = "";
//                this.realValuePrice = "";
//                binding.tResultDaily.text = "";
//                binding.tResultRealValue.text = "";
//
//            }
//            ContextApp.FREE -> {
//                binding.tAssignmentType.text = getString(R.string.free);
//                binding.layoutDeposit.isVisible = false
//                binding.constraintResultDeposit.isVisible = false
//                binding.constraintRealValue.isVisible = false
//                binding.constraintResultRealValue.isVisible = false
//                binding.constraintDaily.isVisible = false
//                binding.constraintResultDaily.isVisible = false
//
//                binding.tResultDaily.text = "";
//                binding.tResultDeposit.text = "";
//                binding.tResultRealValue.text = "";
//
//                this.priceDayli = "";
//                this.realValuePrice = "";
//                this.assignment
//            }
//            else -> {
//                binding.tAssignmentType.text = getString(R.string.price2);
//                binding.layoutDeposit.isVisible = true;
////                binding.constraintResultDeposit.isVisible = true ;
//                binding.constraintRealValue.isVisible = true;
////                binding.constraintResultRealValue.isVisible = true ;
//                binding.constraintDaily.isVisible = true;
////                binding.constraintResultDaily.isVisible = true ;
//            }
//        }
//        assignment = content;
//    }
//
//}