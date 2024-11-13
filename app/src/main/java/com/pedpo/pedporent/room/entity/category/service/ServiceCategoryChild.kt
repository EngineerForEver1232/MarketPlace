package com.pedpo.pedporent.room.entity.category.service

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_category_service_child")
data class ServiceCategoryChild(
    @PrimaryKey(autoGenerate = false)
    var categoryMarketID:String,
    var categoryMarketName:String,
    var englishCategoryMarketName:String,
    var parentID:String,
)
