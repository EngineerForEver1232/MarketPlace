package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.market.category.CategoryTO

interface ClickAdapterCategory {

    fun OnItemClickListenerAdapter(categoryTO: CategoryTO, type:String);

}