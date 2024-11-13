package com.pedpo.pedporent.view.addMarket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityAddMarketBinding
import com.pedpo.pedporent.databinding.AdapterPhotoBinding
import com.pedpo.pedporent.listener.ClickAdapterAlbum
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.market.AdMarketTO
import com.pedpo.pedporent.model.market.PhotoMarketsTO
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.model.transfer.TransferDataMarket
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.view.adapter.PhotoAdapter
import com.pedpo.pedporent.view.adapter.PhotoUnderAdapter
import com.pedpo.pedporent.view.dialog.FragmentDialogChooseImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.AdMarketViewModel
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
open class AddMarketActivity : AppCompatActivity(), ClickAdapterAlbum, IReturnPhoto_FromDialog {


    @Inject
    open lateinit var photoAdapter: PhotoAdapter;

    @Inject
    lateinit var showProgressBar: ShowProgressBar

    @Inject
    lateinit var photoUnderAdapter: PhotoUnderAdapter;
    var list: ArrayList<PhotoTO>? = null;
    var listDelete: ArrayList<String>? = null;
    private var listBottom: ArrayList<PhotoTO>? = null;

//    internal var lat: String? = null;
//    internal var lng: String? = null;

    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    var counter: Int = 0;

    var typeMarket: String? = null;

    private val viewModel: AdMarketViewModel by viewModels();
    var transferDataMarket = TransferDataMarket();
    var typeOfguaranteeTO = TypeOfguaranteeTO();
    var photosBase64 = ArrayList<PhotoMarketsTO>();

    lateinit var binding: ActivityAddMarketBinding;

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        when (currentNightMode) {
//            Configuration.UI_MODE_NIGHT_NO -> {
//            } // Night mode is not active, we're using the light theme
//            Configuration.UI_MODE_NIGHT_YES -> {
//            } // Night mode is active, we're using dark theme
//        }
//    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarketBinding.inflate(layoutInflater)
        binding.listener = this ;
        binding.lifecycleOwner = this ;
        setContentView(binding.root)

        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET) ?: "";
        when (typeMarket) {
            ContextApp.RENT -> binding?.tLabelBar.text =
                "${getString(R.string.rent)} - ${getString(R.string.ad_registration)}"
            ContextApp.SALE -> binding?.tLabelBar.text =
                "${getString(R.string.sell)} - ${getString(R.string.ad_registration)}"
            else -> binding?.tLabelBar.text =
                "${getString(R.string.service)} - ${getString(R.string.ad_registration)}"
        }

        photoAdapter.clickAdapterAlbum = this;
        transferDataMarket = intent?.getParcelableExtra<TransferDataMarket>(ContextApp.TRANFER_DATA_MARKET) ?: TransferDataMarket() ;

//        transferDataMarket.categoryName = intent?.getStringExtra(ContextApp.CATEGORY_NAME) ?: "";
//        transferDataMarket.categoryID = intent?.getStringExtra(ContextApp.CATEGORY_ID) ?: "";
//        transferDataMarket.subCategroyID =
//            intent?.getStringExtra(ContextApp.SUB_CATEGORY_ID) ?: "";

        initIntent()

        designChoosePhotos()


        touchEdiTest(tDesc = binding.eDescription);
        touchEdiTest(tDesc = binding.eAgrrement);
        focusEditText(binding.eTitle, binding.inputTitle, binding.consErrorTitle);
        focusEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);
        focusEditText(binding.eAgrrement, binding.inputAgrrement, null);
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
        photoUnderAdapter?.updateAdapter(listBottom ?: arrayListOf());


        binding.recyclerPhoto.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerPhoto.adapter = photoAdapter;
        listDelete = ArrayList<String>();
        list = ArrayList<PhotoTO>();
        list?.add(PhotoTO(R.drawable.ic_baseline_photo_camera_48, null, null, null, 0, ""))
        //        list?.add(PhotoTO(R.drawable.image_test1, null, null, null, 1))
//        list?.add(PhotoTO(R.drawable.image_test2, null, null, null, 2))
//        list?.add(PhotoTO(R.drawable.image_test3, null, null, null, 2))
        photoAdapter?.updateAdapter(list!!)

        val itemAnimator: ItemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 100;
        itemAnimator.removeDuration = 100;
        itemAnimator.moveDuration = 300;
