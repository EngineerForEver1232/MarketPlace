package com.pedpo.pedporent.view.paging.store.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginStoreBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.listener.OnClickStore
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyMutable
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.ShowPrice
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import okhttp3.internal.http.toHttpDateString
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class StorePagingAdapter @Inject constructor(
    private val showPrice: ShowPrice
    ) :
    PagingDataAdapter<PaginTO, StorePagingAdapter.LastedViewHolder>(PostComparator()) {

    private var numberFormat: NumberFormatDigits? = null;
     var onClickStore: OnClickStore?=null;
    private var iAmNot:Boolean = false;
    private var listTest = ArrayList<String>()
    private var listView = ArrayList<ImageView>()


    fun isIAmNot(i : Boolean){
        this.iAmNot = i;
    }

    override fun onBindViewHolder(holder: LastedViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindView(data?:PaginTO())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastedViewHolder {

        val prefManagerLanguage = PrefManagerLanguage(parent.context)
        numberFormat = NumberFormatDigits(prefManagerLanguage)

        return LastedViewHolder(
            binding =
            AdapterPaginStoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
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


    inner class LastedViewHolder(private val binding: AdapterPaginStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(paginTO: PaginTO) {


            if (paginTO.startDate!=null && paginTO.endDate!=null){
                val startDate = startDate(paginTO.startDate?:"");
                val endDate = endDate( paginTO.endDate?:"");

                binding.tStartDate.text = startDate.get(Calendar.DAY_OF_MONTH).toString();
                binding.tEndDate.text = endDate.get(Calendar.DAY_OF_MONTH).toString();

//                binding.startMonth.text = startDate.getDisplayName(startDate.get(Calendar.MONTH)+1,0, Locale.CANADA);
                binding.startMonth.text = startDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);
                binding.endMonth.text = endDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);

            }

            if (iAmNot) {
                binding.icMenu.visibility = View.VISIBLE
                binding.icBookmark.visibility = View.INVISIBLE
            }else{
                binding.icMenu.visibility = View.INVISIBLE
                binding.icBookmark.visibility = View.VISIBLE
            }

            listTest.add(paginTO.marketID ?: "");
            listView.add(binding.icBookmark);

            binding.viewModel = paginTO;

            if (paginTO.title.isNullOrEmpty())
                binding.tTitle.text = paginTO.registerDate.toString();
            else
                binding.tTitle.text = paginTO.title;

            if (paginTO.isActive == null || paginTO.isActive == true){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true ;
                binding.layoutIsActive.isVisible = true ;
            }


            if (paginTO.isHome == true){
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false;
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_bed)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_bath)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_metrazh)

                binding.detailHomeCar.tBedRoom.text = paginTO.rooms
                binding.detailHomeCar.tBathRoom.text = paginTO.bathrooms
                binding.detailHomeCar.tBuildYear.text = paginTO.meterOfHouse
            }
            else if (paginTO.isCar == true){
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false;
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_km)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_fuel)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_car_year_constraction)

                binding.detailHomeCar.tBedRoom.text = paginTO.kilometerOfCar
                binding.detailHomeCar.tBathRoom.text = paginTO.fuelType
                binding.detailHomeCar.tBuildYear.text = paginTO.yearOfCar
            }else{
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = true;
            }


            binding.tTitle.isVisible = !paginTO.title.isNullOrEmpty()

            if (paginTO.free == true) {
                binding.tPrice.text = binding?.root.context.getString(R.string.free);
            } else if (paginTO.priceAgree == true) {
                binding.tPrice.text = binding?.root.context.getString(R.string.an_agreement)
            } else {
                binding.tPrice.text = showPrice.showPrice(paginTO)
            }
            if (binding.lifecycleOwner!=null)
            MyMutable.mutableMarketID.observe(binding.lifecycleOwner!!) {
                for (i in 0 until listTest.size) {
                    if (listTest[i] == it) {
                        Log.i("testGeneral", "onClickTest: ${listTest.get(i)}")
                        listView.get(i).setImageResource(R.drawable.ic_bookmark)
                    }
                }
            }
//            binding.icBookmark.setOnClickListener {
//                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK, binding)
//            }


            binding.layoutDeactive.setOnClickListener {
                onClickStore?.onClickStore(paginTO = paginTO,position,ContextApp.MENU);
            }
            binding.icMenu.setOnClickListener {
                onClickStore?.onClickStore(paginTO = paginTO,position,ContextApp.MENU);
            }
            binding.root.setOnClickListener {
                onClickStore?.onClickStore(paginTO = paginTO,position,ContextApp.DETAILS);
            }


        }
    }

}

private class PostComparator : DiffUtil.ItemCallback<PaginTO>() {
    override fun areItemsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
        return oldItem?.equals(newItem)
    }
}


