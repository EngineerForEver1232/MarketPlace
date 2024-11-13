package com.pedpo.pedporent.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "th_recentSearch", indices = [Index(value = arrayOf("text"), unique = true)])
//@Entity(tableName = "th_recentSearch")
data class RecentSearch(
    var text:String,
    var nameCategory:String,
    var idCategory:String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null;
}

