package com.pedpo.pedporent.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterSearchCategoryBinding
import com.pedpo.pedporent.listener.ClickAdpterSearchCategory
import com.pedpo.pedporent.model.search.SearchCategoryTO
import javax.inject.Inject

class SearchCategoryAdapter : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {

    private var list: ArrayList<SearchCategoryTO>? = null;
    var clickAdpterSearchCategory:ClickAdpterSearchCategory?=null;

    @Inject
    constructor() {
        list = ArrayList();
    }

    fun updateAdapter(list: ArrayList<SearchCategoryTO>?) {
        this.list = list ?: ArrayList();
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(list?.get(position)!!);
    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    inner class ViewHolder(val binding: AdapterSearchCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun initView(searchCategoryTO: SearchCategoryTO) {

            binding.viewModel = searchCategoryTO;

            itemView.setOnClickListener {
                if (clickAdpterSearchCategory!=null)
                    clickAdpterSearchCategory?.onClickAdapterSearchCategory(searchCategory = searchCategoryTO)
            }

        }

    }

}