package com.pedpo.pedporent.view.profile

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
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityMyMyitemsBinding
import com.pedpo.pedporent.listener.ClickMyItems
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.ActiveMarketTO
import com.pedpo.pedporent.model.ResponseComment
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.myItems.MyItemsData
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.view.adapter.MyItemsAdapter
import com.pedpo.pedporent.view.details.DetailsActivity
import com.pedpo.pedporent.view.dialog.MenuItemFragDialog
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.dialog.Utills
import com.pedpo.pedporent.view.editMarket.EditGeneralActivity
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.ButtonSwitch
import com.pedpo.pedporent.widget.calendar.CalendarActivity
import com.pedpo.pedporent.widget.calendar.utils.AppContents
import com.pedpo.pedporent.widget.calendar.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MyItemActivity : AppCompatActivity(), ClickMyItems, ReturnContent {

    private lateinit var binding: ActivityMyMyitemsBinding;
    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var adapterMyItems: MyItemsAdapter;
    @Inject
    lateinit var utills: Utills ;
    @Inject
    lateinit var numberFormat: NumberFormatDigits

    private var market: MyMarkets? = null;
    private var position: Int? = null;
    private var prefJalalai:PrefManager?=null;
    private var typeAPI :String?=null;
    private var listMyMyItems = ArrayList<MyMarkets>()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMyitemsBinding.inflate(layoutInflater)
        binding.listener = this
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.your_ads)

        prefJalalai = PrefManager(this)

        binding.recyclerMyItems.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerMyItems.adapter = adapterMyItems;
        adapterMyItems.clickMyItems = this;


        typeAPI = ContextApp.MARKET

        myItemsLoadData(typeAPI)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


    fun myItemsLoadData(typeAPI:String?) {
          this.typeAPI = typeAPI ;
        showProgressBar.show(fragmentManager = supportFragmentManager)
        profileViewModel.myItems()?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<MyItemsData> {
                override fun onSuccess(dataInput: MyItemsData) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess) {
                        if (typeAPI == ContextApp.MARKET) {
                            adapterMyItems.updateAdapter(dataInput.data?.myMarkets)
                            listMyMyItems = dataInput.data?.myMarkets as ArrayList<MyMarkets>;

                            if (dataInput.data?.myMarkets?.isEmpty()==true) {
                                binding.included.layoutError.isVisible = true;

                            }else {

                                binding.included.layoutError.isVisible = false;
                                binding.included.layoutError.isVisible = false;
                            }
                        }else{
                            adapterMyItems.updateAdapter(dataInput.data?.myService)
                            listMyMyItems = dataInput.data?.myService as ArrayList<MyMarkets>;

                            if (dataInput.data?.myService?.isEmpty()==true) {
                                binding.included.layoutError.isVisible = true;
                                binding.included.layoutError.isVisible = true;
                            }else{
                                binding.included.layoutError.isVisible = false;
                                binding.included.layoutError.isVisible = false;
                            }
                        }

                    } else {
                        binding.included.layoutError.isVisible = true;
                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                    binding.included.layoutError.isVisible = true;
                }

            })
        )
    }


    override fun onClickMyItems(market: MyMarkets?, position: Int?, type: String?) {

        when (type) {
            ContextApp.DETAILS -> {
                val intent = Intent(this, DetailsActivity::class.java);
                intent.putExtra(ContextApp.MARKET_ID, market?.marketID);
                intent.putExtra(ContextApp.TYPE_API,market?.isService);
                startActivity(intent)
            }
            ContextApp.MENU -> {
                this.market = market;
                this.position = position;

                val menuFrag = MenuItemFragDialog().newInstance(market?.isActive?:false,market?.marketID);
                menuFrag.returnContent = this;
                menuFrag.show(supportFragmentManager, ContextApp.MENU);
            }
            ContextApp.NOTIFICATION -> {
                Toast.makeText(this, market?.notification, Toast.LENGTH_SHORT).show();
            }
        }
    }



    override fun returnContent(content: String?) {
        when (content) {
            ContextApp.DEACTIVE ->{
                dateActive(market?.marketID?:"" , typeAPI ?:"")
            }
            ContextApp.ACTIVE ->{
                active(market?.marketID?:"" , typeAPI ?:"")
            }
            ContextApp.DELETE -> {
                utills.showDialogPositive(
                    getString(R.string.are_you_sure_delete),
                    getString(R.string.yes),
                    getString(R.string.no)
                ).observe(this, Observer {
                        if (it == true) {
                            deleteMarket(market);
                        }
                    })
            }
            ContextApp.EDIT -> {
//                var intent = Intent(this, EditMarketActivity::class.java);
                val intent = Intent(this, EditGeneralActivity::class.java)
                intent.putExtra(ContextApp.MARKET_ID, market?.marketID)
                intent.putExtra(ContextApp.TYPE_API, typeAPI)
                startActivity(intent)
            }
            ContextApp.COMMENT -> {
                closeComment(marketID = market?.marketID?:"")
            }


        }
    }

    private fun active(marketID: String, type: String) {
        showProgressBar.show(supportFragmentManager)
        profileViewModel.activeMarket(marketID,type)?.observe(this,
            CustomObserver(object :CustomObserver.ResultListener<ResponseTO>{
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess==true){

                        prefJalalai?.clear()
                        myItemsLoadData(typeAPI)
                        Toast.makeText(this@MyItemActivity,dataInput.message,Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MyItemActivity,dataInput.message,Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onException(exception: Exception) {

                }

            }))
    }

    private fun deleteMarket(myMarket:MyMarkets?) {
        profileViewModel.deleteMarket(myMarket)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    if (dataInput.isSuccess == true) {
                        listMyMyItems.remove(market)
                        adapterMyItems.notifyItemRemoved(position!!)
                        adapterMyItems.notifyDataSetChanged()
                    }
                    Log.i("testDelete", "onSuccess: ${dataInput.isSuccess}")
                }

                override fun onException(exception: Exception) {
                    Log.e("testDelete", "onSuccess: ${exception.message}")
                }
            }))
    }

    private fun deActive(activeMarketTO: ActiveMarketTO) {
        Log.i("testCalendar", "deActiveMarket: ${activeMarketTO?.MarketID}")

        profileViewModel?.deActiveMarket(activeMarketTO = activeMarketTO)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    if (dataInput?.isSuccess == true) {
                        myItemsLoadData(typeAPI)
                        Toast.makeText(this@MyItemActivity, dataInput.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {

                    }

                }

                override fun onException(exception: Exception) {

                }

            })
        )
    }

    private fun dateActive( marketID:String , type:String ){

        prefJalalai?.clear();

        val intent = Intent(this@MyItemActivity, CalendarActivity::class.java)
        intent.putExtra(ContextApp.MARKET_ID , marketID)
        intent.putExtra(ContextApp.TYPE , type)
        launcher.launch(intent);



    }



    var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                }
                AppContents.RESULT_SET_CALENDAR_POSTER -> {
                    val start = result.data?.getStringExtra(AppContents.FROM_CALENDAR_MILADI);
                    val end = result.data?.getStringExtra(AppContents.TO_CALENDAR_MILADI);

                    deActive(
                        ActiveMarketTO(
                            market?.marketID ?: "",
                            numberFormat.toNumberEnlish(start?:"")?:"",
                            numberFormat.toNumberEnlish(end?:"")?:"" ,
                            typeAPI = typeAPI?:""
                        )
                    )

                    Log.i("testDeactive", "marketID ${market?.marketID}")
                    Log.i("testDeactive", "result start : $start")
                    Log.i("testDeactive", "result end : $end")

                }
            }
        }


    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
          myItemsLoadData(ContextApp.MARKET)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@MyItemActivity).btnSwiche(
            binding.tr,binding.tS,binding.tService,
            binding.lineBottomRent,binding.lineBottomSale,binding.lineBottomService,
            binding.img,binding.imgSale, binding.imgService,1
        )
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {


        Handler(Looper.getMainLooper()).postDelayed({
            myItemsLoadData(ContextApp.MARKET)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@MyItemActivity).btnSwiche(
            binding.tS,binding.tr,binding.tService,
            binding.lineBottomSale,binding.lineBottomRent,binding.lineBottomService,
            binding.imgSale,binding.img, binding.imgService,2
        )
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
            myItemsLoadData(ContextApp.SERVICE)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(this@MyItemActivity).btnSwiche(
            binding.tService,binding.tr,binding.tS,
            binding.lineBottomService,binding.lineBottomRent,binding.lineBottomSale,
            binding.imgService,binding.img, binding.imgSale,3
        )
    }

    private fun closeComment(marketID:String){

        profileViewModel.closeComment(marketID = marketID)
            ?.observe(this@MyItemActivity,
                CustomObserver(object :CustomObserver.ResultListener<ResponseTO>{
                    override fun onSuccess(dataInput: ResponseTO) {

                        if (dataInput.isSuccess){
                            Toast.makeText(this@MyItemActivity,dataInput.message , Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@MyItemActivity,dataInput.message , Toast.LENGTH_SHORT).show()
                        }
                        getComment(marketID)
                    }

                    override fun onException(exception: Exception) {
                        Toast.makeText(this@MyItemActivity,"error :  ${exception.message}" , Toast.LENGTH_SHORT).show()
                    }
                }))
    }

    private fun getComment(marketID:String){
        profileViewModel.getStatusComment(marketID = marketID)
            ?.observe(this@MyItemActivity,
                CustomObserver(object :CustomObserver.ResultListener<ResponseComment>{
                    override fun onSuccess(dataInput: ResponseComment) {

                        Toast.makeText(this@MyItemActivity , dataInput.isCloseComment.toString() , Toast.LENGTH_SHORT).show()
                    }

                    override fun onException(exception: Exception) {

                    }
                }))
    }

}