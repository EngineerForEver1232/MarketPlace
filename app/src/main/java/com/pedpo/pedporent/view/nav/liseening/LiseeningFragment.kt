package com.pedpo.pedporent.view.nav.liseening

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.FragmentLiseeningBinding
import com.pedpo.pedporent.listener.OnClickItemAdapterLiseen
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.filter.FilterTransfer
import com.pedpo.pedporent.model.liseening.LiseeningData
import com.pedpo.pedporent.model.liseening.LiseeningTO
import com.pedpo.pedporent.model.liseening.RequestLiseening
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.RecyclerItemTouchHelper
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.ListenerAdapter
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.filter.FilterActivity
import com.pedpo.pedporent.view.paging.liseening.activity.LiseeningActivty
import com.pedpo.pedporent.viewModel.LiseeningViewModel
import com.pedpo.pedporent.widget.ButtonSwitch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LiseeningFragment : Fragment() ,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener ,
OnClickItemAdapterLiseen{

    var typeMarket = ContextApp.RENT;
    lateinit var binding: FragmentLiseeningBinding;

    @Inject
    lateinit var prefApp: PrefApp
    @Inject
    lateinit var adapterListener: ListenerAdapter;

    private val liseeningViewModel: LiseeningViewModel by viewModels()
    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLiseeningBinding.inflate(layoutInflater)
        binding.listener=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterListener.onClickItemAdapterLiseen = this;

        binding.constraint.isVisible = !prefApp.getToken().isNullOrEmpty();
        binding.layoutIncludeLogin.constraintDoLogin.isVisible = prefApp.getToken().isNullOrEmpty();
        binding.layoutIncludeLogin.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        getLiseening(typeMarket)


        binding.recyclerListener.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false);
            adapter = adapterListener ;
        }

        val recyclerItemTouchHelper = RecyclerItemTouchHelper(0, ItemTouchHelper.END)
        recyclerItemTouchHelper.setListener(this)
        val ith = ItemTouchHelper(recyclerItemTouchHelper)
        //        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(binding.recyclerListener)

        binding.btnAddLiseening1.setOnClickListener {
            onClickAddLiseening();
        }
    }

    var listLiseening : List<LiseeningTO>?=null;

    fun getLiseening(type:String?){
        liseeningViewModel.getLiseenings(type = type)
            ?.observe(viewLifecycleOwner,CustomObserver(object : CustomObserver.ResultListener<LiseeningData>{
                override fun onSuccess(dataInput: LiseeningData) {

                    if (dataInput.isSuccess ){

                        if (dataInput.data?.isEmpty()==true){
                            binding.included.layoutError.isVisible = true;
                            binding.recyclerListener.isVisible = false;
                            binding.included.labelError.text = getString(R.string.no_items)
                            binding.btnAddLiseening1.isVisible = false;
                        }else {
                            listLiseening = dataInput.data
                            adapterListener.update(dataInput.data as ArrayList<LiseeningTO>? ?: arrayListOf())
                            binding.btnAddLiseening1.isVisible = true;

                            binding.included.layoutError.isVisible = false
                            binding.recyclerListener.isVisible = true;
                        }


                    }

                }

                override fun onException(exception: Exception) {


                }
            }))
    }


    /*OnClick onClick Button Category Rent */
    fun onClickBtnCategoryRent(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.RENT, typeCategory)
            typeMarket = ContextApp.RENT
            getLiseening(ContextApp.RENT)
        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tr,binding.tS,binding.tService,
            binding.lineBottomRent,binding.lineBottomSale,binding.lineBottomService,
            binding.img,binding.imgSale, binding.imgService,1
        )
    }

    /*OnClick onClick Button Category Sale */
    fun onClickBtnCategorySale(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.SALE,typeCategory)
            typeMarket = ContextApp.SALE
            getLiseening(ContextApp.SALE)

        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tS,binding.tr,binding.tService,
            binding.lineBottomSale,binding.lineBottomRent,binding.lineBottomService,
            binding.imgSale,binding.img, binding.imgService,2
        )
    }

    /*OnClick onClick Button Category Service */
    fun onClickBtnCategoryService(view: View) {

        Handler(Looper.getMainLooper()).postDelayed({
//            loadDatas(ContextApp.SERVICE,typeCategory)
            typeMarket = ContextApp.SERVICE
            getLiseening(ContextApp.SERVICE)

        }, getString(R.integer.duration_anim).toLong())

        ButtonSwitch(requireContext()).btnSwiche(
            binding.tService,binding.tr,binding.tS,
            binding.lineBottomService,binding.lineBottomRent,binding.lineBottomSale,
            binding.imgService,binding.img, binding.imgSale,3
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        // Log.e("swiped", "onSwiped: " + direction);

        val itemDelete = listLiseening?.get(viewHolder?.adapterPosition?:0)
        val positionDelete = viewHolder?.adapterPosition

        adapterListener?.removeItem(positionDelete)


        var checkDelete = true
        if (positionDelete != null) {
            adapterListener.notifyItemRemoved(positionDelete)
        }


        val snackbar: Snackbar = Snackbar
            .make(
                binding?.coordinator,
//                itemDelete.getOtherUserName() +
                getString(R.string.removed_inbox),
                Snackbar.LENGTH_LONG
            )
        snackbar.setAction(resources.getString(R.string.undo)) { // undo is selected, restore the deleted item
            checkDelete = false
            adapterListener.restoreItem(itemDelete, positionDelete)
        }
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.addCallback(
            object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (checkDelete)
                        deleteLiseening(itemDelete?.id)

                }
            }
        )
        snackbar.show()

    }

    fun onClickAddLiseening(){
        val intent = Intent(requireContext(), FilterActivity::class.java)
        intent.putExtra(ContextApp.PAGE, ContextApp.LISEENING)
        intent.putExtra(ContextApp.TYPE_API, typeMarket)
        launchLiseening.launch(intent)
    }

    var launchLiseening = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        when (result.resultCode) {

            1 ->{

                val filterTransfer = result.data?.getSerializableExtra("result") as FilterTransfer?;

                Log.i("testFilter",  "fragment \r\n title ${filterTransfer?.title} " +
                        "\n cityId ${filterTransfer?.cityID}" +
                        "\n category ${filterTransfer?.categoryID} " +
                        "\n type ${filterTransfer?.typePrice ?: ""}         " +
                        "\n priceFrom ${filterTransfer?.priceFrom ?: null}" +
                        "\n priceTO ${filterTransfer?.priceTo ?: null}" +
                        "\n agree  ${filterTransfer?.priceAgree ?: false}" +
                        "\n free ${filterTransfer?.free ?: false}" +
                        "\n date  ${filterTransfer?.registerDate ?: null}" +
                        "\n advanced  ${filterTransfer?.typeAdvanced ?: null}" +
                        "\n ip ${filterTransfer?.iP}"
                )
                var requestLiseen = RequestLiseening(
                    type = filterTransfer?.typePrice?:"",
                    cityID = filterTransfer?.cityID?:"",
                    categoryID = filterTransfer?.categoryID?:"",
                    priceFrom = filterTransfer?.priceFrom?:0,
                    priceTo = filterTransfer?.priceTo?:0,
                    priceAgree = filterTransfer?.priceAgree?:false,
                    free = filterTransfer?.free?:false,
                    typeOFPrice = filterTransfer?.typeOFPrice?:""
                )


                liseeningViewModel?.createLiseening(liseening = requestLiseen)?.observe(this,
                    CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
                        override fun onSuccess(dataInput: ResponseTO) {
                            getLiseening(type = typeMarket)
                        }

                        override fun onException(exception: Exception) {

                        }

                    })
                )

            }

        }

    }

    fun deleteLiseening(id:String?){
        liseeningViewModel?.deleteLiseening(id)?.observe(viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
                override fun onSuccess(dataInput: ResponseTO) {
                        getLiseening(type = typeMarket)
                }

                override fun onException(exception: Exception) {

                }

            }))
    }

    override fun onItemclickAdapterLiseening(liseeningTO: LiseeningTO?) {

        var intent = Intent(requireContext(),LiseeningActivty::class.java);
        intent?.putExtra(ContextApp.MARKET_ID,liseeningTO?.id)
        startActivity( intent )
    }

}