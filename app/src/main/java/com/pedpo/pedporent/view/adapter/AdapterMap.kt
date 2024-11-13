package com.pedpo.pedporent.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterMapBinding
import com.pedpo.pedporent.databinding.AdapterPaginNewBinding
import com.pedpo.pedporent.model.model.MapData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyMutable
import com.pedpo.pedporent.utills.ShowPrice
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter.ClickAdapterPaging
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.squareup.picasso.Picasso
import javax.inject.Inject

class AdapterMap @Inject constructor(private val showPrice: ShowPrice
                                     , private val prefApp: PrefApp,)
    : RecyclerView.Adapter<AdapterMap.ViewHolder>()  {

    private var list:ArrayList<MapData>?=null

    init {
        list = ArrayList()
    }

    var clickAdapter: ClickAdapter? = null


    fun update(list:ArrayList<MapData>){
        this.list = list;
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = AdapterMapBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindView(list?.get(position))

    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }


    inner class ViewHolder(private val binding : AdapterMapBinding) : RecyclerView.ViewHolder(binding.root){
        
        fun bindView(data: MapData?){

            binding.viewModel = data

            Log.i("testDataMap", "bindView: ${data?.title}")

            if (data?.isActive == null || data.isActive){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true
                binding.layoutIsActive.isVisible = true
            }

//            binding.constraintHomeOrCar.isVisible = data?.isHome == true;


            if (data?.isHome == true){
                binding.tDescription.isInvisible = true;
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false;
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_bed)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_bath)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_metrazh)

                binding.detailHomeCar.tBedRoom.text = data.rooms
                binding.detailHomeCar.tBathRoom.text = data.bathrooms
                binding.detailHomeCar.tBuildYear.text = data.meterOfHouse
            }
            else if (data?.isCar == true){
                binding.tDescription.isInvisible = true;
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false;
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_km)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_fuel)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_car_year_constraction)

                binding.detailHomeCar.tBedRoom.text = data.kilometerOfCar
                binding.detailHomeCar.tBathRoom.text = data.fuelType
                binding.detailHomeCar.tBuildYear.text = data.yearOfCar
            }else{
                binding.tDescription.isInvisible = false;
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = true
            }

            if (data?.free == true)
                binding.tPrice.text = binding.root.context.getString(R.string.free);
            else if (data?.priceAgree == true)
                binding.tPrice.text = binding.root.context.getString(R.string.an_agreement);
            else if (data?.rentPriceDay != null) {

                binding.tPrice.text = showPrice.showPriceMap(data)

            }

            if (prefApp.getToken().isEmpty()) {
                binding.icBookmark.visibility = View.INVISIBLE;
            } else {
                    binding.icBookmark.visibility = View.VISIBLE
            }


            if (data?.free == true)
                binding.tPrice.text = binding.root.context.getString(R.string.free);
            else if (data?.priceAgree == true)
                binding.tPrice.text = binding.root.context.getString(R.string.an_agreement);
            else if (data?.rentPriceDay != null) {
                binding.tPrice.text = showPrice.showPriceMap(data)
            }


            binding.root.setOnClickListener {
                clickAdapter?.onClickAdapter(data, ContextApp.DETAILS, binding)
            }

            binding.icBookmark.setOnClickListener {
                clickAdapter?.onClickAdapter(data, ContextApp.BOOKMARK, binding)
            }


            if (data?.isLikeByIp == true){
                binding.icLike.setImageResource(R.drawable.ic_liked) ;
            }else
                binding.icLike.setImageResource(R.drawable.ic_like) ;

            binding.icLike.setOnClickListener {
                clickAdapter?.onClickAdapter(
                    mapData = data,
                    ContextApp.LIKE,
                    binding
                )
            }
        }

    }

    interface ClickAdapter {
        fun onClickAdapter(mapData: MapData?, action:String, binding: AdapterMapBinding)
    }

}