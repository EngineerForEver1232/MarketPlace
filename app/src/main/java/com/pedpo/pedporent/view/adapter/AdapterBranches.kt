package com.pedpo.pedporent.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterBranchesBinding
import com.pedpo.pedporent.model.liseening.LiseeningTO
import com.pedpo.pedporent.model.store.branche.BranchesData
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AdapterBranches @Inject constructor() : RecyclerView.Adapter<AdapterBranches.ViewHolder>() {


    private var listBranches:ArrayList<BranchesData.BranchesTO>?=null;



    fun updateAdapter(list: List<BranchesData.BranchesTO>){
        this.listBranches = list as ArrayList<BranchesData.BranchesTO>;
        notifyDataSetChanged();
    }

    fun removeItem(position: Int?) {
        if (position!=null) {
            listBranches?.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun restoreItem(item: BranchesData.BranchesTO?, position: Int?) {
        if (item!=null && position !=null) {
            listBranches?.add(position, item)
            notifyItemInserted(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterBranchesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.listener = this;
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(listBranches?.get(position)?: BranchesData.BranchesTO())

    }

    inner class ViewHolder(val binding:AdapterBranchesBinding):RecyclerView.ViewHolder(binding.root){

        @JvmField
        var viewForeground: ConstraintLayout = binding.layoutAddBraches

        fun init(branchesTO: BranchesData.BranchesTO){
                binding.viewModel = branchesTO;

        }
    }

    override fun getItemCount(): Int {
        return listBranches?.size?:0 ;
    }

    /* onClick */
    fun onClickBranches(branchesTO: BranchesData.BranchesTO,position: Int){
            onClickAdapter?.onclickAdapter(branchesTO = branchesTO , position = position)
    }

    private var onClickAdapter:OnClickAdapter?=null;

    fun setOnClickAdapter(onClickAdapter: OnClickAdapter){
        this.onClickAdapter = onClickAdapter;
    }

    interface OnClickAdapter{
        fun onclickAdapter(branchesTO: BranchesData.BranchesTO , position: Int)
    }

}