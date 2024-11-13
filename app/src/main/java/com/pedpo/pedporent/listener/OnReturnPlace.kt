package com.pedpo.pedporent.listener

import com.pedpo.pedporent.model.place.PlaceTO

interface OnReturnPlace {
    fun onReturnPlace(placeTO: PlaceTO)
}