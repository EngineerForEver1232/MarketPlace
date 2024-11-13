package com.pedpo.pedporent.view.store.edit

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityEditStorePhotosBinding
import com.pedpo.pedporent.databinding.AdapterPhotoBinding
import com.pedpo.pedporent.listener.ClickAdapterAlbum
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.ResponseStorePhotos
import com.pedpo.pedporent.model.store.StorePhotoslEditRequest
import com.pedpo.pedporent.model.store.StoreRequestEdit
import com.pedpo.pedporent.model.store.StoreTO
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.edit.Logo
import com.pedpo.pedporent.model.store.edit.MadrakStore
import com.pedpo.pedporent.model.store.edit.PhotoStore
import com.pedpo.pedporent.model.store.edit.ResponseEditStorePhotos
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PhotoAdapter
import com.pedpo.pedporent.view.adapter.PhotoUnderAdapter
import com.pedpo.pedporent.view.dialog.DialogFragmentShowImage
import com.pedpo.pedporent.view.dialog.FragmentDialogChooseImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.addStore.StoreAddActivitySecond
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class EditStorePhotosActivity : AppCompatActivity() , IReturnPhoto_FromDialog , ClickAdapterAlbum {

    var storeTO: StoreTO? = null;
    lateinit var binding : ActivityEditStorePhotosBinding;
    val storeViewModel: ProfileViewModel by viewModels();

    var editRequest: StorePhotoslEditRequest? = null;
    var finalPhotos = ArrayList<PhotoStore>();
    var list: ArrayList<PhotoTO>? = null;
    var listDelete: ArrayList<String>? = null;
    internal var uriPermission:Uri?=null;

    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    var counter: Int = 0;

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

    private var listBottom: ArrayList<PhotoTO>? = null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStorePhotosBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root);
        setToolbar();
        editRequest = StorePhotoslEditRequest();
        finalPhotos = arrayListOf();
        list = arrayListOf();
        photoAdapter.clickAdapterAlbum = this;
        designChoosePhotos();


        getPhotos()

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
        photoAdapter.updateAdapter(list?: arrayListOf())

        val itemAnimator: RecyclerView.ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 100;
        itemAnimator.removeDuration = 100;
        itemAnimator.moveDuration = 300;
