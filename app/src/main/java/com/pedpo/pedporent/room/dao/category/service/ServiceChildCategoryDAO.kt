package com.pedpo.pedporent.room.dao.category.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.category.ChildCategory
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryChild

@Dao
interface ServiceChildCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(childCategory: List<ServiceCategoryChild>):List<Long>

    @Query("select * from tb_category_service_child where parentID=:parentID")
    suspend fun selectChild(parentID:String?):List<ServiceCategoryChild>

    @Query("delete from tb_category_service_child")
    fun deleteAll()
}