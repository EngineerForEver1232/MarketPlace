package com.pedpo.pedporent.model.market

import com.google.gson.annotations.SerializedName

class ShowMarketTO {

     var CityID:String?=null;
     var CategoryID:String?=null;
     @SerializedName("Type")
     var type:String?=null;

     constructor(cityID:String,categoryID:String?,type: String?){
          this.CityID = cityID;
          this.CategoryID = categoryID;
          this.type = type;
     }
//     constructor(cityID:String,categoryID:String?){
//          this.CityID = cityID;
//          this.CategoryID = categoryID;
//     }
     constructor(cityID:String){
          this.CityID = cityID;
     }


     var marketID:String?=null;
     var title:String?=null;
     var photoAddress:String?=null;
     var registerDate:String?=null;
     var rentPriceDay:Int?=null;
     var free:Boolean?=null;
     var priceAgree:Boolean?=null;
     var isActive:Boolean?=null;


}