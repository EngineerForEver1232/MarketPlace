package com.pedpo.pedporent.view.store.addStore

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityAddStoreBinding
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.view.adapter.PhotoAdapter
import com.pedpo.pedporent.view.adapter.PhotoUnderAdapter
import com.pedpo.pedporent.view.dialog.FragmentDialogChooseImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.CategoryStoreActivity
import com.pedpo.pedporent.view.store.TransferStore
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
open class StoreAddActivity : AppCompatActivity() , IReturnPhoto_FromDialog {

    internal lateinit var binding:ActivityAddStoreBinding ;
    @Inject
    open lateinit var photoAdapter: PhotoAdapter;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var photoUnderAdapter: PhotoUnderAdapter;
    @Inject
    lateinit var utillsApp: UtillsApp ;



    var list: ArrayList<PhotoTO>? = null;
     var transferStore: TransferStore?=null;

    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoreBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar();
        transferStore = TransferStore();


        utillsApp.focusTextInputLayout(binding.eTitle,binding.inputTitle)
//        utillsApp.focusEditText(binding.eTitle , binding.consErrorTitle)
        utillsApp.focusTextInputLayout(binding.ePhone , binding.inputPhone)
        utillsApp.focusTextInputLayout(binding.eDescription , binding.inputDescription)
        utillsApp.focusTextInputLayout(binding.eEmail,binding.inputEmail)
        utillsApp.focusTextInputLayout(binding.eCategory,binding.inputCategory)

        binding.eCategory.setOnClickListener {
            activityResultLauncherAddMarket.launch(Intent(this@StoreAddActivity, CategoryStoreActivity::class.java ))
        }

    }

    fun setToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.store)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


    /*Onclick */
    fun onClickAvatar(view: View) {

        var fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.SINGLE);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage.iReturnphotoFromdialog = this;

        fragmentDialogChooseImage.show(supportFragmentManager, "dialogAddMarket");

    }
    /*Onclick City */
    fun onClickCity(view: View) {


    }
    private fun countPhoto_Full(): Boolean {
        return (list?.size ?: 0) >= 6;
    }

    override fun onReturnPhoto_FromCamera(uri: Uri) {
        binding.icErrorimage.visibility = View.GONE;
        binding.tCatErrorimage.visibility = View.GONE;


        if (!countPhoto_Full()) {
            convertPhoto(uri)
        }
    }

    override fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>) {
        binding.tCatErrorimage.visibility = View.GONE;
        binding.icErrorimage.visibility = View.GONE;

        for (gallery in arrayList) {
            val uri = Uri.parse("file://" + gallery.sdcardPath)
            if (!countPhoto_Full()) {
                convertPhoto(uri)
            }
//            Log.i("chooseImage", "gallery size : " + list?.size)
        }
    }



    override fun onReturnSinglePhoto_FromGallery(customGalleryTO: CustomGalleryTO) {
        binding.tCatErrorimage.visibility = View.GONE;
        binding.icErrorimage.visibility = View.GONE;

        val uri = Uri.parse("file://" + customGalleryTO.sdcardPath)
        convertPhoto(uri = uri)
    }

    private fun convertPhoto(uri: Uri) {
        executor.execute {
            transferStore?.uri = uri;

            val bitmaptest =
                RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);

//            var base64 = ConvertImage.encodeTobase64(bitmaptest)

            handler.post {
                binding.imgProfile.setImageBitmap(bitmaptest)


            }
        }
    }


     open fun onClickContinue(view: View) {

         if (transferStore?.listCategorySelected.isNullOrEmpty()){
             binding.inputCategory.error = getString(R.string.enter_this_item)
         } else
             binding.inputCategory.isErrorEnabled = false;

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

         if (binding.eDescription.text.isNullOrBlank())
         if ((binding.eDescription.text?.length ?: 0) <= 15){
             binding.inputDescription.error = getString(R.string.length_description)
         }else
             binding.inputDescription.isErrorEnabled = false;

         if (binding.eEmail.text.isNullOrEmpty())
             binding.inputEmail.error = getString(R.string.enter_this_item);
         else
             binding.inputEmail.isErrorEnabled = false;

         binding.tCatErrorimage.isVisible = transferStore?.uri == null;
         binding.icErrorimage.isVisible = transferStore?.uri == null;

         if (transferStore?.uri == null)
             return;

         if (transferStore?.listCategorySelected.isNullOrEmpty() )
             return;

        if (binding.eTitle.text.isNullOrEmpty())
            return;

        if (binding.ePhone.text.isNullOrEmpty())
            return;

        if (binding.eDescription.text.isNullOrEmpty())
            return;
        if (binding.eEmail.text.isNullOrEmpty())
            return;



        transferStore?.title = binding.eTitle.text.toString()
        transferStore?.description = binding.eDescription.text.toString()
        transferStore?.phone = binding.ePhone.text.toString()
         transferStore?.email = binding.eEmail.text.toString()

        val intentStore = Intent(this, StoreAddActivitySecond::class.java);

        intentStore.putExtra(ContextApp.STORE,transferStore);

         activityResultLauncherAddMarket.launch(intentStore)
//        startActivity(intentStore)
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

                    val str = StringBuilder();
                    val listCategoryID = arrayListOf<String>();

                    for (a in arr?: arrayListOf()){
                        Log.i("categoryStoreResult", " : ${a.categoryStoreName} ${a.categoryStoreID}")
                        listCategoryID.add(a.categoryStoreID?:"")
                        str.append(a.categoryStoreName+" , ")
                    }
                    transferStore?.listCategorySelected= listCategoryID;
                    binding.eCategory.setText("$str , ")

                    binding.inputCategory.isErrorEnabled = false;

                }
            }
        }


}