//        itemAnimator.changeDuration = 500;
        binding.recyclerPhoto.itemAnimator = itemAnimator;
    }


    fun setEditPhotos(){


        storeViewModel.setEditStorePhotos(request = editRequest?:StorePhotoslEditRequest())?.observe(this,
        CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
            override fun onSuccess(dataInput: ResponseTO) {

                if (dataInput.isSuccess == true){
                    Toast.makeText(this@EditStorePhotosActivity , dataInput.message , Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@EditStorePhotosActivity , dataInput.message , Toast.LENGTH_SHORT).show()
                }

            }

            override fun onException(exception: Exception) {

            }

        }))
    }

    /*Onclick */
    fun onClickAvatar(view: View) {

        typeCamera = ContextApp.IMAGE_LOGO

        val fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.SINGLE);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage.iReturnphotoFromdialog = this;

        fragmentDialogChooseImage.show(supportFragmentManager, "dialogAddMarket");

    }

    fun getPhotos() {
        showProgressBar.show(supportFragmentManager)
        storeViewModel.getEditStorePhotos()?.observe(this ,
            CustomObserver(object : CustomObserver.ResultListener<ResponseEditStorePhotos> {
                override fun onSuccess(dataInput: ResponseEditStorePhotos) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {

                        for (p in dataInput.data?.images ?: emptyList()) {
                            val photo = PhotoTO();
                            photo.photoUri = Uri.parse(p.image);
                            photo.counter = p.order?.plus(1);
                            photo.type = ContextApp.URL ;
                            list?.add(photo);
                        }
                        Log.i("testEditStore", "getPhotos onSuccess: ${storeTO?.logo}")

                        photoAdapter.updateAdapter(list?: arrayListOf())

                        Picasso.get().load(dataInput.data?.logo).into(binding.imgProfile);
                        Picasso.get().load(dataInput.data?.madrak).into(binding.imgPermission);


                    } else {

                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()

                }

            })
        )
    }


    override fun onItemClickChoosePhoto(photoTO: PhotoTO, position: Int) {
        if (photoTO.counter == 0) {
            if (countPhoto_Full()) {
                return;
            }

            typeCamera = ContextApp.MULTI;

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
        viewHolder.constraintCamera?.startAnimation(animationFadeIn)

        photoAdapter?.notifyItemRemoved(position)
        photoAdapter?.notifyItemInserted(1)

        Handler(Looper.getMainLooper()).postDelayed({
            photoAdapter?.notifyDataSetChanged()
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

    fun initOrderPhotos() {
        var counter = 0;
        for (p in list ?: emptyList()) {
//            p.counter = if (counter>0) counter-1 else counter;
            p.counter = counter;
            list?.set(counter, p);
            counter += 1;
        }
    }

    private fun countPhoto_Full(): Boolean {
        return (list?.size ?: 0) >= 6;
    }

    override fun onReturnPhoto_FromCamera(uri: Uri) {
        binding.icErrorimage.visibility = View.GONE;
        binding.tCatErrorimage.visibility = View.GONE;

        if (typeCamera == ContextApp.MULTI){
        if (!countPhoto_Full()) {
            convertPhotoMultiImage(uri = uri)
            }
        }else
            convertPhoto(uri)

    }

    override fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>) {
        binding.tCatErrorimage.visibility = View.GONE;
        binding.icErrorimage.visibility = View.GONE;

        for (gallery in arrayList) {
            val uri = Uri.parse("file://" + gallery.sdcardPath)
            if (!countPhoto_Full()) {
                convertPhotoMultiImage(uri = uri)
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


    private fun convertPhotoMultiImage(uri: Uri) {
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
                photoAdapter.updateAdapter(list!!)
//                adapterPhoto?.notifyItemInserted(counter)
            }
        }
    }


    private fun convertPhoto(uri: Uri) {
        executor.execute {

            val bitmaptest =
                RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);

            val base64 = ConvertImage.encodeTobase64(bitmaptest)

            handler.post {
                if (typeCamera == ContextApp.LOGO) {
                    binding.imgProfile.setImageBitmap(bitmaptest)
                    val logo = Logo();
                    logo.type = ContextApp.BASE_64;
                    logo.photo = base64;
                    editRequest?.logo = logo
                } else {
                    binding.imgPermission.setImageBitmap(bitmaptest)
                    val madrak = MadrakStore();
                    madrak.photo = base64 ;
                    madrak.type = ContextApp.BASE_64;
                    editRequest?.madrak = madrak;
                }



            }



        }
    }



    /*Onclick City */
    fun onClickApply(view: View) {


//        if (storeRequest?.madrak?.isNullOrEmpty() == true)
//            return


//        if (binding?.eEmail?.text.toString().isEmpty()){
//            binding?.inputEmail.error = getString(R.string.enter_this_item)
//            binding?.inputEmail.boxStrokeErrorColor =
//                ColorStateList(arrayOf(intArrayOf(android.R.attr.state_focused)),
//                intArrayOf(Color.RED)
//            )
//        }
//        utills?.errorEditText(binding.eEmail, binding.inputTitle, binding.consErrorTitle);
//
//        utills?.errorEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);


        if (list?.isEmpty() == true) {
            binding.tCatErrorimage.isVisible = true;
            binding.icErrorimage.isVisible = true;
            binding.nestedScroll.scrollTo(0, 0)
            return;
        }

        for (index in list?.indices ?: emptyList()) {
            if (index == 0)
                continue;
            finalPhotos.add(
                PhotoStore(
                    type = list?.get(index)?.type,
                    order = (list?.get(index)?.counter?.minus(1)),
                    photo = list?.get(index)?.base64 ?: list?.get(index)?.photoUri.toString()
                )
            );
        }

        editRequest?.photo = finalPhotos ;

        editRequest?.deleteURL = listDelete ;
        setEditPhotos()
    }

    var typeCamera:String?=null;

    /*Onclick ShowImagePermission */
    fun onClickShowImagePermission(view: View) {

        typeCamera = ContextApp.IMAGE_MADRAK

        val fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.SINGLE);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage.iReturnphotoFromdialog = this;

        fragmentDialogChooseImage.show(supportFragmentManager, "dialogAddMarket");

    }

}