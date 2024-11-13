package com.pedpo.pedporent.view.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityCategoryStoreBinding
import com.pedpo.pedporent.databinding.AdapterCategoryBinding
import com.pedpo.pedporent.listener.ClickAdapterCategoryStore
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.CategoryStoreAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryStoreActivity : AppCompatActivity(), ClickAdapterCategoryStore {

    lateinit var binding: ActivityCategoryStoreBinding ;
    private val storeViewModel: ProfileViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var prefApp:PrefApp
    @Inject
    lateinit var adapter: CategoryStoreAdapter;
    var listCategorySelected:ArrayList<CategoryStoreTO>?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryStoreBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root);

        listCategorySelected = arrayListOf();
        binding.recyclerCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_vertical_category)!!)

        binding.recyclerCategory.addItemDecoration(divider);
        binding.recyclerCategory.adapter = adapter;
        adapter.clickAdapterCategory = this;


        getCategory()

    }

    fun getCategory() {
        showProgressBar.show(supportFragmentManager)
        storeViewModel.getCategoryStore()?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<CategoryStoreData> {
                override fun onSuccess(dataInput: CategoryStoreData) {
                    showProgressBar.dismiss()

//                    Log.i("categoryStore", "onSuccess: ${dataInput?.data?.size}")
                    if (dataInput.isSuccess == true) {

                        val categories = arrayListOf<CategoryStoreTO>();
                        for (index in dataInput.data?.indices ?: arrayListOf()){
                            if (index!=0)
                                categories.add(dataInput.data?.get(index = index)?:CategoryStoreTO())
                        }
                        adapter.updateAdapter(categories, "")
                    } else {
                        Toast.makeText(
                            this@CategoryStoreActivity,
                            dataInput?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onException(exception: Exception) {
                    Toast.makeText(
                        this@CategoryStoreActivity,
                        getString(R.string.try_again),
                        Toast.LENGTH_SHORT
                    ).show()
                    showProgressBar.dismiss()
                }

            })
        )
    }


    /* Onclick back */
    fun onClickBack(view: View) {
        onBackPressed()
    }
    /* onClick Apply */
    fun onClickApply(view: View) {
        val intent =Intent();
        intent.putParcelableArrayListExtra(ContextApp.CATEGORY_ID,listCategorySelected);
        setResult(ContextApp.CATEGORY_RESULT_ID,intent);
        finish();
    }

    override fun OnItemClickListenerAdapter(categoryTO: CategoryStoreTO, binding: AdapterCategoryBinding) {

        if (categoryTO.selected == true) {
            binding.containerConstr.setBackgroundResource(R.drawable.back_adapter_category)
            binding.nameCategory.setTextColor(ContextCompat.getColor(this@CategoryStoreActivity,R.color.black_dark))
            binding.icCategory.setColorFilter(ContextCompat.getColor(this@CategoryStoreActivity,R.color.tinticon))
            binding.icCategory.setImageResource(R.drawable.ic_dot)
            listCategorySelected?.remove(categoryTO)
        }else {
            binding.containerConstr.setBackgroundResource(R.drawable.back_adapter_category_selected)
            binding.nameCategory.setTextColor(ContextCompat.getColor(this@CategoryStoreActivity,R.color.white_dark))
            binding.icCategory.setColorFilter(ContextCompat.getColor(this@CategoryStoreActivity,R.color.white_dark))
            binding.icCategory.setImageResource(R.drawable.ic_baseline_check_circle_24)
            listCategorySelected?.add(categoryTO)
        }
        categoryTO.selected = categoryTO.selected !=true;

//        val intent =Intent();
//        intent.putExtra(ContextApp.CATEGORY_ID,categoryTO.categoryStoreID);
//        intent.putExtra(ContextApp.CATEGORY_NAME,categoryTO.categoryStoreName);
//        setResult(ContextApp.CATEGORY_RESULT_ID,intent);
//        finish();


//        val intent = if (prefApp.isStore())
//            Intent(this@CategoryStoreActivity, EditStoreAddActivity::class.java);
//        else
//            Intent(this@CategoryStoreActivity, StoreAddActivity::class.java);
//
//        intent.putExtra(ContextApp.CATEGORY_ID, categoryTO.categoryStoreID);
////        startActivity(intent)
//        activityResultLauncherAddMarket.launch(intent)
    }

//    /**Loucher Add Market **/
//    var activityResultLauncherAddMarket =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            when (result.resultCode) {
//                ContextApp.FINISH -> {
//                    setResult(ContextApp.FINISH)
//                    finish()
//                }
//            }
//        }

}