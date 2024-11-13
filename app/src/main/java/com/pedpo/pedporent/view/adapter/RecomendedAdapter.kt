package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
 import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterRecomendedBinding
import com.pedpo.pedporent.listener.ClickAdapterVisited
import com.pedpo.pedporent.model.market.VisitMarketTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import androidx.core.view.isVisible

@ActivityScoped
class RecomendedAdapter : RecyclerView.Adapter<RecomendedAdapter.ViewHolder>{

    var context: Context?=null;
    var layoutInflater:LayoutInflater?=null;
    var list:List<VisitMarketTO>?=null;
    var clickAdapter: ClickAdapterVisited? = null;

    @Inject
    constructor(@ActivityContext context: Context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = ArrayList<VisitMarketTO>()
    }

    fun updateAdapter(list: List<VisitMarketTO>){
        this.list = list;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecomendedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        var view = layoutInflater?.inflate(R.layout.adapter_recomended,parent,false);
        return ViewHolder(binding!!);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.initView(list?.get(position))

        holder.itemView.setOnClickListener {
            clickAdapter?.OnItemClickListenerAdapter(
                list?.get(position)?.marketID!!.toString()
            )
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }



    inner class ViewHolder(val binding: AdapterRecomendedBinding) : RecyclerView.ViewHolder(binding.root){

        fun initView(visitMarketTO: VisitMarketTO?){
            binding.viewModel = visitMarketTO;
            if (visitMarketTO?.free == true){
                binding.tPrice.text= context?.getString(R.string.free)
                binding.tTitlePrice.isVisible = false;
            }else if (visitMarketTO?.priceAgree == true){
                binding.tPrice.text= context?.getString(R.string.an_agreement)
                binding.tTitlePrice.isVisible = false;
            }else{
                binding.tPrice.text= visitMarketTO?.rentPriceDay.toString()
                binding.tTitlePrice.isVisible = true;
            }
            Log.i("adapterRecommended", "initView: ${visitMarketTO?.free}")
            Log.i("adapterRecommended", "initView: ${visitMarketTO?.rentPriceDay}")
        }

    }
}