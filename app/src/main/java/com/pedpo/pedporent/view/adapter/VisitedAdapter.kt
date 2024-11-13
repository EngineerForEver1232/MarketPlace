package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.listener.ClickAdapterVisited
import com.pedpo.pedporent.databinding.AdapterVisitedBinding
import com.pedpo.pedporent.model.market.VisitMarketTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class VisitedAdapter : RecyclerView.Adapter<VisitedAdapter.ViewHolder> {

    var context: Context? = null;
    var list: List<VisitMarketTO>? = null;
    var clickAdapter: ClickAdapterVisited? = null;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
        this.list = ArrayList<VisitMarketTO>()
    }

    fun updateAdapter(list: List<VisitMarketTO>) {
        this.list = list;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterVisitedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(val binding: AdapterVisitedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun initView(visitMarketTO: VisitMarketTO?){
            binding.viewModel = visitMarketTO;
        }

    }
}