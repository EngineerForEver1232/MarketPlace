package com.pedpo.pedporent.view.addMarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityCategoryBinding
import com.pedpo.pedporent.model.market.category.CategoryData
import com.pedpo.pedporent.listener.ClickAdapterCategory
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.view.adapter.CategoryAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.filter.FilterActivity
import com.pedpo.pedporent.viewModel.CategoryViewModel
import com.pedpo.pedporent.widget.ButtonSwitch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity(), ClickAdapterCategory {

    lateinit var binding: ActivityCategoryBinding

    @Inject
    lateinit var categoryAdapter: CategoryAdapter;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

//    private var market: EditMarketTO? = null;

//    private val viewModel: AdMarketViewModel by viewModels()
    private val viewModelCategory: CategoryViewModel by viewModels()
    var typeOfguaranteeTO = TypeOfguaranteeTO();
    var typeMarket: String? = null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this;
        binding.listener = this;
        setContentView(binding.root)

//        market = intent.getParcelableExtra<EditMarketTO>(ContextApp.MARKET) ?: EditMarketTO();
        typeOfguaranteeTO =
            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
                ?: TypeOfguaranteeTO();

        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET) ?: ContextApp.RENT


        binding.recyclerCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_vertical_category)!!)

        binding.recyclerCategory.addItemDecoration(divider)
        categoryAdapter.clickAdapterCategory = this;
        binding.recyclerCategory.adapter = categoryAdapter;


        if (intent.getStringExtra(ContextApp.PAGE) == ContextApp.PAGE_FILTER){
            binding?.layoutBtns.isVisible = false;
            binding?.tLabelBar.text = getString(R.string.filter)
            when (typeMarket) {
                ContextApp.RENT -> {
//                        getParentCategory(true)
                    onClickBtnCategoryRent(binding.btnCategoryRent)
                }
                ContextApp.SALE -> {
                    onClickBtnCategorySale(binding.btnCategorySale)
                }
                ContextApp.SERVICE -> {
        //                parentCategoryService(true)
                    onClickBtnCategoryService(binding.btnCategoryService)
                }
            }
        }else{
            getParentCategory(true)
        }
