package com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model

import androidx.recyclerview.widget.DiffUtil

class PaginTO {
    companion object{
        var increment = 0
        var DIFF_CALLBACK = object :DiffUtil.ItemCallback<PaginTO>(){
            override fun areItemsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PaginTO, newItem: PaginTO): Boolean {
                return oldItem?.equals(newItem)
            }
        }
    }
    constructor(){
        id = (++increment).toLong()
    }

    var id: Long = 0
    var marketID:String?=null;
    var neighborMarketID:Long?=null;
    var title:String?=null;
    var description:String?=null
    var photoAddress:String?=null;
    var logo:String?=null;
    var showType:String?=null;
    var priceType:String?=null;
    var registerDate:String?=null;
    var nameSite:String?=null;
    var type:String?=null;
    var place:String?=null;
    var rentPriceDay:Long?=null;
    var free:Boolean?=null;
    var priceAgree:Boolean?=null;
    var isActive:Boolean?=null;
    var isBookMarkByUser:Boolean?=null;
    var isLikeByIp:Boolean?=null;
    var isService:Boolean?=null;
    var startDate:String?=null;
    var endDate:String?=null;

    var rooms:String?=null;
    var bathrooms:String?=null;
    var meterOfHouse:String?=null;
    var isHome:Boolean?=false;

    var isCar:Boolean?=false;
    var fuelType:String?=null;
    var yearOfCar:String?=null;
    var kilometerOfCar:String?=null;

    var startTimeInactive:String?=null;
    var endTimeInactive:String?=null;

}