//        itemAnimator.changeDuration = 500;
        binding.recyclerPhoto.itemAnimator = itemAnimator;
    }

    fun initIntent() {

        typeOfguaranteeTO =
            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
                ?: TypeOfguaranteeTO()
        if (intent.getSerializableExtra(ContextApp.TRANFER_DATA_MARKET) != null)
            transferDataMarket =
                intent.getSerializableExtra(ContextApp.TRANFER_DATA_MARKET) as TransferDataMarket
        binding.nameCategory.text = transferDataMarket.categoryName;
        Log.i(
            "adMarketData", "${binding.nameCategory.text} \n" +
                    "${transferDataMarket.cityName} \t\n" +
                    "${transferDataMarket.cityID} \t\n" +
                    "${transferDataMarket.latitude} \t\n" +
                    "${transferDataMarket.longitude} \t\n\n" +

                    "${transferDataMarket.commodityPrice} \t\n" +
                    "${transferDataMarket.priceDayli} \t\n" +
                    "${transferDataMarket.hourlyRentalPrice} \t\n" +
                    "${transferDataMarket.monthlyRentalPrice} \t\n" +
                    "${transferDataMarket.yearlyRentPrice} \t\n\n" +

                    "${transferDataMarket.meterOfHouse} \t\n" +
                    "${transferDataMarket.bathrooms} \t\n" +
                    "${transferDataMarket.rooms} \t\n" +
                    "${transferDataMarket.yearOfHouse} \t\n" +
                    "${transferDataMarket.parkingID} \t\n" +
                    "${transferDataMarket.heatingID} \t\n" +
                    "${transferDataMarket.coolingID} \t\n\n" +

                    "${transferDataMarket.fuelType} \t\n" +
                    "${transferDataMarket.yearOfCar} \t\n" +
                    "${transferDataMarket.kilometerOfCar} \t\n\n" +
                    "${transferDataMarket.showType} \t\n\n" +

                    "assignment :  ${transferDataMarket.assignment} \t\n\n" +

                    "${transferDataMarket.categoryName} \t\n" +
                    "type ${typeMarket} \t\n" +
                    "categoryMarketID ${transferDataMarket.categoryID} \t\n" +
                    "subCategoryMarketID ${transferDataMarket.subCategroyID} \t"
        )
        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.nationalCard}")
        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.identityCard}")
        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.promissoryNote}")
        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.check}")
        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.other}")
    }

    private fun countPhoto_Full(): Boolean {
        return list?.size?:0 >= 6;
    }

    fun touchEdiTest(tDesc: TextInputEditText) {
        tDesc.setOnTouchListener(OnTouchListener { v, event ->
            if (tDesc.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })
    }

    /**Interface **/
    override fun onItemClickChoosePhoto(photoTO: PhotoTO, position: Int) {
        binding.consErrorAdImage.visibility = GONE ;
        if (photoTO.counter == 0) {
            if (countPhoto_Full()) {
                return;
            }

            var fragmentDialogChooseImage =
                FragmentDialogChooseImage().newInstance(list?.size ?: 0, ContextApp.MULTI);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
            fragmentDialogChooseImage?.iReturnphotoFromdialog = this;

            fragmentDialogChooseImage?.show(supportFragmentManager, "dialogAddMarket");
        }
    }

    /**Interface
     * 1 : delete item selected
     * 2 : add item selected in first List
     * 3 : Run animation Fade_in , chon jabejayi text (Base Photo)
     * nabayad dide shavad
     * 4 : vase inke be sorat animation item jabeja shavad ,
     * ebteda item ra remove sepas homon ro insert mikonim
     * 5 : Run Delay(notifiDataSetChanged) , beacuse : index Adapter refresh shavad
     * 6 : moratab kardan position list
     * **/
    override fun setOrginalPhotoInAlbums_Adapter(
        photoTO: PhotoTO,
        position: Int,
        viewHolder: AdapterPhotoBinding,
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

//        var counter = 0;
//        for (p in list ?: emptyList()) {
////            p.counter = if (counter>0) counter-1 else counter;
//            p.counter = counter;
//            list?.set(counter, p);
//            counter += 1;
//            Log.i("testCounter", "setOrginalPhotoInAlbums_Adapter: " + p.counter)
//        }
        initOrderPhotos()

    }

    /**Interface
     *
     * **/
    override fun onDeletePhotoFromAlbums_Adapter(
        photoTO: PhotoTO,
        position: Int,
        viewHolder: AdapterPhotoBinding,
    ) {
//        Log.e("testDelete", "onDeletePhotoFromAlbums: $position")
        if (photoTO?.type.equals(ContextApp.URL)) {
            listDelete?.add(photoTO.photoUri.toString())
        }
        list?.remove(photoTO)

//        if (position == 1) {
//            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//            viewHolder.constraintCamera?.startAnimation(animationFadeIn)
//        }

        photoAdapter?.notifyItemRemoved(position)

        Handler(Looper.getMainLooper()).postDelayed({
            photoAdapter?.notifyDataSetChanged()
        }, 390)

        initOrderPhotos()

    }

    /**Interface **/
    override fun onReturnPhoto_FromCamera(uri: Uri) {
        if (!countPhoto_Full()) {
            convertPhoto(uri)
        }
    }

    /**Interface **/
    override fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>) {
        for (gallery in arrayList) {
            val uri = Uri.parse("file://" + gallery.sdcardPath)
            if (!countPhoto_Full()) {
                convertPhoto(uri)
            }
//            Log.i("chooseImage", "gallery size : " + list?.size)
        }
    }

    override fun onReturnSinglePhoto_FromGallery(customGalleryTO: CustomGalleryTO) {

    }

    private fun convertPhoto(uri: Uri) {
        executor.execute {
            val bitmaptest = RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);

            var base64 = ConvertImage.encodeTobase64(bitmaptest)
//            photosBase64.add(PhotoMarketsTO(counter.toString(), base64))
            Log.i("testPhotos", "convertPhoto: ${counter}")

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

    fun initOrderPhotos(){
        var counter = 0;
        for (p in list?: emptyList()) {
//            p.counter = if (counter>0) counter-1 else counter;
            p.counter = counter;
            list?.set(counter, p);
            counter += 1;
        }
    }


    /* OnClick */
    open fun onClickCategory(view: View) {
        binding.consErrorCategory.visibility = GONE
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)
        activityResultLauncherAddMarket.launch(intent)
//        activityResultLauncherAddMarket.launch(Intent(this, CategoryActivityOld::class.java))
    }

    /* OnClick */
    fun onClickIcClose(view: View) {
        finish()
    }



    private fun focusEditText(
        editText: TextInputEditText,
        inputLayout: TextInputLayout,
        constraint: ConstraintLayout?
    ) {
        editText.setOnFocusChangeListener { _, b ->
            when (b) {
                true -> {
                    val myList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_focused)),
                        intArrayOf(ContextCompat.getColor(this, R.color.gray_focus_edittext))
                    )
                    inputLayout.setBoxStrokeColorStateList(myList)
                    inputLayout.boxStrokeColor =
                        ContextCompat.getColor(this, R.color.gray_focus_edittext)

                    constraint?.visibility = GONE
                }
            }
        }
    }


    fun errorEditText(
        inputEditText: TextInputEditText,
        inputLayout: TextInputLayout,
        constraint: ConstraintLayout?
    ): Boolean {
        var bool = if (inputEditText.text.isNullOrEmpty()) {
            inputLayout.setBoxStrokeColorStateList(
                ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_focused)),
                    intArrayOf(Color.RED)
                )
            )
            inputLayout.clearFocus()
            constraint?.visibility = VISIBLE
            true;
        } else
            false;
