package com.pedpo.pedporent.room.entity.place

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "th_country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String ,
    var name: String? = null,
    var englishName:String,
    var parentID:String?=null

)