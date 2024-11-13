package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.market.category.CategoryTO

interface ClickIconCategory {

    fun OnItemClickListenerAdapter(categoryTO: CategoryTO);

}