//      binding.inputTitle.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this,R.color.selector_input)!!)
//      binding.inputTitle.boxStrokeColor = ContextCompat.getColor(this, R.color.gray_focus_edittext)
        return bool;
    }


    /* Onclick */
    open fun onClickContinue(view: View) {
//        Log.i("testAddMarkt", "onSuccess 2 : ${transferDataMarket.categoryID}" )

        if (transferDataMarket.categoryID.isNullOrEmpty()) {
            binding.consErrorCategory.visibility = VISIBLE;
            binding.consErrorCategory.requestFocus();
            binding.nestedScroll.scrollTo(0, 0);
        }

        photosBase64.clear();
        for (index in list?.indices!!) {
            if (index == 0)
                continue;
            photosBase64.add(
                PhotoMarketsTO(
                    order = (list?.get(index)?.counter!! - 1),
                    photo = list?.get(index)?.base64!!
                )
            );
        }
        if (photosBase64.isEmpty()) {
            binding.consErrorAdImage.visibility = VISIBLE
            binding.nestedScroll.scrollTo(0, 0)
        }

        var boolTitle = errorEditText(binding.eTitle, binding.inputTitle, binding.consErrorTitle);
        var boolDes =
            errorEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);

        if (boolTitle || boolDes || transferDataMarket.categoryID.isNullOrEmpty() || photosBase64.isEmpty())
            return;


        var marketTO = AdMarketTO();
        marketTO.title = binding.eTitle.text.toString().trim() ?: "";

        marketTO.description = binding.eDescription.text.toString().trim() ?: "";

        marketTO.agreement = binding.eAgrrement.text.toString().trim() ?: "";

        marketTO.latitude = transferDataMarket.latitude;
        marketTO.longitude = transferDataMarket.longitude;
        marketTO.cityID = transferDataMarket.cityID;
        marketTO.categoryMarketID = transferDataMarket.categoryID
        marketTO.subCategoryMarketID = transferDataMarket.subCategroyID
