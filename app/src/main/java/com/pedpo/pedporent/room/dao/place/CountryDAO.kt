package com.pedpo.pedporent.room.dao.place

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.place.CountryEntity

@Dao
interface CountryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CountryEntity>) : List<Long>

    @Query("select * from th_country")
    suspend fun selectCountry():List<CountryEntity>

    @Query("delete from th_country")
    fun deleteAll()

}