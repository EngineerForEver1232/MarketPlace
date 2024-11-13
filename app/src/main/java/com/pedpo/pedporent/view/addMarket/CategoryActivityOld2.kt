//package com.pedpo.pedporent.view.addMarket
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.MenuItem
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.databinding.ActivityCategoryOld2Binding
//import com.pedpo.pedporent.model.market.category.CategoryData
//import com.pedpo.pedporent.listener.ClickAdapterCategory
//import com.pedpo.pedporent.model.market.category.CategoryTO
//import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
//import com.pedpo.pedporent.model.market.editMarket.EditMarketTO
//import com.pedpo.pedporent.utills.ContextApp
//import com.pedpo.pedporent.utills.CustomObserver
//import com.pedpo.pedporent.utills.MyContextWrapper
//import com.pedpo.pedporent.utills.language.PrefManagerLanguage
//import com.pedpo.pedporent.view.adapter.CategoryAdapter
//import com.pedpo.pedporent.view.dialog.ShowProgressBar
//import com.pedpo.pedporent.view.filter.FilterActivity
//import com.pedpo.pedporent.viewModel.AdMarketViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class CategoryActivityOld2 : AppCompatActivity(), ClickAdapterCategory {
//
//    lateinit var binding: ActivityCategoryOld2Binding
//
//    @Inject
//    lateinit var categoryAdapter: CategoryAdapter;
//
//    @Inject
//    lateinit var showProgressBar: ShowProgressBar
//
//    private var market: EditMarketTO? = null;
//
//    private val viewModel: AdMarketViewModel by viewModels()
//    var typeOfguaranteeTO = TypeOfguaranteeTO();
//    var typeMarket :String?=null;
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCategoryOld2Binding.inflate(layoutInflater)
//        binding.lifecycleOwner = this;
//        binding.listener = this;
//        setContentView(binding.root)
//
//        market = intent.getParcelableExtra<EditMarketTO>(ContextApp.MARKET)?:EditMarketTO();
//        typeOfguaranteeTO =
//            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)?: TypeOfguaranteeTO();
//
//        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET)?:""
//
//        when (typeMarket) {
//            ContextApp.RENT -> binding?.tLabelBar.text ="${getString(R.string.rent)} - ${getString(R.string.ad_registration)}"
//            ContextApp.SALE -> binding?.tLabelBar.text ="${getString(R.string.sell)} - ${getString(R.string.ad_registration)}"
//            else -> binding?.tLabelBar.text ="${getString(R.string.service)} - ${getString(R.string.ad_registration)}"
//        }
//
//
//        binding.recyclerCategory?.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        var divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_vertical_category)!!)
//
//        binding.recyclerCategory.addItemDecoration(divider)
//        categoryAdapter?.clickAdapterCategory = this;
//        binding.recyclerCategory?.adapter = categoryAdapter;
//
//
////        getAllCategory_ForInsert()
//        getParentCategory(true)
//
//    }
//
//    var parentCategoryID: String? = null;
//    var childCategoryID: String? = null;
//    var pages = ArrayList<String>();
//    var nameCategoryParent:String?=null
//    var nameCategoryChild=""
//
//    private fun loadDataIconsService() {
//
//        viewModel.getCategoriesService()
//            ?.observe(
//                this,
//                CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
//                    override fun onSuccess(dataInput: CategoryData) {
//
//                        if (dataInput?.isSuccess!!) {
////                            iconsAdapter?.updateAdapter(dataInput.data!!)
//                        } else {
//
//                        }
//                    }
//
//                    override fun onException(exception: Exception) {
//
//                    }
//
//                })
//            )
//    }
//
//
//
////    fun getAllCategory_ForInsert() {
////        viewModel.categoriesAll()?.observe(
////            this, CustomObserver(
////                object : CustomObserver.ResultListener<String> {
////                    override fun onSuccess(dataInput: String) {
////                        Log.i("categoryTestSql", "onSuccess: $dataInput")
////
////                        getParentCategory(true)
////                    }
////
////                    override fun onException(exception: Exception) {
////                        Log.e("categoryTestSql", "onException: " + exception.message)
////                    }
////
////                })
////        )
////    }
//
//    fun getParentCategory(adStack: Boolean) {
////        viewModel.selectParent()
////            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
////                override fun onSuccess(dataInput: CategoryData) {
//////                    if (dataInput.data != null && dataInput.data?.isEmpty()!!) {
//////                        getAllCategory_ForInsert()
//////                    } else {
//////                        categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
//////
//////                        if (adStack)
//////                            pages.add("")
//////                        parentCategoryID = "";
//////                    }
////                }
////
////                override fun onException(exception: Exception) {
////
////                }
////            }))
//    }
//
//    fun getChildCategory(parentID: String?, name: String?, adStack: Boolean) {
//        viewModel.selectChild(parentID = parentID)
//            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
//                override fun onSuccess(dataInput: CategoryData) {
////                    if (dataInput.data != null && dataInput.data?.isEmpty()!!) {
////                        getAllCategory_ForInsert()
////                    } else {
////                        categoryAdapter?.updateAdapter(dataInput.data!!, type = dataInput.type)
////
////                        if (adStack)
////                            pages.add(parentID!!)
////                        parentCategoryID = parentID;
////                    }
//                }
//
//                override fun onException(exception: Exception) {
//
//                }
//            }))
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) finish()
//        return super.onOptionsItemSelected(item)
//    }
//
//    @Inject
//    lateinit var prefLang: PrefManagerLanguage
//    private var isHome:Boolean?=false
//    private var isCar:Boolean?=false
//
//    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO, type: String) {
//
//        if (type == ContextApp.CATEGORY) {
//            if (prefLang.language == ContextApp.EN)
//                nameCategoryParent= categoryTO.englishCategoryMarketName
//            else
//                nameCategoryParent = categoryTO.categoryMarketName
//
//            isHome = categoryTO.isHome;
//            isCar = categoryTO.isCar;
////            Log.i("categoryType", "OnItemClickListenerAdapter: ${categoryTO.isHome}")
//
//            getChildCategory(categoryTO.categoryMarketID, categoryTO.categoryMarketName, true)
//        } else {
//            nameCategoryChild = categoryTO?.englishCategoryMarketName?:"";
//            childCategoryID = categoryTO.categoryMarketID;
//
//            if (intent.getStringExtra(ContextApp.PAGE) == ContextApp.PAGE_FILTER ){
//
//                var intentFilter =
//                    Intent(this@CategoryActivityOld2, FilterActivity::class.java);
//
//                intentFilter?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
//                intentFilter?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)
//
////                intentFilter?.putExtra(ContextApp.TYPE_CATEGORY, nameCategoryBase)
//                if (prefLang.language == ContextApp.EN)
//                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.englishCategoryMarketName)
//                else
//                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.categoryMarketName)
//
//                setResult(1,intentFilter)
//                finish()
//
//            }else{
//
//            var intent =
//                Intent(this@CategoryActivityOld2, GeneralSpecificationActivity::class.java);
//
//            intent?.putExtra(ContextApp.MARKET, market)
//            intent?.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
//            intent?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
//            intent?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)
//
//            intent?.putExtra(ContextApp.IS_HOME, isHome)
//            intent?.putExtra(ContextApp.IS_CAR, isCar)
//
//
//                if (prefLang.language == ContextApp.EN)
//                    intent?.putExtra(ContextApp.CATEGORY_NAME,nameCategoryParent+" \\ "+ categoryTO.englishCategoryMarketName)
//                else
//                    intent?.putExtra(ContextApp.CATEGORY_NAME,nameCategoryParent+" \\ "+ categoryTO.categoryMarketName)
//
//
//                intent.putExtra(ContextApp.TYPE_MARKET,typeMarket)
//
//                activityResultLauncherCategory.launch(intent)
//            }
//        }
//
//    }
//
//    private var activityResultLauncherCategory =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            when (result.resultCode) {
//                Activity.RESULT_OK -> {
//                    Toast.makeText(this, "category", Toast.LENGTH_SHORT).show()
//                }
//                ContextApp.RESULT_Ad -> {
//                    setResult(ContextApp.RESULT_Ad, result.data)
//                    finish()
//                }
//                ContextApp.FINISH -> {
//                    setResult(ContextApp.FINISH)
//                    finish()
//                }
//            }
//        }
//
//    override fun onBackPressed() {
//        if (pages.isNotEmpty())
//            pages.removeLast()
//
//        if (pages.isEmpty())
//            super.onBackPressed()
//        else {
//            getParentCategory(false)
//        }
//    }
//
//    /* Onclick */
//    fun onClickBack(view: View) {
//        onBackPressed()
//    }
//
//    /* Onclick */
//    fun onClickicClose(view: View) {
//        setResult(ContextApp.FINISH)
//        finish()
//    }
//}