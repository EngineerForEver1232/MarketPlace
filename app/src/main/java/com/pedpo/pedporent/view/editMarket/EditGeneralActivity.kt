package com.pedpo.pedporent.view.editMarket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.pedpo.pedporent.R
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.model.market.EditMarketGetData
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.model.transfer.TransferDataMarket
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.view.addMarket.AddMarketActivity
import com.pedpo.pedporent.view.addMarket.GeneralSpecificationActivity
import com.pedpo.pedporent.viewModel.AdMarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditGeneralActivity : GeneralSpecificationActivity() {

    private var marketID: String? = null;
    private var typeApi = "";
    private val viewModel: AdMarketViewModel by viewModels();
    var list: ArrayList<PhotoTO>? = null;
    private var market: TransferDataMarket? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        marketID = intent.getStringExtra(ContextApp.MARKET_ID) ?: "";
        typeApi = intent.getStringExtra(ContextApp.TYPE_API) ?: "";

        if (typeApi == ContextApp.MARKET)
            loadDataMarket()
        else
            loadDataService()
    }

    fun loadDataMarket() {
        showProgressBar.show(supportFragmentManager)

        viewModel.getEditMarket(marketID ?: "")?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<EditMarketGetData> {
                override fun onSuccess(dataInput: EditMarketGetData) {
                    showProgressBar.dismiss()
                    Log.i(
                        "testEdit",
                        "onSuccess: ${dataInput.isSuccess}  " +
                                "${dataInput.data?.cityID}"
                    )
                    initMarket(dataInput.data)
                    market = dataInput.data;
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                    Log.e("testEdit", "onException: ${exception.message}")
                }
            })
        )
    }

    fun loadDataService() {

        showProgressBar.show(supportFragmentManager)

        viewModel.getEditService(serviceID = marketID ?: "")?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<EditMarketGetData> {
                override fun onSuccess(dataInput: EditMarketGetData) {
                    showProgressBar.dismiss()
                    Log.i(
                        "testEdit",
                        "onSuccess: ${dataInput.isSuccess}  " +
                                "${dataInput.data?.cityID}"
                    )
                    initMarket(dataInput.data)
                    market = dataInput.data;
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                    Log.e("testEdit", "onException: ${exception.message}")
                }
            })
        )
    }

    fun initSpinnerDeposit(typeOfguaranteeTO: TypeOfguaranteeTO?) {
        var arr = arrayListOf<String>()
        if (typeOfguaranteeTO?.nationalCard == true) {
            arr.add(getString(R.string.national_id_card))
        }
        if (typeOfguaranteeTO?.identityCard == true) {
            arr.add(getString(R.string.certificate))
        }
        if (typeOfguaranteeTO?.promissoryNote == true) {
            arr.add(getString(R.string.promissory_note))
        }
        if (typeOfguaranteeTO?.check == true) {
            arr.add(getString(R.string.check))
        }
        if (typeOfguaranteeTO?.other == true) {
            arr.add(typeOfguaranteeTO.otherText.toString())
        }

        var adapterFilter =
            ArrayAdapter(this@EditGeneralActivity, R.layout.spinner_item_renteral, arr)
        binding.spinnerRenteral.adapter = adapterFilter;

        binding.spinnerRenteral.isVisible = true;

        binding.selectCessionType.visibility = View.INVISIBLE;

        binding.spinnerRenteral.visibility = View.VISIBLE;
        binding.lineStrokeDeposit.visibility = View.INVISIBLE;

    }

    fun initMarket(market: TransferDataMarket?) {

        transferDataMarket = market ?: TransferDataMarket();

        if (market?.type == ContextApp.RENT)
            typeMarket = ContextApp.RENT ?: ""
        else if (market?.type == ContextApp.SALE)
            typeMarket = ContextApp.SALE ?: ""



        initSpinnerDeposit(market?.typeOfguaranteeTO)


        transferDataMarket.photos = market?.photos ;
        transferDataMarket.description = market?.description ;
        transferDataMarket.agreement = market?.agreement ;

        binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT
        binding.linearPriceRent.isVisible = typeMarket == ContextApp.RENT

        if (prefApp.getNameCountry() != "Iran" && prefApp.getNameCountry() != "ایران")
            binding.layoutDeposit.isVisible = false;


        isHome = market?.isHome == true
        binding.linearHouse.isVisible = isHome
        binding.lineHouse.isVisible = isHome

        isCar = market?.isCar == true
        binding.linearCar.isVisible = isCar
        binding.lineCar.isVisible = isCar

        when (typeMarket) {
            ContextApp.RENT -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.rent)} - ${getString(R.string.ad_registration)}"
            ContextApp.SALE -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.sell)} - ${getString(R.string.ad_registration)}"
            else -> binding.actionBar.tLabelBar.text =
                "${getString(R.string.service)} - ${getString(R.string.ad_registration)}"
        }



        binding.labelCity.text = market?.cityName.toString()
        binding.hintArea.isVisible = market?.cityName.isNullOrEmpty() != true

        binding.labelCategory.text = market?.categoryName;


        transferDataMarket.free = market?.free
        transferDataMarket.priceAgree = market?.priceAgree


            if (market?.free == true) {
            binding.tAssignment.text = getString(R.string.free)
            transferDataMarket.assignment = ContextApp.FREE;
            binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT

            binding.linearPrice.isVisible = false;

        } else if (market?.priceAgree == true) {
            binding.tAssignment.text = getString(R.string.an_agreement)
            transferDataMarket.assignment = ContextApp.AN_AGREEMENT;

            binding.layoutDeposit.isVisible = false
            binding.linearPrice.isVisible = false;
        } else {
            binding.tAssignment.text = getString(R.string.price2)
            transferDataMarket.assignment = ContextApp.PRICE_ASSIGMENT

            binding.linearPrice.isVisible = true;

            if (prefApp.getNameCountry() != "Iran" && prefApp.getNameCountry() != "ایران")
                binding.layoutDeposit.isVisible = false;
            else
                binding.layoutDeposit.isVisible = typeMarket == ContextApp.RENT;
        }
        binding.hintAssignment.isVisible = true;


        Log.i("testEditMarket", "onCreate General :  ${listTypeShowPrice.indexOf(market?.showType)}\r\n ")
        Log.i("testEditMarket", "onCreate General :  ${market?.showType}\r\n ")
        binding.spinnerTypePrice.setSelection(listTypeShowPrice.indexOf(market?.showType))

        /*********      init price hourly monthly yearly daily      *********/
        transferDataMarket.showType = market?.showType

        //              commodityPrice          //
        binding.tReal.text = if (market?.commodityPrice.isNullOrEmpty() ||
            market?.commodityPrice == "0" )  "" else market?.commodityPrice ;
        transferDataMarket.commodityPrice = market?.commodityPrice ?: ""
        binding.hintRealValue.isVisible = !(market?.commodityPrice.isNullOrEmpty() ||
                market?.commodityPrice == "0")

        //              yearlyRentPrice          //
        binding.tPriceYear.text = if (market?.yearlyRentPrice == 0L)
             "" else market?.yearlyRentPrice.toString() ;
        transferDataMarket.yearlyRentPrice = market?.yearlyRentPrice
        binding.hintYearPrice.isVisible = market?.yearlyRentPrice != 0L

        //              monthlyRentalPrice          //
        binding.tPriceMonth.text = if (market?.monthlyRentalPrice == 0L)
            "" else market?.monthlyRentalPrice.toString() ;
        transferDataMarket.monthlyRentalPrice = market?.monthlyRentalPrice
        binding.hintMonthlyPRice.isVisible = market?.monthlyRentalPrice != 0L

        //             Daily :  rentPrice          //
        binding.tDaily.text = market?.rentPrice ?: "";
        transferDataMarket.priceDayli = market?.rentPrice
        binding.hintDaily.isVisible = market?.rentPrice.isNullOrEmpty() != true

        //             hourlyRentalPrice          //
        binding.tHours.text = if (market?.hourlyRentalPrice == 0L)
            "" else market?.hourlyRentalPrice.toString() ;
        binding.hintHourPrice.isVisible = market?.hourlyRentalPrice != 0L
        /*********      end     *********/

        /*********       information home      *********/
        //             meterOfHouse          //
        binding.tMetrazh.text = market?.meterOfHouse.toString() ?: "";
        transferDataMarket.meterOfHouse = market?.meterOfHouse
        binding.hintMetrazh.isVisible = market?.meterOfHouse != 0

        //             rooms          //
        binding.tBadRoom.text = market?.rooms.toString()
        transferDataMarket.rooms = market?.rooms
        binding.hintBadroom.isVisible = market?.rooms != 0

        //             bathrooms          //
        binding.tBathRoom.text = market?.bathrooms.toString()
        transferDataMarket.bathrooms = market?.bathrooms
        binding.hintBathroom.isVisible = market?.bathrooms != 0

        //             yearOfHouse          //
        binding.tYearHome.text = market?.yearOfHouse.toString()
        transferDataMarket.yearOfHouse = market?.yearOfHouse
        binding.hintYearHome.isVisible = market?.yearOfHouse?.isNullOrEmpty() != true

        //             parking          //
        transferDataMarket.parkingID = market?.parkingID.toString()
        binding.tParkingHome.text = market?.parkingTypeName ?: ""
        binding.hintParking.isVisible = market?.parkingTypeName?.isNullOrEmpty() != true

        //             heating          //
        transferDataMarket.heatingID = market?.heatingID.toString()
        binding.tHeatingH.text = market?.heatingName ?: ""
        binding.hintHeating.isVisible = market?.heatingName?.isNullOrEmpty() != true

        //             cooling          //
        transferDataMarket.coolingID = market?.coolingID.toString()
        binding.tCoolingH.text = market?.coolingName ?: ""
        binding.hintCooling.isVisible = true
