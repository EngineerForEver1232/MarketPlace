package com.pedpo.pedporent.view.paging.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.databinding.AdapterPaginFilterBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.listener.ClickAdapterPagingFilter
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

class FilterAdapter(private val context: Context) : PagingDataAdapter<PaginTO, RecyclerView.ViewHolder>(PostComparator()) {

    var clickAdapterPaging: ClickAdapterPagingFilter? = null;

    var prefApp: PrefApp? = null;
    var numberFormat: NumberFormatDigits? = null;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var prefManagerLanguage = PrefManagerLanguage(parent.context);
        numberFormat = NumberFormatDigits(prefManagerLanguage);

        val binding =
            AdapterPaginFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        prefApp = PrefApp(parent.context);
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ViewHolder).bindView(getItem(position = position))
    }

    inner class ViewHolder(val binding: AdapterPaginFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(paginTO: PaginTO?) {
            binding.viewModel = paginTO

            Log.i(
                "testPaging",
                "${paginTO?.isBookMarkByUser}" +
                        " ${paginTO?.isLikeByIp}" +
                        " ${paginTO?.rentPriceDay.toString()}"
            )


//            binding.tTitle.isVisible = !paginTO?.title.isNullOrEmpty()

            if (prefApp?.getToken().isNullOrEmpty() || paginTO?.nameSite != ContextApp.Pedpo)
                binding.icBookmark.visibility = View.INVISIBLE;

            if (paginTO?.isLikeByIp==true)
                binding.icLike.setImageDrawable( context.getDrawable(R.drawable.ic_liked))
            else
                binding.icLike.setImageDrawable( context.getDrawable(R.drawable.ic_like))

            if (paginTO?.isBookMarkByUser==true)
                binding.icBookmark.setImageDrawable( context.getDrawable(R.drawable.ic_bookmarked))
            else
                binding.icLike.setImageDrawable( context.getDrawable(R.drawable.ic_bookmark))


            if (paginTO?.title.isNullOrEmpty())
                binding.tTitle.text = paginTO?.nameSite.toString();
            else
                binding.tTitle.text = paginTO?.title;

            Log.i("testFilterAdapter", "bindView: ${paginTO?.nameSite} ${paginTO?.title}")

            if (paginTO?.free == true) {
                binding.tPrice.text = context.getString(R.string.free)
                binding.tPrice.isVisible = true;

            } else if (paginTO?.priceAgree == true) {
                binding.tPrice.text = context.getString(R.string.an_agreement)
                binding.tPrice.isVisible = true;

            } else {

                if (paginTO?.type == ContextApp.RENT)
                    binding.tPrice.text =  "$"+ numberFormat?.convertToDigist(paginTO.rentPriceDay ?: 0)+" "+paginTO.showType;
                else
                    binding.tPrice.text = "$"+ numberFormat?.convertToDigist(paginTO?.rentPriceDay ?: 0)


//                if (paginTO?.rentPriceDay != null)
//                binding.tPrice.text = numberFormat?.convertToDigist(paginTO?.rentPriceDay ?: 0)
//                Log.i("testAdapterFilter", "bindView: ${paginTO?.rentPriceDay.toString()}")

                binding.tPrice.isVisible = true;

                if (paginTO?.rentPriceDay == null ){
                    binding.tPrice.text = "-----" ;
                    binding.tPrice.isVisible = false;

                    Log.i("testAdapterFilter", "bindView: ${paginTO?.rentPriceDay.toString()}")
                }


            }

            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS, binding)
            }
            binding.icBookmark.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK, binding)
            }
            binding.icLike.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(
                    paginTO = paginTO,
                    ContextApp.LIKE,
                    binding
                )
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