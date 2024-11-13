package com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginNewBinding
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.squareup.picasso.Picasso
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LastedPagingAdapterNew @Inject constructor (
    private val prefApp: PrefApp ,
    private val showPrice: ShowPrice
)  : PagingDataAdapter<PaginTO, LastedPagingAdapterNew.LastedViewHolder>(PostComparator()) {

    var clickAdapterPaging: ClickAdapterPaging? = null
    private var utillsApp: UtillsApp?=null
    var numberFormat: NumberFormatDigits? = null

    var listTest = ArrayList<String>()
    var listView = ArrayList<ImageView>()

    override fun onBindViewHolder(holder: LastedViewHolder, position: Int) {
        val data = getItem(position)
        Log.i("adapterLasted", "onBindViewHolder: $itemCount")
        holder.bindView(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastedViewHolder {

        utillsApp = UtillsApp(context = parent.context);
        val prefManagerLanguage = PrefManagerLanguage(parent.context)
        numberFormat = NumberFormatDigits(prefManagerLanguage)

        return LastedViewHolder(
            binding =
            AdapterPaginNewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
    }

    inner class LastedViewHolder(private val binding: AdapterPaginNewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(paginTO: PaginTO?) {

            listTest.add(paginTO?.marketID ?: "")
            listView.add(binding.icBookmark)

            binding.viewModel = paginTO

            if (paginTO?.isActive == null || paginTO.isActive == true){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true
                binding.layoutIsActive.isVisible = true
            }

//            binding.constraintHomeOrCar.isVisible = paginTO?.isHome == true;


            if (paginTO?.isHome == true){
                binding.tDescription.isInvisible = true
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_bed)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_bath)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_metrazh)

                binding.detailHomeCar.tBedRoom.text = paginTO.rooms
                binding.detailHomeCar.tBathRoom.text = paginTO.bathrooms
                binding.detailHomeCar.tBuildYear.text = paginTO.meterOfHouse
            }
            else if (paginTO?.isCar == true){
                binding.tDescription.isInvisible = true
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = false
                binding.detailHomeCar.icBed.setImageResource(R.drawable.ic_km)
                binding.detailHomeCar.icBath.setImageResource(R.drawable.ic_fuel)
                binding.detailHomeCar.icBuild.setImageResource(R.drawable.ic_car_year_constraction)

                binding.detailHomeCar.tBedRoom.text = paginTO.kilometerOfCar
                binding.detailHomeCar.tBathRoom.text = paginTO.fuelType
                binding.detailHomeCar.tBuildYear.text = paginTO.yearOfCar
            }else{
                binding.tDescription.isInvisible = false
                binding.detailHomeCar.constraintHomeOrCar.isInvisible = true
            }


            if (paginTO?.startTimeInactive!=null && paginTO.endTimeInactive!=null){
                val startDate = utillsApp?.startDate(paginTO.startTimeInactive?:"")
                val endDate = utillsApp?.endDate( paginTO.endTimeInactive?:"")

                binding.tStartDate.text = startDate?.get(Calendar.DAY_OF_MONTH).toString()
                binding.tEndDate.text = endDate?.get(Calendar.DAY_OF_MONTH).toString()

//                binding.startMonth.text = startDate.getDisplayName(startDate.get(Calendar.MONTH)+1,0, Locale.CANADA);
                binding.startMonth.text = startDate?.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)
                binding.endMonth.text = endDate?.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)

            }


            if (prefApp.getToken().isEmpty()) {
                binding.icBookmark.visibility = View.INVISIBLE;
            } else {
                if (paginTO?.nameSite != ContextApp.Pedpo)
                    binding.icBookmark.visibility = View.INVISIBLE
                else
                    binding.icBookmark.visibility = View.VISIBLE
            }


            binding.tTitle.isVisible = !paginTO?.title.isNullOrEmpty()

//            if (!paginTO?.logo.isNullOrEmpty()) {
//                binding.icLike.visibility = View.VISIBLE;
//                Picasso.get().load(paginTO?.logo).into(binding.icLike)
//            } else {
//                binding.icLike.visibility = View.INVISIBLE
//                binding.icLike.visibility = View.VISIBLE;
//                binding.icLike.setImageResource(R.drawable.ic_like)
//                binding.icLike.setImageDrawable(null)
//            }
            if (paginTO?.free == true)
                binding.tPrice.text = binding.root.context.getString(R.string.free);
            else if (paginTO?.priceAgree == true)
                binding.tPrice.text = binding.root.context.getString(R.string.an_agreement);
            else if (paginTO?.rentPriceDay != null) {

//                val tPrice = StringBuilder();

//                if (paginTO.type == ContextApp.RENT) {
//                    tPrice.append(" $ ")
//                    tPrice.append("${numberFormat?.convertToDigist(paginTO.rentPriceDay ?: 0)}")
//                    tPrice.append(" / ")
//                    tPrice.append(paginTO.priceType)
//                }
//                else {
//                    tPrice.append(" $ ")
//                    tPrice.append("${numberFormat?.convertToDigist(paginTO.rentPriceDay ?: 0)}")
//                }

                binding.tPrice.text = showPrice.showPrice(paginTO )
//                 showPrice.showPriceTest(paginTO ,binding.tPrice)

            }


            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS, binding)
            }

            if (binding.lifecycleOwner!=null)
            MyMutable.mutableMarketID.observe(binding.lifecycleOwner!!) {
                for (i in 0 until listTest.size) {
                    if (listTest[i] == it) {
//                        Log.i("testGeneral", "onClickTest: ${listTest.get(i)}")
                        listView.get(i).setImageResource(R.drawable.ic_bookmark)
                    }
//                    Log.i("testGeneral", "onClickTest: ${listTest.get(i)}")
                }
//                    clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK,binding)
            }
            binding.icBookmark.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK, binding)
            }

            binding.icLike.isVisible = paginTO?.logo.isNullOrEmpty() ;
            if (paginTO?.logo.isNullOrEmpty()) {
                binding.icLogo.isVisible = false ;
                binding.icLogo.setImageDrawable(null) ;
            } else {
                binding.icLogo.isVisible = true ;
                Picasso.get().load(paginTO?.logo).into(binding.icLogo) ;
            }


            if (paginTO?.isLikeByIp == true){
                binding.icLike.setImageResource(R.drawable.ic_liked) ;
            }else
                binding.icLike.setImageResource(R.drawable.ic_like) ;

            binding.icLike.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(
                    paginTO = paginTO,
                    ContextApp.LIKE,
                    binding
                )
            }
//            binding.tTitle.text = market?.title;
//            Picasso.get().load(market?.photoAddress).placeholder(R.drawable.placeholder).into(binding.imgItem);
        }
    }

}

interface ClickAdapterPaging {

    fun onClickAdapterPaging(paginTO: PaginTO?,action:String,binding: AdapterPaginNewBinding)

}

//private class PostComparator : DiffUtil.ItemCallback<PaginTO>() {
//    override fun areItemsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
//        return oldItem.equals(newItem)
//    }
//}


