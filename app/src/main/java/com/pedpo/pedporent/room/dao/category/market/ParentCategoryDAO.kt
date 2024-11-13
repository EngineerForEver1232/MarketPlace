package com.pedpo.pedporent.room.dao.category.market

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedpo.pedporent.room.entity.category.ParentCategory

@Dao
interface ParentCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(parentCategory: List<ParentCategory>):List<Long>;

    @Query("select * from tb_category_parent")
    suspend fun select():List<ParentCategory>

    @Query("delete from tb_category_parent")
    fun deleteAll()

}