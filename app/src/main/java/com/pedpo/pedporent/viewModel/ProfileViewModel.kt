package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.bookmark.BookmarkData
import com.pedpo.pedporent.model.myItems.MyItemsData
import com.pedpo.pedporent.model.myItems.MyMarkets
import com.pedpo.pedporent.model.poster.PosterData
import com.pedpo.pedporent.model.profile.CalendarData
import com.pedpo.pedporent.model.profile.ProfileData
import com.pedpo.pedporent.model.renterMarket.RenterMarketData
import com.pedpo.pedporent.model.repository.ProfileRepository
import com.pedpo.pedporent.model.store.*
import com.pedpo.pedporent.model.store.category.CategoryStoreData
import com.pedpo.pedporent.model.store.edit.ResponseEditStorePhotos
import com.pedpo.pedporent.model.store.storeList.StoreListData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel : ViewModel {

    private var profileRepository: ProfileRepository? = null;

    @Inject
    constructor(profileRepository: ProfileRepository) {
//        if (this.profileRepository == null)
            this.profileRepository = profileRepository;
    }

    fun profile(): LiveData<DataWrapper<ProfileData>>? {
        return profileRepository?.profile();
    }

    fun setFirstName(firstName: String): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setFirstName(firstName = firstName)
    }

    fun setLastName(lastName: String): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setLastName(firstName = lastName)
    }

    fun setCityID(cityID: String?): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setCityID(cityID = cityID)
    }

    fun setImageProfile(requestProfilePhoto: RequestProfilePhoto): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setImageProfile(requestProfilePhoto)
    }

    fun myItems(): LiveData<DataWrapper<MyItemsData>>? {
        return profileRepository?.myItems();
    }

    fun bookmarks(): LiveData<DataWrapper<BookmarkData>>? {
        return profileRepository?.bookmarks();
    }

    fun deleteMarket(myMarket: MyMarkets?): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.deleteMarket(myMarket)
    }

    fun deActiveMarket(activeMarketTO: ActiveMarketTO?): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.deActiveMarket(activeMarketTO)
    }

    fun renterMarkets(userID: String, ip: String): LiveData<DataWrapper<RenterMarketData>>? {
        return profileRepository?.renterMarkets(userID = userID, ip);
    }

    fun datesDeActive(marketID: String?, type: String?): LiveData<DataWrapper<CalendarData>>? {
        return profileRepository?.datesDeActive(marketID = marketID, type = type);
    }

    fun activeMarket(marketID: String?, type: String?): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.active(marketID = marketID, type = type);
    }

    /** Stoe **/

    fun createStore(storeRequest: StoreRequest?): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.createRepository(storeRequest)
    }

    fun photosStore(): LiveData<DataWrapper<ResponseStorePhotos>>? {
        return profileRepository?.photosStore()
    }
    fun photosStoreUser(storeID:String?): LiveData<DataWrapper<ResponseStorePhotos>>? {
        return profileRepository?.photosStoreUser(storeID = storeID)
    }

    fun detailStore(): LiveData<DataWrapper<StoreDetials>>? {
        return profileRepository?.detailStore()
    }
    fun detailStore(storeID: String?): LiveData<DataWrapper<StoreDetials>>? {
        return profileRepository?.detailStore(storeID = storeID)
    }

    fun editStore(request: StoreRequestEdit): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.editStore(request = request)
    }
    fun getEditStorePhotos(): LiveData<DataWrapper<ResponseEditStorePhotos>>? {
        return profileRepository?.getEditStorePhotos()
    }
    fun setEditStore(request: StoreDetailEditRequest): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setEditStore(request = request)
    }
    fun setEditStorePhotos(request: StorePhotoslEditRequest): LiveData<DataWrapper<ResponseTO>>? {
        return profileRepository?.setEditStorePhotos(request = request)
    }

    fun getCategoryStore(): LiveData<DataWrapper<CategoryStoreData>>? {
        return profileRepository?.getCategoryStore();
    }

    fun getStoreList(categoryID: String,title:String?): LiveData<DataWrapper<StoreListData>>? {
        return profileRepository?.getStore(categoryID = categoryID,title=title)
    }
    fun poster():LiveData<DataWrapper<PosterData>>?{
        return profileRepository?.poster()
    }

    fun isStore():LiveData<DataWrapper<IsStore>>?{
        return profileRepository?.isStore()
    }
    fun setRate(rateTO:RateStoreTO):LiveData<DataWrapper<ResponseTO>>?{
        return profileRepository?.setRateStore(rateTO = rateTO)
    }
    fun closeComment(marketID:String):LiveData<DataWrapper<ResponseTO>>?{
        return profileRepository?.closeComment(marketID = marketID)
    }
    fun getStatusComment(marketID:String):LiveData<DataWrapper<ResponseComment>>?{
        return profileRepository?.getStatusComment(marketID = marketID)
    }

}