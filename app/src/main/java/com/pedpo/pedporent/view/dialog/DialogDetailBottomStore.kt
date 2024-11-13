package com.pedpo.pedporent.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogBottomSheetDetailStoreBinding
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.store.StoreDetials
import com.pedpo.pedporent.model.store.branche.BranchDetailData
import com.pedpo.pedporent.model.store.branche.BranchesData
import com.pedpo.pedporent.model.store.branche.time.TimeBranchData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.view.adapter.AdapterBranches
import com.pedpo.pedporent.view.adapter.ShiftTimeUserAdapter
import com.pedpo.pedporent.viewModel.BrancheStoreViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DialogDetailBottomStore : BottomSheetDialogFragment() ,AdapterBranches.OnClickAdapter ,
    OnMapReadyCallback , ReturnContent {

    private val viewModel: ProfileViewModel by viewModels()
    lateinit var binding: DialogBottomSheetDetailStoreBinding
    private val viewModelBranch : BrancheStoreViewModel by viewModels();
    private var googleMap: GoogleMap? = null;

    @Inject
    lateinit var adapterBranch: AdapterBranches
    @Inject
    lateinit var adapterTime:ShiftTimeUserAdapter
    private var storeID:String?=null;

    fun newInstance(storeID: String?): DialogDetailBottomStore {

        val fragment = DialogDetailBottomStore();
        val bundle = Bundle();
        bundle.putString(ContextApp.STORE_ID, storeID)
        fragment.arguments = bundle;
        return fragment;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

        storeID = arguments?.getString(ContextApp.STORE_ID)?:""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetDetailStoreBinding.inflate(inflater);
        binding.listener = this
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerBranches.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.recyclerBranches.adapter = adapterBranch
        adapterBranch.setOnClickAdapter(this)

        showMap()
        detailStore()

        branches()

        binding.recyclerTime.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL ,false);
//        adapterTime.setDialog(this)
        binding.recyclerTime.adapter = adapterTime;

    }


    fun branches(){
        Log.i("testAddressBranche", "storeID: "+arguments?.getString(ContextApp.STORE_ID))

        viewModelBranch.branches(storeID = arguments?.getString(ContextApp.STORE_ID)?:"noID")?.observe(this ,
            CustomObserver(object :CustomObserver.ResultListener<BranchesData>{
                override fun onSuccess(dataInput: BranchesData) {
                    Log.i("testAddressBranche", "onSuccess: ${dataInput.isSuccess}")
                    Log.i("testAddressBranche", "onSuccess: ${dataInput.data?.size}")
                    if (dataInput.isSuccess == true){
                        adapterBranch.updateAdapter(dataInput.data?: arrayListOf())
                    }else{

                    }
                }

                override fun onException(exception: Exception) {

                }

            }))
    }


    private fun detailStore() {

        val liveData = if (arguments?.getString(ContextApp.STORE_ID).isNullOrEmpty())
            viewModel.detailStore()
        else {
            Log.i("detialStore", "STORE_ID : ")

            viewModel.detailStore(storeID = arguments?.getString(ContextApp.STORE_ID))
        }
        liveData?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<StoreDetials> {
                override fun onSuccess(data: StoreDetials) {

                    if (data.isSuccess == true) {

                        val storeData = data.data;

                        binding.viewModel = storeData;

//                        Log.i("responseStore", "inner: ${storeData?.inner}")
//                        Log.i("responseStore", "onner: ${storeData?.onner}")

                        Picasso.get().load(data.data?.logo).into(binding.icon)

                        binding.tTitle.isVisible = !storeData?.title.isNullOrEmpty()
//                        binding.tTitle.text = storeData?.title

                        binding.tDescription.isVisible = !storeData?.description.isNullOrEmpty()
                        binding.lableDescription.isVisible =
                            !storeData?.description.isNullOrEmpty()
//                        binding.tDescription.text = storeData?.description

                        binding.tLocation.isVisible = !storeData?.cityName.isNullOrEmpty()
                        binding.lableLocation.isVisible = !storeData?.cityName.isNullOrEmpty()
                        binding.dotdotLocation.isVisible = !storeData?.cityName.isNullOrEmpty()
//                        binding.tLocation.text = storeData?.cityName

                        binding.tPhone.isVisible = !storeData?.phone.isNullOrEmpty()
                        binding.lablePhone.isVisible = !storeData?.phone.isNullOrEmpty()
                        binding.dotdotPhone.isVisible = !storeData?.phone.isNullOrEmpty()
//                        binding.tPhone.text = storeData?.phone

                        binding.tPermission.isVisible = !storeData?.madrak.isNullOrEmpty()
                        binding.lablePermission.isVisible = !storeData?.madrak.isNullOrEmpty()
                        binding.dotPermission.isVisible = !storeData?.madrak.isNullOrEmpty()
                        binding.tPermission.text = getString(R.string.accept)
                        Log.i("detialStore", "photosStore: ${storeData?.madrak}")

                        binding.tPermission.setOnClickListener {
                            if (!storeData?.madrak.isNullOrEmpty()) {
                                val showImageDialogPermission = DialogFragmentShowImage().newInstance(null,storeData?.madrak);
                                showImageDialogPermission.show(childFragmentManager, "permission");
                            }
                        }

                        if (storeData?.inner == false && storeData.onner == false){
                            binding.tSend.isVisible  = false
                            binding.lableSend.isVisible  = false
                            binding.dotdotSend.isVisible  = false
                        }


                        val str = StringBuilder();
                        if (storeData?.inner == true && storeData.onner == true )
                            str.append("inner city - outer city")
                        else if (storeData?.inner == true && storeData.onner == false)
                            str.append("inner city")
                        else if (storeData?.inner == false && storeData.onner == true)
                            str.append("outer city ")
                        binding.tSend.text = str ;

                    }
                }

                override fun onException(exception: Exception) {
                    Log.i("detialStore", "photosStore: ${exception.message}")

                }

            })
        )

    }

    override fun onclickAdapter(branchesTO: BranchesData.BranchesTO, position: Int) {
//        binding.nestedDetail.isInvisible = true ;
//        binding.nestedTime.isInvisible = false ;
        binding.nestedDetail.isVisible = false ;
        binding.nestedTime.isVisible = true ;
        binding.tTitle.text = branchesTO.name ;

        getTimeBranch(branchID = branchesTO.branchID)

        branchDetail(branchID = branchesTO.branchID)

        val bottomSheetDialog = dialog as BottomSheetDialog;

        BottomSheetBehavior.from(bottomSheetDialog.findViewById<FrameLayout>(
            com.google.android.material.R.id.design_bottom_sheet
        ) as FrameLayout)
            .state = BottomSheetBehavior.STATE_EXPANDED
    }


    fun getTimeBranch(branchID:String?){

        viewModelBranch.getTimeBranch(branchID = branchID?:"")?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<TimeBranchData>{
                override fun onSuccess(dataInput: TimeBranchData) {

                    Log.i("testTimeBranche", "onSuccess WorkTime: ${dataInput.data?.workTimeID}")

                    Log.i("testTimeBranche", "onSuccess: ${dataInput.isSuccess}")
                    Log.i("testTimeBranche", "size: ${dataInput.data?.workTimes?.size}")
                    Log.i("testTimeBranche", "onSuccess: ${dataInput.data?.workTimeID}")

                    if (dataInput.isSuccess == true){
                        if (!dataInput.data?.workTimeID.isNullOrEmpty()) {
                            adapterTime.updateAdapter(
                                dataInput.data?.workTimes ?: arrayListOf(),
                                dataInput.data?.workTimeID ?: ""
                            )
                        }
                    }else{

                    }

                }

                override fun onException(exception: Exception) {

                }

            }))
    }


    fun branchDetail(branchID:String?){

        viewModelBranch.branchDetail(branchID = branchID?:"")?.observe(this,
        CustomObserver(object :CustomObserver.ResultListener<BranchDetailData>{
            override fun onSuccess(dataInput: BranchDetailData) {
                if (dataInput.isSuccess == true){

                    val data = dataInput.data;

                    binding.tAddress.text = data?.address ;
                    binding.titleBranch.text = data?.name ;

                    Log.i("testMap", "${data?.latitude} ${data?.longitude}")
                    if (data?.latitude.isNullOrEmpty() && data?.longitude.isNullOrEmpty())
                        binding.carViewMap.isVisible = false;
                    else {
                        binding.carViewMap.isVisible = true;
                        placeMap(
                            LatLng(
                                data?.latitude?.toDouble() ?: 29.4963,
                                data?.longitude?.toDouble() ?: 60.8629
                            ),
                            10f
                        )
                    }
                }

            }

            override fun onException(exception: Exception) {

            }

        }))

    }

    /*onClick Back One*/
    fun onClickClose(view: View){
        dismiss()
    }

    /* onClick Back */
    fun onClickBack(view: View){

        if (binding.nestedDetail.isVisible){
            dismiss()
        }else{
            val bottomSheetDialog = dialog as BottomSheetDialog;

            BottomSheetBehavior.from(bottomSheetDialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as FrameLayout)
                .state = BottomSheetBehavior.STATE_COLLAPSED


            binding.nestedDetail.isVisible = true ;
            binding.nestedTime.isVisible = false ;
        }

    }

    /*onClickRate*/
    fun onClickRate(view: View){
        val dialogRate = RateDialogFragment().newInstance(storeID = storeID?:"")
        dialogRate.setReturn(this)
        dialogRate.show(childFragmentManager,"Rate")
    }

    override fun returnContent(content: String?) {
//        getStore(categoryID = categoryID,"")
            detailStore() ;
    }

    fun showMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapAddress) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap;
        placeMap(LatLng(29.4963, 60.8629), 10f)
    }


    fun placeMap(latLng: LatLng, zoom: Float) {
        googleMap?.clear()
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to Mountain View
            .zoom(zoom)
            .build() // Creates a CameraPosition from the builder

        googleMap?.addMarker(
            MarkerOptions()
                .position(latLng))

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

    }

}