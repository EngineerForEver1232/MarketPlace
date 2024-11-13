package com.pedpo.pedporent.view.editMarket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.market.*
import com.pedpo.pedporent.model.market.editMarket.*
import com.pedpo.pedporent.model.transfer.TransferDataMarket
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.view.addMarket.AddMarketActivity
import com.pedpo.pedporent.view.addMarket.CategoryActivity
import com.pedpo.pedporent.viewModel.AdMarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMarketActivity : AddMarketActivity() {

    private val viewModel: AdMarketViewModel by viewModels();
    private var market: EditMarketTO? = null;
    private var marketID = "";
    private var typeApi = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        marketID = intent.getStringExtra(ContextApp.MARKET_ID).toString();

        typeMarket = intent.getStringExtra(ContextApp.TYPE_MARKET) ?: "";
        typeApi = intent.getStringExtra(ContextApp.TYPE_API) ?: "";

//        loadDataMarket()

        initData()

    }

    fun initData() {


        if (intent.getParcelableExtra<TransferDataMarket>(ContextApp.TRANFER_DATA_MARKET) != null)
            transferDataMarket =
                intent.getParcelableExtra<TransferDataMarket>(ContextApp.TRANFER_DATA_MARKET) as TransferDataMarket

        typeOfguaranteeTO =
            intent.getParcelableExtra<TypeOfguaranteeTO>(ContextApp.TYPE_OF_GUARANTEE)
                ?: TypeOfguaranteeTO();

        transferDataMarket.assignment = intent.getStringExtra("ASSIGNMENT");

        binding.eTitle.setText(transferDataMarket.title.toString())
        binding.eDescription.setText(transferDataMarket.description)
        binding.eAgrrement.setText(transferDataMarket.agreement)
        binding.nameCategory.setText(transferDataMarket.categoryName)

        var photos = intent.getParcelableArrayListExtra<PhotoEditTO>(ContextApp.PHOTOS)

        for (p in photos ?: emptyList()) {
            var photo = PhotoTO()
            photo.photoUri = Uri.parse(p.image);
            photo.counter = p.order?.plus(1);
            photo.type = ContextApp.URL;
            list?.add(photo);
        }


        photoAdapter.updateAdapter(list!!)
//        photoAdapter.updateAdapter(photos?:ArrayList())

//        Log.i(
//            "adEditMarket", "${binding.nameCategory.text} \n" +
//                    "${transferDataMarket.cityName} \t\n" +
//                    "${transferDataMarket.cityID} \t\n" +
//                    "${transferDataMarket.latitude} \t\n" +
//                    "${transferDataMarket.longitude} \t\n\n" +
//
//                    "${transferDataMarket.commodityPrice} \t\n" +
//                    "${transferDataMarket.priceDayli} \t\n" +
//                    "${transferDataMarket.hourlyRentalPrice} \t\n" +
//                    "${transferDataMarket.monthlyRentalPrice} \t\n" +
//                    "${transferDataMarket.yearlyRentPrice} \t\n\n" +
//
//                    "${transferDataMarket.meterOfHouse} \t\n" +
//                    "${transferDataMarket.bathrooms} \t\n" +
//                    "${transferDataMarket.rooms} \t\n" +
//                    "${transferDataMarket.yearOfHouse} \t\n" +
//                    "${transferDataMarket.parkingID} \t\n" +
//                    "${transferDataMarket.heatingID} \t\n" +
//                    "${transferDataMarket.coolingID} \t\n\n" +
//
//                    "${transferDataMarket.fuelType} \t\n" +
//                    "${transferDataMarket.yearOfCar} \t\n" +
//                    "${transferDataMarket.kilometerOfCar} \t\n\n" +
//
//                    "assignment :  ${transferDataMarket.assignment} \t\n\n" +
//
//                    "${transferDataMarket.categoryName} \t\n" +
//                    "${transferDataMarket.categoryID} \t\n" +
//                    "${transferDataMarket.subCategroyID} \t\n" +
//                    " marketID :  ${marketID} \t\n" +
//                    "${list?.size} \t\n"
//        )
    }


    var finalPhotos = ArrayList<PhotoMarkets>();

    /* Onclick */
    override fun onClickContinue(view: View) {
        Log.i("testEdit", "onSuccess: ${listDelete?.size}")

//        if (market?.categoryID.isNullOrEmpty())
//            binding.consErrorCategory.visibility = View.VISIBLE

        finalPhotos.clear();
        for (index in list?.indices!!) {
            if (index == 0)
                continue;
            finalPhotos.add(
                PhotoMarkets(
                    type = list?.get(index)?.type,
                    order = (list?.get(index)?.counter!! - 1),
                    photo = list?.get(index)?.base64 ?: list?.get(index)?.photoUri.toString()
//                    photo = list?.get(index)?.photoUri.toString()
                )
            );
        }

        if (finalPhotos.isEmpty())
            binding.consErrorAdImage.visibility = View.VISIBLE


        var boolTitle = errorEditText(binding.eTitle, binding.inputTitle, binding.consErrorTitle);
        var boolDes =
            errorEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);

        if (boolTitle || boolDes || binding.nameCategory.text.isNullOrEmpty() || finalPhotos.isEmpty())
            return;


        var submitMarketTO = SubmitMarketTO();
        submitMarketTO.marketID = marketID
        submitMarketTO.title = binding.eTitle.text.toString().trim() ?: "";
        submitMarketTO.description = binding.eDescription.text.toString().trim() ?: "";
        submitMarketTO.agreement = binding.eAgrrement.text.toString().trim() ?: "";
        submitMarketTO.rentPrice = transferDataMarket.priceDayli;
        submitMarketTO?.priceAgree = transferDataMarket.priceAgree
        submitMarketTO.free = transferDataMarket.free;
        submitMarketTO.commodityPrice = transferDataMarket.commodityPrice;
        submitMarketTO.cityID = transferDataMarket.cityID;
        submitMarketTO.categoryMarketID = transferDataMarket.categoryID;
        submitMarketTO.subCategoryMarketID = transferDataMarket.subCategroyID;
        submitMarketTO.typeOfguaranteeTO = typeOfguaranteeTO;
        submitMarketTO.latitude = transferDataMarket.latitude;
        submitMarketTO.longitude = transferDataMarket.longitude;
        submitMarketTO.showType = transferDataMarket.showType
        submitMarketTO.hourlyRentalPrice = transferDataMarket.hourlyRentalPrice
        submitMarketTO.monthlyRentalPrice = transferDataMarket.monthlyRentalPrice
        submitMarketTO.yearlyRentPrice = transferDataMarket.yearlyRentPrice
        submitMarketTO.type = transferDataMarket.type
        submitMarketTO.salePrice = transferDataMarket.salePrice
        submitMarketTO.meterOfHouse = transferDataMarket.meterOfHouse
        submitMarketTO.rooms = transferDataMarket.rooms
        submitMarketTO.bathrooms = transferDataMarket.bathrooms

        submitMarketTO.yearOfHouse = transferDataMarket.yearOfHouse?.toInt()
        submitMarketTO.parkingID = transferDataMarket.parkingID?.toInt()
        submitMarketTO.heatingID = transferDataMarket.heatingID?.toInt()
        submitMarketTO.coolingID = transferDataMarket.coolingID?.toInt()
        submitMarketTO.yearOfCar = transferDataMarket.yearOfCar?.toInt()


        submitMarketTO.photoMarkets = finalPhotos;
        submitMarketTO.deleteURL = listDelete;


