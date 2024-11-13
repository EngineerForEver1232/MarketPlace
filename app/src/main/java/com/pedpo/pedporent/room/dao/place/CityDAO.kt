package com.pedpo.pedporent.room.dao.place

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.place.CityEntity
import com.pedpo.pedporent.room.entity.place.CountryEntity
import com.pedpo.pedporent.room.entity.place.ProvinceEntity

@Dao
interface CityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CityEntity>) : List<Long>

    @Query("select * from th_city where parentID=:cityID and ( name like '%' || :title || '%' OR englishName like '%' || :title || '%' ) ")
    suspend fun selectCity(cityID:String?,title:String):List<CityEntity>


    @Query("select * from th_city")
    suspend fun selectCityAll():List<CityEntity>

    @Query("delete from th_city")
    fun deleteAll()
}