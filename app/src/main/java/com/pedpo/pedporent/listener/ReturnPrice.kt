package com.pedpo.pedporent.listener

interface ReturnPrice {

    fun returnPriceDayli(priceDayli:String?);
    fun returnPriceReal(priceRent:String?);
    fun returnPriceHour(priceHour:String?);
    fun returnPriceMonth(priceMonth:String?);
    fun returnPriceYear(priceYear:String?);

    fun returnMetrazhHome(metrazh:String?);
    fun returnRoom(roomNumber:String?);
    fun returnBadRoom(badRoomNumber:String?);
    fun returnYearConstruction(yearConstruction:String?);

    fun returnKM(km:String?);
    fun returnTypeFuel(typeFuel:String?);
    fun returnYearConstructionCar(yearConstructionCar:String?);


}