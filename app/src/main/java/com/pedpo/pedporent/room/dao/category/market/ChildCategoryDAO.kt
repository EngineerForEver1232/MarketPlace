package com.pedpo.pedporent.room.dao.category.market

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.category.ChildCategory

@Dao
interface ChildCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(childCategory: List<ChildCategory>):List<Long>

    @Query("select * from tb_category_child where parentID=:parentID")
    suspend fun selectChild(parentID:String?):List<ChildCategory>


    @Query("delete from tb_category_child")
    fun deleteAll()
}