//        getAllCategory_ForInsert()
//        loadDataIconsService()

    }

    var parentCategoryID: String? = null;
    var childCategoryID: String? = null;
    var pages = ArrayList<String>();
    var nameCategoryParent: String? = null
    var nameCategoryChild = ""



    fun getParentCategory(adStack: Boolean) {
        viewModelCategory.selectListCategory(true)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                override fun onSuccess(dataInput: CategoryData) {
                    Log.e(
                        "categoryMarketPage",
                        "0 ${dataInput?.isSuccess} ${dataInput?.message} ${dataInput?.data?.type}"
                    )

                        categoryAdapter.updateAdapter(
                            dataInput.data?.categoryMarket ?: arrayListOf(),
                            type = dataInput?.data?.type
                        )

                        if (adStack) {
                            pages.clear()
                            pages.add("")
                        }
                        parentCategoryID = "";

                }

                override fun onException(exception: Exception) {

                }
            }))
    }

    fun getChildCategory(parentID: String?, name: String?, adStack: Boolean) {
        viewModelCategory.selectChild(parentID = parentID)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                override fun onSuccess(dataInput: CategoryData) {

                        categoryAdapter?.updateAdapter(
                            dataInput?.data?.categoryMarket ?: arrayListOf(),
                            type = dataInput?.data?.type
                        )

                        if (adStack)
                            pages.add(parentID ?: "")
                        parentCategoryID = parentID;
                    }

                override fun onException(exception: Exception) {

                }
            }))
    }

    private fun parentCategoryService(stack:Boolean?) {

        viewModelCategory.getCategoriesService(isAll = true)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                override fun onSuccess(dataInput: CategoryData) {
                    categoryAdapter?.updateAdapter(
                        dataInput.data?.categoryService ?: arrayListOf(),
                        dataInput?.data?.type
                    )

                    if (stack==true) {
                        pages.clear()
                        pages.add("")
                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    fun selectServiceChild(parentID: String?, name: String?, adStack: Boolean) {
        viewModelCategory.selectServiceChild(parentID = parentID)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<CategoryData> {
                override fun onSuccess(dataInput: CategoryData) {
                        categoryAdapter?.updateAdapter(
                            dataInput?.data?.categoryService ?: arrayListOf(),
                            type = dataInput?.data?.type
                        )

                        if (adStack)
                            pages.add(parentID ?: "")
                        parentCategoryID = parentID;
                }

                override fun onException(exception: Exception) {

                }
            }))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    @Inject
    lateinit var prefLang: PrefManagerLanguage
    private var isHome: Boolean? = false
    private var isCar: Boolean? = false

    override fun OnItemClickListenerAdapter(categoryTO: CategoryTO, type: String) {
        Log.i("categoryService", "OnItemClickListenerAdapter: ${type}")

        if (type == ContextApp.CATEGORY) {
            nameCategoryParent = if (prefLang.language == ContextApp.EN)
                categoryTO.englishCategoryMarketName
            else
                categoryTO.categoryMarketName

            isHome = categoryTO.isHome;
            isCar = categoryTO.isCar;
//            Log.i("categoryType", "OnItemClickListenerAdapter: ${categoryTO.isHome}")

            getChildCategory(categoryTO.categoryMarketID, categoryTO.categoryMarketName, true)
        } else if (type == ContextApp.SUB_CATEGORY) {
            nameCategoryChild = categoryTO?.englishCategoryMarketName ?: "";
            childCategoryID = categoryTO.categoryMarketID;

            if (intent.getStringExtra(ContextApp.PAGE) == ContextApp.PAGE_FILTER) {
                Log.i("testFilter", "filterrrr" )

                var intentFilter =
                    Intent(this@CategoryActivity, FilterActivity::class.java);

                intentFilter?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
                intentFilter?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)
                intentFilter?.putExtra(ContextApp.IS_HOME, isHome)
                intentFilter?.putExtra(ContextApp.IS_CAR, isCar)
//                intentFilter?.putExtra(ContextApp.TYPE_CATEGORY, nameCategoryBase)
                if (prefLang.language == ContextApp.EN)
                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.englishCategoryMarketName)
                else
                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.categoryMarketName)

                setResult(1, intentFilter)
                finish()

            } else {

                var intent = Intent(this@CategoryActivity, GeneralSpecificationActivity::class.java);

                intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

//                intent?.putExtra(ContextApp.MARKET, market)
                intent?.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
                intent?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
                intent?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)

                intent?.putExtra(ContextApp.IS_HOME, isHome)
                intent?.putExtra(ContextApp.IS_CAR, isCar)


                if (prefLang.language == ContextApp.EN)
                    intent?.putExtra(
                        ContextApp.CATEGORY_NAME,
                        nameCategoryParent + " \\ " + categoryTO.englishCategoryMarketName
                    )
                else
                    intent?.putExtra(
                        ContextApp.CATEGORY_NAME,
                        nameCategoryParent + " \\ " + categoryTO.categoryMarketName
                    )



                activityResultLauncherCategory.launch(intent)
            }
        } else if (type == ContextApp.SERVICE) {
            nameCategoryParent = if (prefLang.language == ContextApp.EN)
                categoryTO.englishCategoryMarketName
            else
                categoryTO.categoryMarketName

            selectServiceChild(categoryTO.categoryMarketID, categoryTO.categoryMarketName, true)

        } else if (type == ContextApp.SERVICE_CHILD_CATEGORY) {
            childCategoryID = categoryTO.categoryMarketID;

            if (intent.getStringExtra(ContextApp.PAGE) == ContextApp.PAGE_FILTER) {
                Log.i("testFilter", "filterrrr" )

                var intentFilter =
                    Intent(this@CategoryActivity, FilterActivity::class.java);

                intentFilter?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
                intentFilter?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)
                intentFilter?.putExtra(ContextApp.IS_HOME, isHome)
                intentFilter?.putExtra(ContextApp.IS_CAR, isCar)
//                intentFilter?.putExtra(ContextApp.TYPE_CATEGORY, nameCategoryBase)
                if (prefLang.language == ContextApp.EN)
                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.englishCategoryMarketName)
                else
                    intentFilter?.putExtra(ContextApp.CATEGORY_NAME, categoryTO.categoryMarketName)

                setResult(1, intentFilter)
                finish()

            }else {
                var intent =
                    Intent(this@CategoryActivity, GeneralSpecificationActivity::class.java);

                if (prefLang.language == ContextApp.EN)
                    intent?.putExtra(
                        ContextApp.CATEGORY_NAME,
                        nameCategoryParent + " \\ " + categoryTO.englishCategoryMarketName
                    )
                else
                    intent?.putExtra(
                        ContextApp.CATEGORY_NAME,
                        nameCategoryParent + " \\ " + categoryTO.categoryMarketName
                    )

//            intent?.putExtra(ContextApp.CATEGORY_NAME, nameCategoryParent)
                intent?.putExtra(ContextApp.CATEGORY_ID, parentCategoryID)
                intent?.putExtra(ContextApp.SUB_CATEGORY_ID, childCategoryID)

                intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

                activityResultLauncherCategory.launch(intent)
            }
        }

    }

    private var activityResultLauncherCategory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(this, "category", Toast.LENGTH_SHORT).show()
                }
                ContextApp.RESULT_Ad -> {
                    setResult(ContextApp.RESULT_Ad, result.data)
                    finish()
                }
                ContextApp.FINISH -> {
                    setResult(ContextApp.FINISH)
                    finish()
                }
            }
        }

    override fun onBackPressed() {
        if (pages.isNotEmpty())
            pages.removeLast()

        if (pages.isEmpty())
            super.onBackPressed()
        else {
            if (typeMarket == ContextApp.SERVICE)
                parentCategoryService(false)
            else
                getParentCategory(false)
        }
    }

    /* Onclick */
    fun onClickBack(view: View) {
        onBackPressed()
    }

    /* Onclick */
    fun onClickicClose(view: View) {
        setResult(ContextApp.FINISH)
        finish()
    }

    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.RENT, typeCategory)
            typeMarket = ContextApp.RENT
            getParentCategory(true)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@CategoryActivity).btnSwiche(
            binding.tr,binding.tS,binding.tService,
            binding.lineBottomRent,binding.lineBottomSale,binding.lineBottomService,
            binding.img,binding.imgSale, binding.imgService,1
        )
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.SALE,typeCategory)
            typeMarket = ContextApp.SALE
            getParentCategory(true)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@CategoryActivity).btnSwiche(
            binding.tS,binding.tr,binding.tService,
            binding.lineBottomSale,binding.lineBottomRent,binding.lineBottomService,
            binding.imgSale,binding.img, binding.imgService,2
        )
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.SERVICE,typeCategory)
            typeMarket = ContextApp.SERVICE
            parentCategoryService(true)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@CategoryActivity).btnSwiche(
            binding.tService,binding.tr,binding.tS,
            binding.lineBottomService,binding.lineBottomRent,binding.lineBottomSale,
            binding.imgService,binding.img, binding.imgSale,3
        )
    }
}