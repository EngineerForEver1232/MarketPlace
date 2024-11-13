package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterStoreListBinding
import com.pedpo.pedporent.listener.OnCallJust
import com.pedpo.pedporent.listener.OnItemClickStoreList
import com.pedpo.pedporent.model.store.storeList.StoreListTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject


@ActivityScoped
 class StoreListAdapter @Inject constructor(@ActivityContext context: Context) :
    RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {

    private var context: Context? = context
    private var layoutInflater: LayoutInflater? = null
    private var list: List<StoreListTO>? = null
     var onItemClick: OnItemClickStoreList?=null

    fun updateAdapter(list: List<StoreListTO>?) {
        this.list = list?:ArrayList<StoreListTO>()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterStoreListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(market = list?.get(position),position)

//        Log.i("testActive", "onBindViewHolder: ${list?.get(position)?.isActive}")

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(
                list?.get(position)
            )
        }

    }

    override fun getItemCount(): Int {
        return list?.size?:0;
    }

    inner class ViewHolder(private val binding: AdapterStoreListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(market: StoreListTO?, position: Int?){
            if (market!=null)
            binding.viewModel=market;

            binding.icCall.setOnClickListener {
                val number = Uri.parse("tel:${market?.phoneNumber}")
                val callIntent = Intent(Intent.ACTION_DIAL, number)
                context?.startActivity(callIntent)
            }
//            binding.viewRate.setOnClickListener() {
//                onCallJust?.onCall(storeListTO = market)
//            }

        }
    }
    private var onCallJust:OnCallJust?=null;

    fun setCall(onCallJust: OnCallJust){
        this.onCallJust = onCallJust;
    }

    init {
        layoutInflater = LayoutInflater.from(context)
        list = ArrayList<StoreListTO>()
    }

}