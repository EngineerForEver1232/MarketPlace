package com.pedpo.pedporent.room.entity.category

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedpo.pedporent.model.market.category.CategoryTO

@Entity(tableName = "tb_category_parent" )
data class ParentCategory(

    @PrimaryKey(autoGenerate = false)
     var categoryMarketID: String,
    var categoryMarketName: String,
    var englishCategoryMarketName: String,
    var appIconeAddress: String,
    var isHome: Boolean,
    var isCar: Boolean,
    var isAll: Boolean


)