//        marketTO.commodityPrice = if (transferDataMarket.realValuePrice.isNullOrEmpty()) "0" else transferDataMarket.realValuePrice;
        marketTO.commodityPrice = if (transferDataMarket.commodityPrice.isNullOrEmpty()) 0 else
            transferDataMarket?.commodityPrice?.toLong();

        if (typeMarket == ContextApp.SALE)
            marketTO.typeOfguaranteeTO = TypeOfguaranteeTO()
        else
            marketTO.typeOfguaranteeTO = typeOfguaranteeTO ?: TypeOfguaranteeTO();
//        Log.i("testDetails", "onSuccess: " +
//                "${marketTO.typeOfguaranteeTO?.check} \n" +
//                "${marketTO.typeOfguaranteeTO?.promissoryNote} \n" +
//                "${marketTO.typeOfguaranteeTO?.identityCard} \n" +
//                "${marketTO.typeOfguaranteeTO?.nationalCard} \n" +
//                "${marketTO.typeOfguaranteeTO?.other} \n" +
//                "")

        when (transferDataMarket.assignment) {
            ContextApp.AN_AGREEMENT -> marketTO.priceAgree = true;
            ContextApp.FREE -> marketTO.free = true;
            else -> {
                marketTO.priceAgree = false;
                marketTO.free = false;
            }
        }

//        marketTO.rentPrice = if (transferDataMarket.priceDayli.isNullOrEmpty()) "0" else transferDataMarket.priceDayli;
        marketTO.showType = transferDataMarket.showType;
        marketTO.type = typeMarket;
        marketTO.salePrice = transferDataMarket.salePrice;
        if (!transferDataMarket.priceDayli?.trim().isNullOrEmpty())
            marketTO.rentPrice = transferDataMarket.priceDayli?.toInt();
//        if (!transferDataMarket.hourlyRentalPrice?.trim().isNullOrEmpty())
            marketTO.hourlyRentalPrice = transferDataMarket.hourlyRentalPrice?.toInt();
//        if (!transferDataMarket.monthlyRentalPrice?.trim().isNullOrEmpty())
            marketTO.monthlyRentalPrice = transferDataMarket.monthlyRentalPrice?.toInt();
//        if (!transferDataMarket.yearlyRentPrice?.trim().isNullOrEmpty())
            marketTO.yearlyRentPrice = transferDataMarket.yearlyRentPrice?.toInt();


        if (!transferDataMarket.parkingID?.trim().isNullOrEmpty())
            marketTO.parkingTypeID = transferDataMarket.parkingID?.toInt();

        if (!transferDataMarket.heatingID?.trim().isNullOrEmpty())
            marketTO.heatingID = transferDataMarket.heatingID?.toInt();
        if (!transferDataMarket.coolingID?.trim().isNullOrEmpty())
            marketTO.coolingID = transferDataMarket.coolingID?.toInt();
//        if (!transferDataMarket.meterOfHouse?.trim().isNullOrEmpty())
            marketTO.meterOfHouse = transferDataMarket.meterOfHouse?.toInt();
//        if (!transferDataMarket.rooms?.trim().isNullOrEmpty())
            marketTO.rooms = transferDataMarket.rooms?.toInt();
//        if (!transferDataMarket.bathrooms?.trim().isNullOrEmpty())
            marketTO.bathrooms = transferDataMarket.bathrooms?.toInt();
        if (!transferDataMarket.yearOfHouse?.trim().isNullOrEmpty())
            marketTO.yearOfHouse = transferDataMarket.yearOfHouse?.toInt();

