package com.pedpo.pedporent.view.store.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityEditDetailStoreBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.StoreDetailEditRequest
import com.pedpo.pedporent.model.store.StoreDetials
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import com.pedpo.pedporent.model.store.edit.Logo
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.AdapterCategoryGrid
import com.pedpo.pedporent.view.addMarket.LocationChooseActivity
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.CategoryStoreActivity
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditStoreDetailActivity : AppCompatActivity() , AdapterCategoryGrid.OnClickDeleteCategory {

    private var storeID: String? = null;
    lateinit var binding:ActivityEditDetailStoreBinding
    private var typeApi = "";
    val storeViewModel: ProfileViewModel by viewModels();
    var storeRequestEdit: StoreDetailEditRequest?=null;
    private var logo:Logo?=null;
    private var listItemCategory:MutableList<CategoryStoreTO>?=null;

    @Inject
    lateinit var adapterCategoryGrid:AdapterCategoryGrid ;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDetailStoreBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar();

        typeApi = intent.getStringExtra(ContextApp.TYPE_API) ?: "";
        listItemCategory = arrayListOf();

        binding.recyclerCategory.layoutManager = GridLayoutManager(this,2)
        binding.recyclerCategory.adapter = adapterCategoryGrid;
        adapterCategoryGrid.setOnClickDeleteCategory(this)

        storeRequestEdit = StoreDetailEditRequest();
        logo = Logo()

        loadData()

    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


    fun loadData() {

        showProgressBar.show(supportFragmentManager)
        storeViewModel.detailStore()?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<StoreDetials> {
                override fun onSuccess(data: StoreDetials) {
                    showProgressBar.dismiss()
                    if (data.isSuccess == true) {

                         val storeTO = data.data;
                        storeRequestEdit?.title = storeTO?.title;
                        storeRequestEdit?.role = storeTO?.roles;
                        storeRequestEdit?.categoryList = storeTO?.listCategoryID;
                        for (index  in storeTO?.listCategoryID?.indices ?: arrayListOf()){
                            Log.i("testEditStore", "2 : " +storeTO?.listCategoryName?.get(index))
                            val categoryStoreTO= CategoryStoreTO();
                            categoryStoreTO.categoryStoreID = storeTO?.listCategoryID?.get(index)
                            categoryStoreTO.categoryStoreName = storeTO?.listCategoryName?.get(index)
                            listItemCategory?.add(categoryStoreTO)
                        }
                        Log.i("testEditStore", "2 : " +listItemCategory?.size)

                        adapterCategoryGrid.updateAdapter(listItemCategory?: arrayListOf())

                        storeRequestEdit?.phone = storeTO?.phone;
                        storeRequestEdit?.email = storeTO?.email;
                        storeRequestEdit?.description = storeTO?.description;
                        storeRequestEdit?.latitude = storeTO?.latitude;
                        storeRequestEdit?.longitude = storeTO?.longitude;
                        storeRequestEdit?.cityID = storeTO?.cityID;

                        Log.i("testEditStore", "c: ${storeRequestEdit?.cityID}")
//                        Log.i("testEditStore", "c: ${storeTO?.categoryStoreID}")

                        binding.eTitle.setText(storeRequestEdit?.title?:"");
                        binding.eDescription.setText(storeRequestEdit?.description);
                        binding.ePhone.setText(storeRequestEdit?.phone);
                        binding.eEmail.setText(storeRequestEdit?.email);
                        binding.tAddress.setText(storeTO?.cityName);
                        binding.eRole.setText(storeTO?.roles);

                        storeRequestEdit?.sendingInner = storeTO?.inner
                        storeRequestEdit?.sendingOuner = storeTO?.onner

                        binding.checkInner.isChecked = storeTO?.inner?:false
                        binding.checkOuner.isChecked = storeTO?.onner?:false

                        val str = StringBuilder();
                        for (a in storeTO?.listCategoryName?: arrayListOf()){
                            Log.i("categoryStoreResult", " : $a")
                            str.append("$a , ")
                        }


                        logo?.type = ContextApp.URL;
//                        logo?.photo = storeTO?.logo;

//                        Picasso.get().load(storeTO?.logo).into(binding.imgProfile);


                    } else {

                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }

            })
        )


    }


     fun onClickContinue(view: View) {
//        Log.i("testEditStore", "onClickContinue: ${storeTO?.cityID}")
        if (binding.eTitle.text.isNullOrEmpty()){
            binding.inputTitle.error = getString(R.string.enter_this_item)
        }else
            binding.inputTitle.isErrorEnabled = false;


        if (binding.ePhone.text.isNullOrEmpty()){
            binding.inputPhone.error = getString(R.string.enter_this_item)
        }else
            binding.inputPhone.isErrorEnabled = false;

        if (binding.eDescription.text.isNullOrEmpty()){
            binding.inputDescription.error = getString(R.string.enter_this_item)
        }else
            binding.inputDescription.isErrorEnabled = false;


        if (binding.eTitle.text.isNullOrEmpty())
            return;

        if (binding.ePhone.text.isNullOrEmpty())
            return;

        if (binding.eDescription.text.isNullOrEmpty())
            return;



        storeRequestEdit?.title = binding.eTitle.text.toString()
        storeRequestEdit?.description = binding.eDescription.text.toString()
        storeRequestEdit?.phone = binding.ePhone.text.toString()
        storeRequestEdit?.email = binding.eEmail.text.toString()
        storeRequestEdit?.role = binding.eRole.text.toString()

         storeRequestEdit?.sendingInner = binding.checkInner.isChecked;
         storeRequestEdit?.sendingOuner = binding.checkOuner.isChecked;


         val listCategoryID = arrayListOf<String>();

         for (a in listItemCategory?: arrayListOf()){
             listCategoryID.add(a.categoryStoreID?:"")
         }

         storeRequestEdit?.categoryList = listCategoryID;


//        Log.i("testEditStore", "onClickContinue: ${storeRequestEdit?.title}")
//        Log.i("testEditStore", "onClickContinue: ${storeRequestEdit?.description}")
//        Log.i("testEditStore", "onClickContinue: ${storeRequestEdit?.phone}")
//        Log.i("testEditStore", "onClickContinue: ${storeRequestEdit?.email}")
//        Log.i("testEditStore", "onClickContinue: ${storeRequestEdit?.categoryList}")


         showProgressBar.show(supportFragmentManager)

        storeViewModel.setEditStore(request = storeRequestEdit?: StoreDetailEditRequest() )?.observe(this,
            CustomObserver(object :CustomObserver.ResultListener<ResponseTO>{
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true){
                        finish()
                    }

                    Toast.makeText(this@EditStoreDetailActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show();
                }

                override fun onException(exception: Exception) {
                    Toast.makeText(this@EditStoreDetailActivity,exception.message.toString(),Toast.LENGTH_SHORT).show();

                }

            }))
    }


    private var activityResultLauncherGeneral =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                binding.tAddress.text = result.data?.getStringExtra(ContextApp.CITY) ;
                storeRequestEdit?.cityID = result.data?.getStringExtra(ContextApp.CITY_ID) ;

                storeRequestEdit?.latitude = result.data?.getStringExtra(ContextApp.LATITUDE) ;
                storeRequestEdit?.longitude = result.data?.getStringExtra(ContextApp.LONGITUDE) ;

                binding.constraintAddress.setBackgroundResource(R.drawable.border_search_white);


            } else if (result.resultCode == ContextApp.FINISH) {
                setResult(ContextApp.FINISH)
                finish()
            }
        }

    /**Loucher Add Market **/
    var activityResultLauncherAddMarket =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ContextApp.FINISH -> {
                    setResult(ContextApp.FINISH)
                    finish()
                }

                ContextApp.CATEGORY_RESULT_ID -> {
//                    transferStore?.categoryID = result.data?.getStringExtra(ContextApp.CATEGORY_ID);
//                    transferStore?.listCategorySelected = result.data?.getParcelableArrayListExtra(ContextApp.CATEGORY_ID);

                    val arr = result.data?.getParcelableArrayListExtra<CategoryStoreTO>(ContextApp.CATEGORY_ID);

                    val listCategoryID = arrayListOf<String>();

                    for (a in arr?: arrayListOf()){
                        listCategoryID.add(a.categoryStoreID?:"")
                    }
                    storeRequestEdit?.categoryList = listCategoryID;

                    listItemCategory = arr;

                    adapterCategoryGrid.updateAdapter(arr?: arrayListOf())

                }
            }
        }


    /*Onclick Category */
    fun onClickCategory(view: View){
        activityResultLauncherAddMarket.launch(Intent(this@EditStoreDetailActivity, CategoryStoreActivity::class.java ))

    }

    /*Onclick City */
    fun onClickCity(view: View) {
        val intent = Intent(this, LocationChooseActivity::class.java);
//        val intent = Intent(this, AddressStoreActivity::class.java);
        intent.putExtra(ContextApp.STORE, true);
//        intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

        activityResultLauncherGeneral.launch(intent)

    }

    override fun onClickDeleteCategory(categoryStoreTO: CategoryStoreTO?) {

        Log.i("testEditStore", "size  : " +listItemCategory?.size)

        listItemCategory?.remove(categoryStoreTO)
        adapterCategoryGrid.updateAdapter(listItemCategory?: arrayListOf())

    }


}