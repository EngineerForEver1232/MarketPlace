package com.pedpo.pedporent.room.dao.category.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryParent

@Dao
interface ServiceParentCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(serviceCategoryParent: List<ServiceCategoryParent>):List<Long>;

    @Query("select * from th_category_service_parent")
    suspend fun select():List<ServiceCategoryParent>;

    @Query("delete from th_category_service_parent")
    fun deleteAll()
}