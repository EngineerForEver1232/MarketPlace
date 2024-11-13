package com.pedpo.pedporent.view.store.addStore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityAddStoreSecondBinding
import com.pedpo.pedporent.databinding.AdapterPhotoBinding
import com.pedpo.pedporent.listener.ClickAdapterAlbum
import com.pedpo.pedporent.listener.IReturnPhotoPermission
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.PhotoStoreTO
import com.pedpo.pedporent.model.store.StoreRequest
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PhotoAdapter
import com.pedpo.pedporent.view.adapter.PhotoUnderAdapter
import com.pedpo.pedporent.view.addMarket.LocationChooseActivity
import com.pedpo.pedporent.view.dialog.DialogFragmentShowImage
import com.pedpo.pedporent.view.dialog.FragmentDialogChooseImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.TransferStore
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
open class StoreAddActivitySecond : AppCompatActivity(), ClickAdapterAlbum, IReturnPhoto_FromDialog,
    IReturnPhotoPermission {

    internal lateinit var binding: ActivityAddStoreSecondBinding;
    val storeViewModel: ProfileViewModel by viewModels();

    @Inject
    lateinit var photoUnderAdapter: PhotoUnderAdapter;
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    open lateinit var photoAdapter: PhotoAdapter;
    @Inject
    lateinit var utillsApp: UtillsApp;
    @Inject
    lateinit var prefApp: PrefApp;

    var list: ArrayList<PhotoTO>? = null;
    var listDelete: ArrayList<String>? = null;
    private var listBottom: ArrayList<PhotoTO>? = null;
    internal var transferStore: TransferStore? = null;

    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper());
    var counter: Int = 0;
    var photosBase64 = ArrayList<PhotoStoreTO>();
    var storeRequest: StoreRequest? = null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoreSecondBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar();
        storeRequest = StoreRequest();

        transferStore = intent.getParcelableExtra<TransferStore>(ContextApp.STORE);

        photoAdapter.clickAdapterAlbum = this;

        designChoosePhotos()


    }


    internal open fun createStore() {

        storeRequest?.title = transferStore?.title ?: ""
        storeRequest?.description = transferStore?.description ?: ""
        storeRequest?.phone = transferStore?.phone ?: ""
        storeRequest?.email = transferStore?.email ?: ""
        storeRequest?.categoryList = transferStore?.listCategorySelected
        storeRequest?.roles =  "roles"



        if (transferStore?.uri != null) {
            val bitmaptest =
                RotationPhotoUtills.handleSamplingAndRotationBitmap(this, transferStore?.uri);

            val base64 = ConvertImage.encodeTobase64(bitmaptest)
            storeRequest?.logo = base64
        }

        storeRequest?.sendingInner = binding.checkInner.isChecked
        storeRequest?.sendingOuner = binding.checkOuner.isChecked

        photosBase64.clear();
        for (index in list?.indices ?: emptyList()) {
            if (index == 0)
                continue;
            photosBase64.add(
                PhotoStoreTO(
                    order = (list?.get(index)?.counter!! - 1),
                    photo = list?.get(index)?.base64!!
                )
            );
        }
        storeRequest?.storePhotoes = photosBase64;


        binding.tCatErrorimage.isVisible = storeRequest?.storePhotoes.isNullOrEmpty();
        binding.icErrorimage.isVisible = storeRequest?.storePhotoes.isNullOrEmpty();


        if (storeRequest?.cityID.isNullOrEmpty())
            binding.constraintAddress.setBackgroundResource(R.drawable.border_stroke_error)
        if (storeRequest?.madrak.isNullOrEmpty())
            binding.constraintMadrak.setBackgroundResource(R.drawable.border_stroke_error)


        if (storeRequest?.storePhotoes.isNullOrEmpty())
            return
        if (storeRequest?.cityID.isNullOrEmpty())
            return
        if (storeRequest?.madrak.isNullOrEmpty())
            return

        Log.i("categoryStore", "createStore: ${storeRequest?.storePhotoes}")
        Log.i("categoryStore", "createStore: ${storeRequest?.madrak}")
        Log.i("categoryStore", "createStore: ${storeRequest?.cityID}")
        Log.i("categoryStore", "createStore: ${storeRequest?.latitude}")
        Log.i("categoryStore", "createStore: ${storeRequest?.longitude}")
        Log.i("categoryStore", "createStore: ${storeRequest?.sendingInner}")
        Log.i("categoryStore", "createStore: ${storeRequest?.sendingOuner}")
        Log.i("categoryStore", "createStore: ${storeRequest?.roles}")

        showProgressBar.show(supportFragmentManager)

        storeViewModel.createStore(storeRequest = storeRequest)
            ?.observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
//                    Log.i("categoryStore", "onSuccess: ${dataInput?.message}")

                    if (dataInput.isSuccess == true) {
                        Toast.makeText(
                            this@StoreAddActivitySecond,
                            dataInput.message + "",
                            Toast.LENGTH_SHORT
                        ).show()

                        prefApp.isStore(true)
                        setResult(ContextApp.FINISH)
                        finish();
                    } else
                        Toast.makeText(
                            this@StoreAddActivitySecond,
                            dataInput.message + "",
                            Toast.LENGTH_SHORT
                        ).show()

                }

                override fun onException(exception: Exception) {
//                    Log.e("categoryStore", "onException2: ${exception.message}")
                    Toast.makeText(this@StoreAddActivitySecond,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
                    showProgressBar.dismiss()

                }

            }))
    }


    fun setToolbar() {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.store)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    fun designChoosePhotos() {

        binding.recyclerPhotoUnder.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerPhotoUnder.adapter = photoUnderAdapter;
        listBottom = ArrayList<PhotoTO>();
        listBottom?.add(PhotoTO(R.drawable.ic_baseline_photo_camera_48, null, null, null, 0))
        listBottom?.add(PhotoTO(R.drawable.ic_gallery_market, null, null, null, 1))
        listBottom?.add(PhotoTO(R.drawable.ic_gallery_market, null, null, null, 2))
        listBottom?.add(PhotoTO(R.drawable.ic_gallery_market, null, null, null, 3))
        listBottom?.add(PhotoTO(R.drawable.ic_gallery_market, null, null, null, 4))
        listBottom?.add(PhotoTO(R.drawable.ic_gallery_market, null, null, null, 5))
        photoUnderAdapter.updateAdapter(listBottom ?: arrayListOf());


        binding.recyclerPhoto.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerPhoto.adapter = photoAdapter;
        listDelete = ArrayList<String>();
        list = ArrayList<PhotoTO>();
        list?.add(PhotoTO(R.drawable.ic_baseline_photo_camera_48, null, null, null, 0, ""))
        //        list?.add(PhotoTO(R.drawable.image_test1, null, null, null, 1))
//        list?.add(PhotoTO(R.drawable.image_test2, null, null, null, 2))
//        list?.add(PhotoTO(R.drawable.image_test3, null, null, null, 2))
        photoAdapter?.updateAdapter(list!!)

        val itemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 100;
        itemAnimator.removeDuration = 100;
        itemAnimator.moveDuration = 300;
//        itemAnimator.changeDuration = 500;
        binding.recyclerPhoto.itemAnimator = itemAnimator;
    }


    private fun countPhoto_Full(): Boolean {
        return (list?.size ?: 0) >= 6 ;
    }

    override fun onItemClickChoosePhoto(photoTO: PhotoTO, position: Int) {
        if (photoTO.counter == 0) {
            if (countPhoto_Full()) {
                return;
            }

            val fragmentDialogChooseImage =
                FragmentDialogChooseImage().newInstance(list?.size ?: 0, ContextApp.MULTI);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
            fragmentDialogChooseImage.iReturnphotoFromdialog = this;

            fragmentDialogChooseImage.show(supportFragmentManager, "dialogAddMarket");
        }
    }

    override fun setOrginalPhotoInAlbums_Adapter(
        photoTO: PhotoTO,
        position: Int,
        viewHolder: AdapterPhotoBinding
    ) {

        list?.remove(photoTO)
        list?.add(1, photoTO)

        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        viewHolder.constraintCamera.startAnimation(animationFadeIn)

        photoAdapter.notifyItemRemoved(position)
        photoAdapter.notifyItemInserted(1)

        Handler(Looper.getMainLooper()).postDelayed({
            photoAdapter.notifyDataSetChanged()
        }, 700)

        initOrderPhotos()
    }


    override fun onDeletePhotoFromAlbums_Adapter(
        photoTO: PhotoTO,
        position: Int,
        viewHolder: AdapterPhotoBinding
    ) {
        if (photoTO?.type.equals(ContextApp.URL)) {
            listDelete?.add(photoTO.photoUri.toString())
        }
        list?.remove(photoTO)

        photoAdapter?.notifyItemRemoved(position)

        Handler(Looper.getMainLooper()).postDelayed({
            photoAdapter?.notifyDataSetChanged()
        }, 390)

        initOrderPhotos()
    }

    override fun onReturnPhoto_FromCamera(uri: Uri) {
        binding.tCatErrorimage.isVisible = false;
        binding.icErrorimage.isVisible = false;

        if (!countPhoto_Full()) {
            convertPhoto(uri)
        }
    }

    override fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>) {
        binding.tCatErrorimage.isVisible = false;
        binding.icErrorimage.isVisible = false;


        for (gallery in arrayList) {
            val uri = Uri.parse("file://" + gallery.sdcardPath)
            if (!countPhoto_Full()) {
                convertPhoto(uri)
            }
//            Log.i("chooseImage", "gallery size : " + list?.size)
        }
    }

    override fun onReturnSinglePhoto_FromGallery(customGalleryTO: CustomGalleryTO) {
//        Log.i("testPhotos", "onReturnSinglePhoto_FromGallery: ")
        if (customGalleryTO.sdcardPath != null) {
            val uri = Uri.parse("file://" + customGalleryTO.sdcardPath)
            uriPermission = uri;
            convertPhotoPermission(uri)
            binding.tMadrak.text = getString(R.string.show_image)

            binding.tMadrak.setTextColor(getColor(R.color.green))
        }
    }

    override fun onReturnPhotoPermission(uri: Uri?) {
        if (uri != null) {
            uriPermission = uri;
            convertPhotoPermission(uri)
            binding.tMadrak.text = getString(R.string.show_image);

            binding.tMadrak.setTextColor(getColor(R.color.green));

        }
    }

    private fun convertPhoto(uri: Uri) {
        executor.execute {
            val bitmaptest = RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);

            var base64 = ConvertImage.encodeTobase64(bitmaptest)
//            photosBase64.add(PhotoMarketsTO(counter.toString(), base64))
//            Log.i("testPhotos", "convertPhoto: ${counter}")

            counter++;

            list?.add(
                PhotoTO(
                    0,
                    uri,
                    bitmap = bitmaptest,
                    base64 = base64,
                    counter = counter,
                    ContextApp.BASE_64
                )
            )

            initOrderPhotos()

            handler.post {
                photoAdapter?.updateAdapter(list!!)
//                adapterPhoto?.notifyItemInserted(counter)
            }
        }
    }

    private fun convertPhotoPermission(uri: Uri) {
        executor.execute {
            val bitmaptest = RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);
            val base64 = ConvertImage.encodeTobase64(bitmaptest);
//            photosBase64.add(PhotoMarketsTO(counter.toString(), base64))
            Log.i("testPhotos", "convertPhoto: ${counter}")

            initOrderPhotos()

            handler.post {
                storeRequest?.madrak = base64 ;
//                binding.imgMadrak.setImageBitmap(bitmaptest)
                binding.constraintMadrak.setBackgroundResource(R.drawable.border_search_white)

            }
        }
    }


    fun initOrderPhotos() {
        var counter = 0;
        for (p in list ?: emptyList()) {
//            p.counter = if (counter>0) counter-1 else counter;
            p.counter = counter;
            list?.set(counter, p);
            counter += 1;
        }
    }


    /*Onclick permission */
    fun onClickPermission(view: View) {
//        Log.i("testPhotos", "onClickPermission: ")
        var fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.PERMISSION_PHOTO);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage.iReturnphotoFromdialog = this;
        fragmentDialogChooseImage.iReturnPhotoPermission = this;

        fragmentDialogChooseImage.show(supportFragmentManager, "dialogAddMarket");


    }

    /*Onclick City */
    fun onClickCity(view: View) {
        val intent = Intent(this, LocationChooseActivity::class.java);
//        val intent = Intent(this, AddressStoreActivity::class.java);
        intent.putExtra(ContextApp.STORE, true);
//        intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)

        activityResultLauncherGeneral.launch(intent)

    }

    /*Onclick Apply */
    open fun onClickApply(view: View) {
        for (a in transferStore?.listCategorySelected?: arrayListOf()){
            Log.i("categoryStoreResult", " : $a")

        }
        createStore()
    }

    /*Onclick ShowImagePermission */
    open fun onClickShowImagePermission(view: View) {

        if (uriPermission!=null) {
            val showImageDialogPermission = DialogFragmentShowImage().newInstance(uriPermission,null);
            showImageDialogPermission.show(supportFragmentManager, "permission");
        }
    }


    private var activityResultLauncherGeneral =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                binding.tAddress.text = result.data?.getStringExtra(ContextApp.CITY);
                storeRequest?.cityID = result.data?.getStringExtra(ContextApp.CITY_ID)

                storeRequest?.latitude = result.data?.getStringExtra(ContextApp.LATITUDE)
                storeRequest?.longitude = result.data?.getStringExtra(ContextApp.LONGITUDE)

                binding.constraintAddress.setBackgroundResource(R.drawable.border_search_white)


            } else if (result.resultCode == ContextApp.FINISH) {
                setResult(ContextApp.FINISH)
                finish()
            }
        }

    internal var uriPermission:Uri?=null;



}