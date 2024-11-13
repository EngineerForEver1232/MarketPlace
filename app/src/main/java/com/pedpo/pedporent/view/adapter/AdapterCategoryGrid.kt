package com.pedpo.pedporent.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterCategoryGridBinding
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AdapterCategoryGrid : RecyclerView.Adapter<AdapterCategoryGrid.ViewHolder>{

    private var list:List<CategoryStoreTO>?=null;

    @Inject
    constructor(){}

    fun updateAdapter(list: List<CategoryStoreTO>){
        this.list = list;
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCategoryGridBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.init(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size?:0 ;
    }

    inner class ViewHolder(private val binding:AdapterCategoryGridBinding) : RecyclerView.ViewHolder(binding.root){
        fun init(categoryStoreTO: CategoryStoreTO?){
            binding.tName.text = categoryStoreTO?.categoryStoreName;

            binding.icDelete.setOnClickListener {
             onClickDeleteCategory?.onClickDeleteCategory(categoryStoreTO = categoryStoreTO)
            }
        }

    }

    private var onClickDeleteCategory:OnClickDeleteCategory?=null;

    fun setOnClickDeleteCategory(onClickDeleteCategory: OnClickDeleteCategory){
        this.onClickDeleteCategory = onClickDeleteCategory;
    }

    interface OnClickDeleteCategory {
        fun onClickDeleteCategory(categoryStoreTO: CategoryStoreTO?);
    }

}