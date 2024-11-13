package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailsData
import com.pedpo.pedporent.model.repository.DetailsNeighborRepository
import com.pedpo.pedporent.model.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsNeighborViewModel @Inject constructor(private val detailsRepository: DetailsNeighborRepository) : ViewModel() {


    fun detailPhotos(id: String): LiveData<DataWrapper<PhotoDetailsData>> {
        return detailsRepository?.photoDetails(id)!!;
    }
    fun detailMarkets(viewTO: ViewTO): LiveData<DataWrapper<DetailsData>> {
        return detailsRepository?.detailsMarkets(viewTO)!!;
    }


//    fun addBookmark(marketID: String?):LiveData<DataWrapper<BookmarkData>>{
//        return detailsRepository?.addBookmark(marketID = marketID)!!
//    }

//    fun view(viewTO: ViewTO):LiveData<DataWrapper<ResponseTO>>{
//        return detailsRepository?.view(viewTO = viewTO)!!
//    }
//    fun like(viewTO: ViewTO):LiveData<DataWrapper<LikeData>>{
//        return detailsRepository?.like(viewTO = viewTO)!!
//    }

    fun checkRenter(userID:String):LiveData<DataWrapper<RenterData>>{
        return detailsRepository?.checkRenter(userID)!!
    }

}