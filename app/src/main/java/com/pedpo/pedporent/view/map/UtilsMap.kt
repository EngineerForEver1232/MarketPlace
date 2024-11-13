package com.pedpo.pedporent.view.map

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.pedpo.pedporent.R
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UtilsMap @Inject constructor(){


    fun cardSelected(card: MaterialCardView, img: ImageView,context: Context){
        card.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorPrimary
            )
        )
        img.setColorFilter(ContextCompat.getColor(context, R.color.white))
    }

    fun cardUnSelected(card: MaterialCardView, img: ImageView , context: Context){
        card.setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.white))
        img.setColorFilter(ContextCompat.getColor(context, R.color.tinticon))

    }


}