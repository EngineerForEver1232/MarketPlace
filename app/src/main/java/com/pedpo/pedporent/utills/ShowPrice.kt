package com.pedpo.pedporent.utills

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.model.model.MapData
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import javax.inject.Inject


class ShowPrice @Inject constructor(private val numberFormat: NumberFormatDigits) {

    fun showPrice(paginTO:PaginTO) : String{

        val tPrice = StringBuilder();

        if (paginTO.type == ContextApp.RENT) {

            tPrice.append("$")
            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
            tPrice.append(" / ")
            tPrice.append(paginTO.priceType)
        }
        else {
            tPrice.append(" $ ")
            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
        }

        return tPrice.toString()

    }
    fun showPriceMap(mapData: MapData) : String{

        val tPrice = StringBuilder();


        if (mapData.type == ContextApp.RENT) {

            tPrice.append("$")
            tPrice.append(numberFormat.convertToDigist(mapData.rentPriceDay)?:"")
            tPrice.append(" / ")
            tPrice.append(mapData.priceType)
        }
        else {
            tPrice.append(" $ ")
            tPrice.append(numberFormat.convertToDigist(mapData.rentPriceDay)?:"")
        }

        return tPrice.toString()

    }

    fun showPriceTest(paginTO:PaginTO,tv:MaterialTextView) {

        val tPrice = StringBuilder();

        val dollarString = SpannableString("$")
        val foregroundSpan = ForegroundColorSpan(Color.RED)
        dollarString.setSpan(foregroundSpan, 0, dollarString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val price = SpannableString(numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0))
        price.setSpan(ForegroundColorSpan(Color.BLACK), 0, dollarString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val slashString = SpannableString("/")
        dollarString.setSpan(ForegroundColorSpan(Color.BLUE), 0, dollarString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val typePrice = SpannableString(paginTO.priceType)
        dollarString.setSpan(ForegroundColorSpan(Color.RED), 0, dollarString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)



//        tv.text = "$dollarString $price $slashString $typePrice"
        tv.text = typePrice

//        if (paginTO.type == ContextApp.RENT) {
//            spannable
//                .append("$", StyleSpan(R.style.spanStyleRed), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//                .append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}",
//                    StyleSpan(R.style.spanStyleRed), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//                .append(" /  ", StyleSpan(R.style.spanStylePrimaty), Spanned.SPAN_PARAGRAPH)
//                .append(paginTO.priceType)
//
//            tPrice.append("$")
//            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
//            tPrice.append(" / ")
//            tPrice.append(paginTO.priceType)
//        }
//        else {
//            tPrice.append(" $ ")
//            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
//        }
//
//        tv.text = spannable.toString()
    }


    fun showPriceMyItem(paginTO: MyMarkets) : String{

        val tPrice = StringBuilder();
        if (paginTO.type == ContextApp.RENT) {
            tPrice.append(" $ ")
            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
            tPrice.append(" / ")
            tPrice.append(paginTO.priceType)
        }
        else {
            tPrice.append(" $ ")
            tPrice.append("${numberFormat.convertToDigist(paginTO.rentPriceDay ?: 0)}")
        }

        return tPrice.toString()

    }

}