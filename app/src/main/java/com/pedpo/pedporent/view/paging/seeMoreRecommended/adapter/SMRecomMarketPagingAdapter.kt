package com.pedpo.pedporent.view.paging.seeMoreRecommended.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.databinding.NetworkItemBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.utils.NetworkState

class SMRecomMarketPagingAdapter(private val context: Context) :
    PagedListAdapter<PaginTO, RecyclerView.ViewHolder>(PaginTO.DIFF_CALLBACK) {

    private val TYPE_PROGRESS = 0;
    private val TYPE_ITEM = 1;
    private var networkState:NetworkState?=null;
    var clickAdapterPaging: ClickAdapterPaging?=null;



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context);

        if (viewType == TYPE_PROGRESS) {
            var bindingNetwork = NetworkItemBinding.inflate(layoutInflater, parent, false);
            return NetWorkViewHolder(bindingNetwork)
        } else {
            var paginBinding = AdapterPaginBinding.inflate(layoutInflater, parent, false);
            return ReCommendedViewHolder(paginBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ReCommendedViewHolder){
            (holder as ReCommendedViewHolder).bindView(getItem(position)!!)
        }else{
            (holder as NetWorkViewHolder).bindView(networkState = networkState!!)
        }
    }

    private fun hasExteraRow():Boolean{
        return networkState!=null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExteraRow() && position == itemCount - 1)
            TYPE_PROGRESS
        else
            TYPE_ITEM
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        var previousState = this.networkState;
        var previousExtraRow = hasExteraRow();
        this.networkState = newNetworkState;
        var newExtraRow = hasExteraRow();

        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow)
                notifyItemRemoved(itemCount)
            else
                notifyItemInserted(itemCount)
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class ReCommendedViewHolder(val binding: AdapterPaginBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(paginTO: PaginTO) {

//            Log.i("testPaging", "${paginTO.isBookMarkByUser} ${paginTO.isLikeByIp}")


            binding.viewModel = paginTO;

            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS,binding)
            }
            binding.icBookmark.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK,binding)
            }
            binding.icLike.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO = paginTO, ContextApp.LIKE,binding)
            }

//            binding.tTitle.text = marketPaginTO.title
//            Picasso.get().load(marketPaginTO.photoAddress).placeholder(R.drawable.placeholder).into(binding.imgItem);
        }

    }


    inner class NetWorkViewHolder(val binding: NetworkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(networkState: NetworkState) {

            if (networkState != null && networkState.status == NetworkState.Status.RUNNING)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE


            if (networkState != null && networkState.status == NetworkState.Status.FAILED) {
                binding.errorMsg.visibility = View.VISIBLE
                binding.errorMsg.text = networkState.msg
            } else {
                binding.errorMsg.visibility = View.GONE
            }

        }

    }

}