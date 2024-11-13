package com.pedpo.pedporent.room.pedpoDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pedpo.pedporent.model.profile.ProfileTO
import com.pedpo.pedporent.room.dao.*
import com.pedpo.pedporent.room.dao.category.market.CategoryParentAndChildDAO
import com.pedpo.pedporent.room.dao.category.market.ChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.market.ParentCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceChildCategoryDAO
import com.pedpo.pedporent.room.dao.category.service.ServiceParentCategoryDAO
import com.pedpo.pedporent.room.dao.place.CityDAO
import com.pedpo.pedporent.room.dao.place.CountryDAO
import com.pedpo.pedporent.room.dao.place.ProvinceDAO
import com.pedpo.pedporent.room.entity.RecentSearch
import com.pedpo.pedporent.room.entity.category.ChildCategory
import com.pedpo.pedporent.room.entity.category.ParentCategory
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryChild
import com.pedpo.pedporent.room.entity.category.service.ServiceCategoryParent
import com.pedpo.pedporent.room.entity.place.CityEntity
import com.pedpo.pedporent.room.entity.place.CountryEntity
import com.pedpo.pedporent.room.entity.place.ProvinceEntity


@Database(version = 26, exportSchema = true,
    entities = [
        RecentSearch::class,
        ParentCategory::class,
        ChildCategory::class,
        ServiceCategoryParent::class,
    ServiceCategoryChild::class,
    CityEntity::class ,
    ProvinceEntity::class ,
    CountryEntity::class
    ])
abstract class DataBasePedpo : RoomDatabase(){

    abstract fun recentSearchDAO():RecentSearchDAO;
    abstract fun childCategoryDAO(): ChildCategoryDAO;
    abstract fun parentCategoryDAO(): ParentCategoryDAO;
    abstract fun categoryParentAndChildDAO(): CategoryParentAndChildDAO;

    abstract fun parentCategoryServiceDAO(): ServiceParentCategoryDAO;
    abstract fun childCategoryServiceDAO():ServiceChildCategoryDAO;

    abstract fun cityDAO():CityDAO;
    abstract fun provinceDAO():ProvinceDAO;
    abstract fun countryDAO():CountryDAO;

}