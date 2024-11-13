package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.store.storeList.StoreListTO

interface OnCallJust {

    fun onCall(storeListTO: StoreListTO?);

}