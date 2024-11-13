package com.pedpo.pedporent.room.entity.category.service

import androidx.room.Embedded
import androidx.room.Relation

data class ServiceParentAndChild (

    @Embedded
    val parentCategory: ServiceCategoryParent,
    @Relation(
        parentColumn = "categoryMarketID",
        entityColumn = "parentID"
    )
    val childCategory: List<ServiceCategoryChild>

        )
