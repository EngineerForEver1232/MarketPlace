package com.pedpo.pedporent.model.market

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO

class MarketPaginTO : Parcelable {


    constructor(){
        id = (++PaginTO.increment).toLong()
    }


    var id: Long = 0
    var marketID:String?=null;
    var title:String?=null;
    var photoAddress:String?=null;
    var registerDate:String?=null;
    var rentPriceDay:Int?=null;
    var free:Boolean?=null;
    var priceAgree:Boolean?=null;
    var isActive:Boolean?=null;

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        marketID = parcel.readString()
        title = parcel.readString()
        photoAddress = parcel.readString()
        registerDate = parcel.readString()
        rentPriceDay = parcel.readValue(Int::class.java.classLoader) as? Int
        free = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        priceAgree = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isActive = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(marketID)
        parcel.writeString(title)
        parcel.writeString(photoAddress)
        parcel.writeString(registerDate)
        parcel.writeValue(rentPriceDay)
        parcel.writeValue(free)
        parcel.writeValue(priceAgree)
        parcel.writeValue(isActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MarketPaginTO> {
        override fun createFromParcel(parcel: Parcel): MarketPaginTO {
            return MarketPaginTO(parcel)
        }

        override fun newArray(size: Int): Array<MarketPaginTO?> {
            return arrayOfNulls(size)
        }

        var increment = 0
        var DIFF_CALLBACK = object :DiffUtil.ItemCallback<MarketPaginTO>(){
            override fun areItemsTheSame(oldItem: MarketPaginTO, newItem: MarketPaginTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MarketPaginTO, newItem: MarketPaginTO): Boolean {
                return oldItem?.equals(newItem)
            }

        }
    }


}