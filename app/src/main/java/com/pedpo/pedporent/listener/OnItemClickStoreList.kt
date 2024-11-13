package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.store.storeList.StoreListTO

interface OnItemClickStoreList {

    fun onItemClick(storeListTO: StoreListTO?)

}