package com.pedpo.pedporent.model.model

import com.pedpo.pedporent.model.ResponseTO

class OneMarketMap : ResponseTO() {

     var data : Data?=null;

    data class Data(
        var latitude:Double,
        var longitude:Double
    )

}