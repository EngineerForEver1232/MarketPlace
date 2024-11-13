package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.store.category.CategoryStoreTO

interface ClickIconCategoryStore {

    fun OnItemClickAdapter(categoryTO: CategoryStoreTO);

}