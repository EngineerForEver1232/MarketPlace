package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterMyItemsBinding
import com.pedpo.pedporent.listener.ClickMyItems
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.ShowPrice
import com.pedpo.pedporent.utills.language.PrefManagerLanguage

import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@ActivityScoped
 class MyItemsAdapter : RecyclerView.Adapter<MyItemsAdapter.ViewHolder> {

    private var context: Context? = null;
    private var layoutInflater: LayoutInflater? = null;
    private var list: List<MyMarkets>? = null;
    var clickMyItems:ClickMyItems?=null;
    var numberFormat: NumberFormatDigits? = null;
    private var showPrice:ShowPrice?=null;


    @Inject
    constructor(@ActivityContext context: Context , showPrice: ShowPrice ) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<MyMarkets>();
        this.showPrice = showPrice;

    }

    fun updateAdapter(list: List<MyMarkets>?) {
        this.list = list?:ArrayList<MyMarkets>()

        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMyItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val prefManagerLanguage = PrefManagerLanguage(parent.context)
        numberFormat = NumberFormatDigits(prefManagerLanguage)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(market = list?.get(position),position)

        Log.i("testActive", "onBindViewHolder: ${list?.get(position)?.isActive}")

//        holder.itemView.setOnClickListener {
//            clickAdapter?.OnItemClickListenerAdapter(
//                list?.get(position)?.marketID!!.toString()
//            )
//        }

    }

    fun startDate(startDate:String) :Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendarStart = Calendar.getInstance()
        calendarStart.time = Date(format.parse(startDate).time)

        return calendarStart;
    }
    fun endDate(endDate:String) :Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = Date(format.parse(endDate).time)

        return calendarEnd;
    }

    override fun getItemCount(): Int {
        return list?.size?:0;
    }

    inner class ViewHolder(private val binding: AdapterMyItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(market: MyMarkets?,position: Int?){
            if (market!=null)
            binding.viewModel=market;

            if (market?.startDate!=null && market.endDate!=null){
                val startDate = startDate(market.startDate?:"");
                val endDate = endDate( market.endDate?:"");

                binding.tStartDate.text = startDate.get(Calendar.DAY_OF_MONTH).toString();
                binding.tEndDate.text = endDate.get(Calendar.DAY_OF_MONTH).toString();

//                binding.startMonth.text = startDate.getDisplayName(startDate.get(Calendar.MONTH)+1,0, Locale.CANADA);
                binding.startMonth.text = startDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);
                binding.endMonth.text = endDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);

            }

            binding.icMenu.setOnClickListener {
                clickMyItems?.onClickMyItems(market = market,position,ContextApp.MENU)
            }
            binding.root.setOnClickListener {
                clickMyItems?.onClickMyItems(market = market,position,ContextApp.DETAILS)
            }
            binding.icNoti.setOnClickListener {
                clickMyItems?.onClickMyItems(market = market,position,ContextApp.NOTIFICATION)
            }

            if (market?.isActive == null || market.isActive == true){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true ;
                binding.layoutIsActive.isVisible = true ;
            }

            if (market?.free==true){
                binding.tPrice.text = context?.getString(R.string.free)
            }else if (market?.priceAgree==true){
                binding.tPrice.text = context?.getString(R.string.an_agreement)
            }else if (market?.rentPriceDay != null) {

//          binding.tPrice.text =  "$"+ numberFormat?.convertToDigist(market.rentPriceDay ?: 0)+" "+market.showType;
            binding.tPrice.text = showPrice?.showPriceMyItem(market)
            }
            else {

                if (market?.rentPriceDay != null)
                    binding.tPrice.text = market.rentPriceDay.toString()
            }
            Log.i("testMyItem", "initView: ${market?.free} ${market?.priceAgree} ${market?.rentPriceDay}")


            if (market?.isHome == true){
                binding.tDescription.isInvisible = true
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_bed)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_bath)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_metrazh)

                binding.detailHomeCar.tBedRoom.text = market.rooms
                binding.detailHomeCar.tBathRoom.text = market.bathrooms
                binding.detailHomeCar.tBuildYear.text = market.meterOfHouse
            }
            else if (market?.isCar == true){
                binding.tDescription.isInvisible = true
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_km)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_fuel)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_car_year_constraction)

                binding.detailHomeCar.tBedRoom.text = market.kilometerOfCar
                binding.detailHomeCar.tBathRoom.text = market.fuelType
                binding.detailHomeCar.tBuildYear.text = market.yearOfCar
            }else{
                binding.tDescription.isInvisible = false
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = true
            }

//            test(binding)
//            binding.icEdit.setOnClickListener {
//                clickMyItems?.onClickMyItems(market = market)
//            }
        }
    }

}