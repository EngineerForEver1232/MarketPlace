package com.pedpo.pedporent.listener

import com.pedpo.pedporent.databinding.AdapterCategoryBinding
import com.pedpo.pedporent.model.store.category.CategoryStoreTO

interface ClickAdapterCategoryStore {

    fun OnItemClickListenerAdapter(categoryTO: CategoryStoreTO, binding: AdapterCategoryBinding);

}