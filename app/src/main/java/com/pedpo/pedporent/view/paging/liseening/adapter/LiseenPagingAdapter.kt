package com.pedpo.pedporent.view.paging.liseening.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

class LiseenPagingAdapter(private val prefApp: PrefApp) :
PagingDataAdapter<PaginTO,LiseenPagingAdapter.ViewHolder>(PostComparator()){

    var clickAdapterPaging: ClickAdapterPaging?=null;



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(binding =
        AdapterPaginBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    inner class ViewHolder(private val binding: AdapterPaginBinding):RecyclerView.ViewHolder(binding.root){
        fun bindView(paginTO: PaginTO?){

            binding.viewModel = paginTO;

            if (paginTO?.title.isNullOrEmpty())
                binding?.tTitle.text = paginTO?.registerDate.toString();
            else
                binding?.tTitle.text = paginTO?.title;

            if (prefApp?.getToken().isNullOrEmpty())
                binding.icBookmark.visibility = View.INVISIBLE;

            binding.tTitle.isVisible = !paginTO?.title.isNullOrEmpty()

            if (paginTO?.free == true) {
                binding.tPrice.text =  binding?.root.context.getString(R.string.free)

            } else if (paginTO?.priceAgree == true) {
                binding.tPrice.text = binding?.root.context.getString(R.string.an_agreement)

            } else {
                if (paginTO?.rentPriceDay != null)
                    binding.tPrice.text = paginTO?.rentPriceDay.toString()

            }

            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS,binding)
            }
            binding.icBookmark.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK,binding)
            }
            binding.icLike.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO = paginTO, ContextApp.LIKE,binding)
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