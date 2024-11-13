package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterViewpagerPosterBinding
import com.pedpo.pedporent.model.poster.PosterTO
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PosterStoreAdapter @Inject constructor() :
    RecyclerView.Adapter<PosterStoreAdapter.ViewHolder>() {

    private var list: List<PosterTO>? = null

    init {
        list = ArrayList<PosterTO>()
    }


    fun updateAdapter(list: List<PosterTO>?) {
        this.list = list ?: ArrayList<PosterTO>()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = AdapterViewpagerPosterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { holder.initView(it) }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    class ViewHolder(val binding: AdapterViewpagerPosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initView(posterTO: PosterTO) {
            binding.viewModel = posterTO
        }
    }

}