package com.pedpo.pedporent.room.dao.place

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.place.CountryEntity
import com.pedpo.pedporent.room.entity.place.ProvinceEntity

@Dao
interface ProvinceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ProvinceEntity>) : List<Long>

    @Query("select * from th_province where parentID =:countryID and ( name LIKE  '%' || :title || '%' OR englishName LIKE  '%' || :title || '%' )")
    suspend fun selectProvince(countryID:String?,title:String):List<ProvinceEntity>

    @Query("select * from th_province")
    suspend fun selectProvinceALL():List<ProvinceEntity>

    @Query("delete from th_province")
    fun deleteAll()
}