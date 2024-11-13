package com.pedpo.pedporent.di.module

import android.content.Context
import androidx.room.Room
import com.pedpo.pedporent.room.dao.*
import com.pedpo.pedporent.room.dao.category.market.CategoryParentAndChildDAO
import com.pedpo.pedporent.room.dao.category.market.ChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.market.ParentCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceParentCategoryDAO
import com.pedpo.pedporent.room.dao.place.CityDAO
import com.pedpo.pedporent.room.dao.place.CountryDAO
import com.pedpo.pedporent.room.dao.place.ProvinceDAO
import com.pedpo.pedporent.room.pedpoDatabase.DataBasePedpo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataBaseRoomModule {


    @Singleton
    @Provides
    fun providerDataBase(@ApplicationContext context: Context): DataBasePedpo {
        return Room.databaseBuilder(context, DataBasePedpo::class.java,
            "db_app_pedpo_rent")
            //                .allowMainThreadQueries()
            //                .addMigrations(DataBaseRoom.migration_1_2)
            .fallbackToDestructiveMigration()
            .build();
    }

    @Singleton
    @Provides
    fun recentSearchDAO(dataBasePedpo: DataBasePedpo):RecentSearchDAO{
        return dataBasePedpo.recentSearchDAO()
    }

    @Singleton
    @Provides
    fun categoryParentAndChild(dataBasePedpo: DataBasePedpo): CategoryParentAndChildDAO {
        return dataBasePedpo.categoryParentAndChildDAO();
    }

    @Singleton
    @Provides
    fun marketParentCategory(dataBasePedpo: DataBasePedpo): ParentCategoryDAO {
        return dataBasePedpo.parentCategoryDAO()
    }

    @Singleton
    @Provides
    fun marketChildCategory(dataBasePedpo: DataBasePedpo): ChildCategoryDAO {
        return dataBasePedpo.childCategoryDAO();
    }

    @Singleton
    @Provides
    fun serviceParentCategory(dataBasePedpo: DataBasePedpo): ServiceParentCategoryDAO {
        return dataBasePedpo.parentCategoryServiceDAO();
    }
    @Singleton
    @Provides
    fun serviceChildCategory(dataBasePedpo: DataBasePedpo): ServiceChildCategoryDAO {
        return dataBasePedpo.childCategoryServiceDAO();
    }

                         /* Place *************/

    @Singleton
    @Provides
    fun cityDAO(dataBasePedpo: DataBasePedpo):CityDAO{
        return dataBasePedpo.cityDAO()
    }
    @Singleton
    @Provides
    fun provinceDAO(dataBasePedpo: DataBasePedpo):ProvinceDAO{
        return dataBasePedpo.provinceDAO()
    }
    @Singleton
    @Provides
    fun countryDAO(dataBasePedpo: DataBasePedpo):CountryDAO{
        return dataBasePedpo.countryDAO()
    }


}