package com.pedpo.pedporent.room.dao

import androidx.room.*
import com.pedpo.pedporent.room.entity.RecentSearch

@Dao
interface RecentSearchDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearch: RecentSearch):Long

    @Delete
    suspend fun delete(recentSearch: RecentSearch)

    @Query("delete from th_recentSearch")
    suspend fun deleteTable()

    @Query("select * from th_recentSearch")
    suspend fun select():List<RecentSearch>?

}