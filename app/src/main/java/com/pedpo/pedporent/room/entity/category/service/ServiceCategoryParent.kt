package com.pedpo.pedporent.room.entity.category.service

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "th_category_service_parent")
data class ServiceCategoryParent (

    @PrimaryKey(autoGenerate = false)
    var categoryMarketID:String,
    var categoryMarketName:String,
    var englishCategoryMarketName:String,
    var appIconeAddress:String,
    var isAll:Boolean

)