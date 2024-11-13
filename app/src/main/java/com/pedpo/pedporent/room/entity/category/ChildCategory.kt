package com.pedpo.pedporent.room.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_category_child")
data class ChildCategory (

    @PrimaryKey(autoGenerate = false)
    var categoryMarketID:String,
    var categoryMarketName:String,
    var englishCategoryMarketName:String,
    var parentID:String,
//    var type:String

)
//{
//
//    @PrimaryKey(autoGenerate = true)
//    var childID:Int?=null
//}