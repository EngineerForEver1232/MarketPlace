package com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyMutable
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.UtillsApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class LastedPagingAdapter(
    private val prefApp: PrefApp,
    private val lifecycleOwner: LifecycleOwner
) :
    PagingDataAdapter<PaginTO, LastedPagingAdapter.LastedViewHolder>(PostComparator()) {

    var clickAdapterPaging: ClickAdapterPaging? = null;
//    var prefApp: PrefApp? = null;

    private var utillsApp: UtillsApp?=null;
    var numberFormat: NumberFormatDigits? = null;


    override fun onBindViewHolder(holder: LastedViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindView(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastedViewHolder {

        utillsApp = UtillsApp(context = parent.context);
        val prefManagerLanguage = PrefManagerLanguage(parent.context);
        numberFormat = NumberFormatDigits(prefManagerLanguage);

        return LastedViewHolder(
            binding =
            AdapterPaginBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    var listTest = ArrayList<String>();
    var listView = ArrayList<ImageView>();

    inner class LastedViewHolder(private val binding: AdapterPaginBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(paginTO: PaginTO?) {

            listTest.add(paginTO?.marketID ?: "");
            listView.add(binding.icBookmark);

            binding.viewModel = paginTO;

            if (paginTO?.isActive == null || paginTO.isActive == true){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true
                binding.layoutIsActive.isVisible = true
            }


            if (paginTO?.startTimeInactive!=null && paginTO.endTimeInactive!=null){
                val startDate = utillsApp?.startDate(paginTO.startTimeInactive?:"");
                val endDate = utillsApp?.endDate( paginTO.endTimeInactive?:"");

                binding.tStartDate.text = startDate?.get(Calendar.DAY_OF_MONTH).toString();
                binding.tEndDate.text = endDate?.get(Calendar.DAY_OF_MONTH).toString();

//                binding.startMonth.text = startDate.getDisplayName(startDate.get(Calendar.MONTH)+1,0, Locale.CANADA);
                binding.startMonth.text = startDate?.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);
                binding.endMonth.text = endDate?.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA);

            }

            if (paginTO?.title.isNullOrEmpty())
                binding.tTitle.text = paginTO?.registerDate.toString();
            else
                binding.tTitle.text = paginTO?.title;


            if (prefApp.getToken().isNullOrEmpty()) {
                binding.icBookmark.visibility = View.INVISIBLE;
            } else {
                if (paginTO?.nameSite != ContextApp.Pedpo)
                    binding.icBookmark.visibility = View.INVISIBLE
                else
                    binding.icBookmark.visibility = View.VISIBLE
            }


            binding.tTitle.isVisible = !paginTO?.title.isNullOrEmpty()

            if (!paginTO?.logo.isNullOrEmpty()) {
                binding.icLike.visibility = View.VISIBLE;
                Picasso.get().load(paginTO?.logo).into(binding.icLike)
            } else {
                binding.icLike.visibility = View.INVISIBLE
                binding.icLike.setImageDrawable(null)
            }
            if (paginTO?.free == true)
                binding.tPrice.text = binding.root.context.getString(R.string.free);
            else if (paginTO?.priceAgree == true)
                binding.tPrice.text = binding.root.context.getString(R.string.an_agreement);
            else if (paginTO?.rentPriceDay != null) {

                if (paginTO.type == ContextApp.RENT)
                binding.tPrice.text =  "$"+ numberFormat?.convertToDigist(paginTO.rentPriceDay ?: 0)+" "+paginTO.showType;
                else
                    binding.tPrice.text = "$"+ numberFormat?.convertToDigist(paginTO.rentPriceDay ?: 0)

            }

            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS, binding)
            }

            MyMutable.mutableMarketID.observe(lifecycleOwner) {
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

//            binding.icLike.setOnClickListener {
//                clickAdapterPaging?.onClickAdapterPaging(
//                    paginTO = paginTO,
//                    ContextApp.LIKE,
//                    binding
//                )
//            }
//            binding.tTitle.text = market?.title;
//            Picasso.get().load(market?.photoAddress).placeholder(R.drawable.placeholder).into(binding.imgItem);
        }
    }

}

 internal class PostComparator : DiffUtil.ItemCallback<PaginTO>() {
    override fun areItemsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
        return oldItem?.equals(newItem)
    }
}