//
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.nationalCard}")
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.identityCard}")
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.promissoryNote}")
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.check}")
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.other}")
//        Log.i("adMarket", "returnContent: ${typeOfguaranteeTO.otherText}")
//
//        Log.i("adMarket", "returnContent: ${transferDataMarket.assignment}")
//        Log.i("adMarket", "commodityPrice: ${transferDataMarket.commodityPrice}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.priceDayli}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.categoryID}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.subCategroyID}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.cityID}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.cityName}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.latitude}")
//        Log.i("adMarket", "returnContent: ${transferDataMarket.longitude}")
//        Log.i("adMarket", "marketID: ${marketID}")


        if (typeApi == ContextApp.SERVICE)
            submitService()
        else
            submitMarket(submitMarketTO)

//        adMarket(marketTO)
    }


    fun submitService(){

        var service = SubmitServiceTO();
        service.marketID = marketID

        service.title = binding.eTitle.text.toString().trim() ?: "";
        service.description = binding.eDescription.text.toString().trim() ?: "";
        service.agreement = binding.eAgrrement.text.toString().trim() ?: "";

        service?.priceAgree = transferDataMarket.priceAgree
        service.free = transferDataMarket.free;
//        Log.i("testEditService", "submitService: ${transferDataMarket.priceAgree} \r\n " +
//                "${transferDataMarket.free}")

        if (!transferDataMarket.commodityPrice.isNullOrEmpty())
        service.commodityPrice = transferDataMarket.commodityPrice?.toLong()?:0L;
        service.cityID = transferDataMarket.cityID;
        service.categoryMarketID = transferDataMarket.categoryID;

        service.subCategoryMarketID = transferDataMarket.subCategroyID;

        service.photoMarkets = finalPhotos;
        service.deleteURL = listDelete;


        if (transferDataMarket.assignment == ContextApp.AN_AGREEMENT || transferDataMarket.assignment == ContextApp.FREE) {
            transferDataMarket.priceDayli = ""
            transferDataMarket.commodityPrice = ""
            transferDataMarket.hourlyRentalPrice = 0
            transferDataMarket.monthlyRentalPrice = 0
            transferDataMarket.yearlyRentPrice = 0
        }

        submitService(service)

    }

    fun submitMarket(submitMarketTO: SubmitMarketTO) {

        showProgressBar.show(supportFragmentManager)

        viewModel.submitEditMarket(submitMarketTO)?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    Toast.makeText(this@EditMarketActivity, dataInput.message, Toast.LENGTH_SHORT)
                        .show()
                    setResult(ContextApp.FINISH)
                    finish()
                }

                override fun onException(exception: Exception) {
//                    Log.e("adEditMarket", "onException: ${exception.message}")
                    showProgressBar.dismiss()
                    Toast.makeText(
                        this@EditMarketActivity,
                        exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
        )

    }

    fun submitService(submitMarketTO: SubmitServiceTO) {

        showProgressBar.show(supportFragmentManager)

        viewModel.submitEditService(submitMarketTO)?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    Toast.makeText(this@EditMarketActivity, dataInput.message, Toast.LENGTH_SHORT)
                        .show()
                    setResult(ContextApp.FINISH)
                    finish()
                }

                override fun onException(exception: Exception) {
                    Log.e("adEditMarket", "onException: ${exception.message}")
                    showProgressBar.dismiss()
                    Toast.makeText(
                        this@EditMarketActivity,
                        exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
        )

    }


    /* OnClick */
    override fun onClickCategory(view: View) {
        binding.consErrorCategory.visibility = View.GONE;
        var intent = Intent(this, CategoryActivity::class.java);
        intent.putExtra(ContextApp.MARKET, market);
        intent.putExtra(ContextApp.TYPE_OF_GUARANTEE, market?.typeOfguaranteeTO);
        activityResultLauncherAddMarket.launch(intent);

    }


}