//        binding.hintCooling.isVisible = market?.coolingName?.isNullOrEmpty() != true
        /*********      end     *********/

        /*********       information Car      *********/
        //             fuel          //
        binding.tFule.text = market?.fuelType ?: ""
        transferDataMarket.fuelType = market?.fuelType
        binding.hintFuelType.isVisible = market?.fuelType.isNullOrEmpty() != true

        //             year of car          //
        binding.tYearCar.text = market?.yearOfCar ?: ""
        transferDataMarket.yearOfCar = market?.yearOfCar
        binding.hintYearConstrCar.isVisible = market?.yearOfCar.isNullOrEmpty() != true

        //             km car          //
        binding.tKm.text = market?.kilometerOfCar.toString()
        transferDataMarket.kilometerOfCar = market?.kilometerOfCar
        binding.hintKM.isVisible = market?.kilometerOfCar != 0;
        /*********      end     *********/


        typeOfguaranteeTO = market?.typeOfguaranteeTO ?: TypeOfguaranteeTO();


//        var listPhotos = ArrayList<PhotoTO>();
//        listPhotos?.add(PhotoTO(R.drawable.ic_baseline_photo_camera_48, null, null, null, 0))


        for (p in market?.photos ?: emptyList()) {
            val photo = PhotoTO()
            photo.photoUri = Uri.parse(p.image);
            photo.counter = p.order?.plus(1);
            photo.type = ContextApp.URL;
            list?.add(photo);
        }

//        photoAdapter.updateAdapter(list!!)
    }


    override fun onClickBtnConfirm(view: View) {
        Log.i("testEditMarket", "size photos : ${transferDataMarket.showType}")
        Log.i("testEditMarket", "size photos : ${transferDataMarket.priceDayli}")

        if (!validation())
            return

        val intent = Intent(this@EditGeneralActivity, EditMarketActivity::class.java)

        intent.putExtra(ContextApp.ASSIGNMENT, transferDataMarket.assignment)
        intent.putExtra(ContextApp.TYPE_OF_GUARANTEE, typeOfguaranteeTO)
        intent.putExtra(ContextApp.TRANFER_DATA_MARKET, transferDataMarket)
        intent.putExtra(ContextApp.TYPE_MARKET, typeMarket)
        intent.putExtra(ContextApp.TYPE_API, typeApi)
        intent.putExtra(ContextApp.MARKET_ID, marketID);

        intent.putParcelableArrayListExtra(ContextApp.PHOTOS, transferDataMarket.photos)
//        intent.putParcelableArrayListExtra(ContextApp.PHOTOS, transferDataMarket?.listPhotos)

        activityResultLauncherGeneral.launch(intent)
    }

}