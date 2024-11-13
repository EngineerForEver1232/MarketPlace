package com.pedpo.pedporent.model.market.editMarket

import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.google.gson.annotations.SerializedName

//class SubmitMarketTO : AdMarketTO() {
class SubmitServiceTO  {

    @SerializedName("MarketID")
    var marketID:String?=null;// force
    @SerializedName("Title")
    var title:String?=null;// force
    @SerializedName("Description")
    var description:String?=null;// force
    @SerializedName("Agreement")
    var agreement:String?=null; // not force
    @SerializedName("Latitude")
    var latitude:String?=null;// force
    @SerializedName("Longitude")
    var longitude:String?=null;// force
    @SerializedName("CategoryMarketID")
    var categoryMarketID:String?=null;// force
    @SerializedName("SubCategoryMarketID")
    var subCategoryMarketID:String?=null;// force

    @SerializedName("PriceAgree")
    var priceAgree:Boolean?=null;// force
    @SerializedName("Free")
    var free:Boolean?=null;// force
    @SerializedName("CommodityPrice")
    var commodityPrice:Long?=null;// gheymat kala // force

    @SerializedName("CityID")
    var cityID:String?=null;// force



    @SerializedName("DeleteURL")
    var deleteURL:List<String>?=null;
    @SerializedName("PhotoMarkets")
    var photoMarkets:List<PhotoMarkets>?=null;


}