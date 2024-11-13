package com.pedpo.pedporent.room.entity.place

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "th_province")
data class ProvinceEntity(


    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var englishName: String,
    var parentID: String
)