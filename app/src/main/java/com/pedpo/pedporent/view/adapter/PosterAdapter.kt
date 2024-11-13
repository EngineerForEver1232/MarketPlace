package com.pedpo.pedporent.view.adapter

import android.content.Context
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
class PosterAdapter : RecyclerView.Adapter<PosterAdapter.ViewHolder>{

    private var context: Context? = null;
    private var layoutInflater:LayoutInflater?=null;
    private var list:List<PosterTO>?=null;
    lateinit var binding: AdapterViewpagerPosterBinding


    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        list = ArrayList<PosterTO>();
    }


    fun updateAdapter(list: List<PosterTO>?){
        this.list = list ?: ArrayList<PosterTO>();
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AdapterViewpagerPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        val view = layoutInflater?.inflate(R.layout.adapter_viewpager_poster,parent,false);

        return ViewHolder(binding!!);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.image?.setImageResource(list?.get(position)?.image!!)
//        binding.imgViewPager.setImageResource(list?.get(position)?.photo)
        Picasso.get().load(list?.get(position)?.photo).placeholder(R.drawable.placeholder).into(binding.imgViewPager)
    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    class ViewHolder(itemView: AdapterViewpagerPosterBinding) : RecyclerView.ViewHolder(itemView.root) {

    }

}