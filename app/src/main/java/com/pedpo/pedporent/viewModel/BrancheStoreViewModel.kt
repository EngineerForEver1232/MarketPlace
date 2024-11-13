package com.pedpo.pedporent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pedpo.pedporent.model.DataWrapper
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.repository.BranchStoreRepository
import com.pedpo.pedporent.model.store.branche.*
import com.pedpo.pedporent.model.store.branche.add.AddAddressBrancheTO
import com.pedpo.pedporent.model.store.branche.AddressBranchData
import com.pedpo.pedporent.model.store.branche.add.AddBranchData
import com.pedpo.pedporent.model.store.branche.time.EnableTimeResponse
import com.pedpo.pedporent.model.store.branche.time.EnableWorkTime
import com.pedpo.pedporent.model.store.branche.time.TimeBranchData
import com.pedpo.pedporent.model.store.branche.time.TimeBranchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrancheStoreViewModel : ViewModel {

    private var repository:BranchStoreRepository?=null;

    @Inject
    constructor(repository: BranchStoreRepository){
        this.repository = repository;
    }

    fun addAdressBranche(addAddressBrancheTO: AddAddressBrancheTO):LiveData<DataWrapper<AddBranchData>>?{
        return repository?.addAddressBranch(addAddressBrancheTO)
    }
    fun addTimeBranch(timeBranchRequest: TimeBranchRequest):LiveData<DataWrapper<ResponseTO>>?{
        return repository?.addTimeBranch(timeBranchRequest)
    }
    fun enableWorkTime(enableWorkTime: EnableWorkTime):LiveData<DataWrapper<EnableTimeResponse>>?{
        return repository?.enableWorkTime(enableWorkTime)
    }
    fun deleteBranch(branchID: String):LiveData<DataWrapper<ResponseTO>>?{
        return repository?.deleteBranch(branchID)
    }
    fun deleteWorkTime(deleteTimeRequest: DeleteTimeRequest):LiveData<DataWrapper<ResponseTO>>?{
        return repository?.deleteWorkTime(deleteTimeRequest)
    }
    fun editAdressBranche(addAddressBrancheTO: AddAddressBrancheTO):LiveData<DataWrapper<ResponseTO>>?{
        return repository?.editAddressBranch(addAddressBrancheTO)
    }
    fun getAdressBranche(branchID:String):LiveData<DataWrapper<AddressBranchData>>?{
        return repository?.getAddressBranch(brancheID = branchID)
    }
    fun branchDetail(branchID:String):LiveData<DataWrapper<BranchDetailData>>?{
        return repository?.branchDetail(brancheID = branchID)
    }
    fun getTimeBranch(branchID:String):LiveData<DataWrapper<TimeBranchData>>?{
        return repository?.getTimeBranch(brancheID = branchID)
    }
    fun branches(storeID:String?):LiveData<DataWrapper<BranchesData>>?{
        return repository?.branches(storeID = storeID);
    }
}