//        if (!transferDataMarket.kilometerOfCar?.trim().isNullOrEmpty())
            marketTO.kilometerOfCar = transferDataMarket.kilometerOfCar?.toInt();
        if (!transferDataMarket.fuelType?.trim().isNullOrEmpty())
            marketTO.fuelType = transferDataMarket.fuelType;
        if (!transferDataMarket.yearOfCar?.trim().isNullOrEmpty())
            marketTO.yearOfCar = transferDataMarket.yearOfCar?.toInt();




        marketTO.listPhotos = photosBase64 ;

        adMarket(marketTO)
    }

    fun adMarket(adMarketTO: AdMarketTO) {
        Log.i("adMarket", "adMarketMethod : " )

        showProgressBar.show(supportFragmentManager)

        var observer: LiveData<DataWrapper<ResponseTO>>? = null;
        observer = if (typeMarket == ContextApp.SALE || typeMarket == ContextApp.RENT)
            viewModel.adMarket(adMarketTO)
        else {
            //            adMarketTO.categoryMarketID = "1db395f8-788b-4527-93fd-05088b5a3b10";
            viewModel.adService(adMarketTO)
        }



        observer
            .observe(this, CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    Log.i("adMarket", "adMarketM : " + dataInput.isSuccess)


                    if (dataInput.isSuccess == true) {
                        Toast.makeText(
                            this@AddMarketActivity,
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(ContextApp.FINISH)
                        finish();
                    } else {
                        Toast.makeText(
                            this@AddMarketActivity,
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onException(exception: Exception) {
                    Log.e("adMarket", "onExceptionMM : " + exception.message)
                    showProgressBar.dismiss()
                }

            }))
    }

//    var assignment: String? = null;
//    var price: String? = null;
//    var realValuePrice: String? = null;
//    var city: String? = null;
//    var cityID: String? = null;
//    var categoryID: String? = null;
//    var subCategoryID: String? = null;

    /**Loucher Add Market **/
    var activityResultLauncherAddMarket =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {

                }
                ContextApp.RESULT_Ad -> {
//                    city = result.data?.getStringExtra(ContextApp.CITY);
//                    cityID = result.data?.getStringExtra(ContextApp.CITY_ID);
                    transferDataMarket.latitude = result.data?.getStringExtra(ContextApp.LATITUDE);
                    transferDataMarket.longitude = result.data?.getStringExtra(ContextApp.LONGITUDE);
//                    categoryID = result.data?.getStringExtra(ContextApp.CATEGORY_ID);
//                    subCategoryID = result.data?.getStringExtra(ContextApp.SUB_CATEGORY_ID);
//                    price = result.data?.getStringExtra(ContextApp.PRICE_DAILY);
//                    assignment = result.data?.getStringExtra(ContextApp.ASSIGNMENT);
//                    realValuePrice = result.data?.getStringExtra(ContextApp.REAL_VALUE);
                    typeOfguaranteeTO =
                        result.data?.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
                            ?: TypeOfguaranteeTO()
                    transferDataMarket =
                        result.data?.getSerializableExtra(ContextApp.TRANFER_DATA_MARKET) as TransferDataMarket
                    binding.nameCategory.text = transferDataMarket.categoryName;
                    Log.i(
                        "adMarketData", "${binding.nameCategory.text} \n" +
                                "${transferDataMarket.cityName} \t\n" +
                                "${transferDataMarket.cityID} \t\n" +
                                "${transferDataMarket.latitude} \t\n" +
                                "${transferDataMarket.longitude} \t\n\n" +

                                "${transferDataMarket.commodityPrice} \t\n" +
                                "${transferDataMarket.priceDayli} \t\n" +
                                "${transferDataMarket.hourlyRentalPrice} \t\n" +
                                "${transferDataMarket.monthlyRentalPrice} \t\n" +
                                "${transferDataMarket.yearlyRentPrice} \t\n\n" +

                                "${transferDataMarket.meterOfHouse} \t\n" +
                                "${transferDataMarket.bathrooms} \t\n" +
                                "${transferDataMarket.rooms} \t\n" +
                                "${transferDataMarket.yearOfHouse} \t\n" +
                                "${transferDataMarket.parkingID} \t\n" +
                                "${transferDataMarket.heatingID} \t\n" +
                                "${transferDataMarket.coolingID} \t\n\n" +

                                "${transferDataMarket.fuelType} \t\n" +
                                "${transferDataMarket.yearOfCar} \t\n" +
                                "${transferDataMarket.kilometerOfCar} \t\n\n" +

                                "assignment :  ${transferDataMarket.assignment} \t\n\n" +

                                "${transferDataMarket.categoryName} \t\n" +
                                "${transferDataMarket.categoryID} \t\n" +
                                "${transferDataMarket.subCategroyID} \t"
                    )
                    Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.nationalCard}")
                    Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.identityCard}")
                    Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.promissoryNote}")
                    Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.check}")
                    Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.other}")

                }
                ContextApp.FINISH -> {
                    finish()
                }
            }
        }

}