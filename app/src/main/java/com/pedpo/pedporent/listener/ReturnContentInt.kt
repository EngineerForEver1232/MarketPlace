package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.market.TypeOfguaranteeTO

interface ReturnContentInt {

    fun returnContent(content:String?,textOther:String?,typeOfguaranteeTO: TypeOfguaranteeTO?);

}