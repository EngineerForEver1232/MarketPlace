package com.pedpo.pedporent.model.market

import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.market.editMarket.EditMarketTO
import com.pedpo.pedporent.model.transfer.TransferDataMarket

class EditMarketGetData : ResponseTO() {

    var data: TransferDataMarket?=null;

}