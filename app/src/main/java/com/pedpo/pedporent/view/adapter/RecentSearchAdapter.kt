package com.pedpo.pedporent.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterRecentSearchBinding
import com.pedpo.pedporent.listener.ClickAdapterRecentlySearch
import com.pedpo.pedporent.room.entity.RecentSearch
import com.pedpo.pedporent.utills.ContextApp
import javax.inject.Inject

class  RecentSearchAdapter @Inject constructor() :RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>(){


    private var list:List<RecentSearch>?=null;
    var clickAdapterRecentlySearch:ClickAdapterRecentlySearch?=null;

    fun updateAdapter(list: List<RecentSearch>?){
        this.list = list;
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecentSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.initView(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size?:0;
    }



    inner class ViewHolder(private val binding: AdapterRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun initView(recentSearch: RecentSearch?){
            binding.viewModel = recentSearch;

            binding.icDelete.setOnClickListener {
                clickAdapterRecentlySearch?.onClickRecentlySearch(recentSearch = recentSearch,ContextApp.DELETE)
            }
            binding.root.setOnClickListener {
                clickAdapterRecentlySearch?.onClickRecentlySearch(recentSearch = recentSearch,"show")
            }

        }

    }

}