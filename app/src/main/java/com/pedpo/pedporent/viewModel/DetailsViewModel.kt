package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.*
import com.pedpo.pedporent.model.comment.AddComment
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.details.DetailsData
import com.pedpo.pedporent.model.details.PhotoDetailsData
import com.pedpo.pedporent.model.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel : ViewModel {

    private var detailsRepository: DetailsRepository? = null;
    private var mutableComments:MutableLiveData<DataWrapper<CommentData>>?=null;

    @Inject
    constructor(detailsRepository: DetailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    fun detailPhotos(id: String,typeAPI :String): LiveData<DataWrapper<PhotoDetailsData>>? {
        return detailsRepository?.photoDetails(id, typeAPI = typeAPI);
    }
    fun detailMarkets(viewTO: ViewTO): LiveData<DataWrapper<DetailsData>>? {
        return detailsRepository?.detailsMarkets(viewTO);
    }

    fun comments(marketID:String,typeAPI: String?):LiveData<DataWrapper<CommentData>>{
        if (mutableComments==null)
        mutableComments = detailsRepository?.comments(marketID = marketID,typeAPI);
        return mutableComments!!
    }
    fun refreshComment(marketID:String,typeAPI: String?){
        mutableComments = detailsRepository?.comments(marketID = marketID,typeAPI);
    }

    fun sendComment(addComment: AddComment):LiveData<DataWrapper<ResponseTO>>{
        return detailsRepository?.sendComment(addComment = addComment)!!
    }

    fun addBookmark(marketID: String?,type:String?):LiveData<DataWrapper<BookmarkData>>?{
        return detailsRepository?.addBookmark(marketID = marketID,type)
    }

    fun view(viewTO: ViewTO):LiveData<DataWrapper<ResponseTO>>{
        return detailsRepository?.view(viewTO = viewTO)!!
    }
    fun like(viewTO: ViewTO):LiveData<DataWrapper<LikeData>>?{
        return detailsRepository?.like(viewTO = viewTO)
    }

    fun checkRenter(userID:String):LiveData<DataWrapper<RenterData>>?{
        return detailsRepository?.checkRenter(userID)
    }

    fun getStatusComment(marketID:String):LiveData<DataWrapper<ResponseComment>>?{
        return detailsRepository?.getStatusComment(marketID = marketID)
    }


}