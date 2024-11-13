package com.pedpo.pedporent.room.entity.category

import androidx.room.Embedded
import androidx.room.Relation

data class ParentAndChild(
    @Embedded
    val parentCategory: ParentCategory,
    @Relation(
        parentColumn = "categoryMarketID",
        entityColumn = "parentID"
    )
    val childCategory: List<ChildCategory>

)