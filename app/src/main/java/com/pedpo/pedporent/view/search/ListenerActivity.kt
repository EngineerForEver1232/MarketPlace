//package com.pedpo.pedporent.view.search
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
//import com.google.android.material.snackbar.Snackbar
//import com.pedpo.pedporent.R
//import com.pedpo.pedporent.databinding.ListenerActivityBinding
//import com.pedpo.pedporent.model.ResponseTO
//import com.pedpo.pedporent.model.filter.FilterTransfer
//import com.pedpo.pedporent.model.liseening.RequestLiseening
//import com.pedpo.pedporent.utills.ContextApp
//import com.pedpo.pedporent.utills.CustomObserver
//import com.pedpo.pedporent.utills.GettingIP
//import com.pedpo.pedporent.utills.RecyclerItemTouchHelper
//import com.pedpo.pedporent.view.adapter.ListenerAdapter
//import com.pedpo.pedporent.view.filter.FilterActivity
//import com.pedpo.pedporent.viewModel.LiseeningViewModel
//import com.pedpo.pedporent.widget.ButtonSwitch
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class ListenerActivity :AppCompatActivity() ,
//    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
//
//    var typeMarket: String? = null;
////    lateinit var binding:ListenerActivityBinding;
//
//    @Inject
//    lateinit var adapterListener: ListenerAdapter;
//
//    private val liseeningViewModel: LiseeningViewModel by viewModels()
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ListenerActivityBinding.inflate(layoutInflater)
////        binding.listener = this;
//        setContentView(binding?.root)
//
//        liseeningViewModel?.getLiseenings()
//
//        binding.recyclerListener.apply {
//            layoutManager = LinearLayoutManager(this@ListenerActivity,LinearLayoutManager.VERTICAL,false);
//            adapter = adapterListener ;
//        }
//        val recyclerItemTouchHelper = RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT)
//        recyclerItemTouchHelper.setListener(this)
//        val ith = ItemTouchHelper(recyclerItemTouchHelper)
//        //        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
//        ith.attachToRecyclerView(binding?.recyclerListener)
//
//    }
//
//
//    /*OnClick onClick Button Category Rent */
//    fun onClickBtnCategoryRent(view: View) {
//
//        Handler(Looper.getMainLooper()).postDelayed({
////            loadDatas(ContextApp.RENT, typeCategory)
//            typeMarket = ContextApp.RENT
//         }, getString(R.integer.duration_anim).toLong())
//
//        ButtonSwitch(this@ListenerActivity).btnSwiche(
//            binding.tr,binding.tS,binding.tService,
//            binding.lineBottomRent,binding.lineBottomSale,binding.lineBottomService,
//            binding.img,binding.imgSale, binding.imgService,1
//        )
//    }
//
//    /*OnClick onClick Button Category Sale */
//    fun onClickBtnCategorySale(view: View) {
//
//        Handler(Looper.getMainLooper()).postDelayed({
////            loadDatas(ContextApp.SALE,typeCategory)
//            typeMarket = ContextApp.SALE
//
//        }, getString(R.integer.duration_anim).toLong())
//
//        ButtonSwitch(this@ListenerActivity).btnSwiche(
//            binding.tS,binding.tr,binding.tService,
//            binding.lineBottomSale,binding.lineBottomRent,binding.lineBottomService,
//            binding.imgSale,binding.img, binding.imgService,2
//        )
//    }
//
//    /*OnClick onClick Button Category Service */
//    fun onClickBtnCategoryService(view: View) {
//
//        Handler(Looper.getMainLooper()).postDelayed({
////            loadDatas(ContextApp.SERVICE,typeCategory)
//            typeMarket = ContextApp.SERVICE
//
//        }, getString(R.integer.duration_anim).toLong())
//
//        ButtonSwitch(this@ListenerActivity).btnSwiche(
//            binding.tService,binding.tr,binding.tS,
//            binding.lineBottomService,binding.lineBottomRent,binding.lineBottomSale,
//            binding.imgService,binding.img, binding.imgSale,3
//        )
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
//        // Log.e("swiped", "onSwiped: " + direction);
//
////        val itemDelete: Chat.ListChat = listChat.get(viewHolder!!.adapterPosition)
//        val positionDelete = viewHolder?.adapterPosition
////        checkDelete = true
//        if (positionDelete != null) {
//            adapterListener.notifyItemRemoved(positionDelete)
//        }
//
//
//        val snackbar: Snackbar = Snackbar
//            .make(
//                binding?.coordinator,
////                itemDelete.getOtherUserName() +
//                        getString(R.string.removed_inbox),
//                Snackbar.LENGTH_LONG
//            )
//        snackbar.setAction(resources.getString(R.string.undo)) { // undo is selected, restore the deleted item
////            checkDelete = false
////            adapterInbox.restoreItem(itemDelete, positionDelete)
//        }
//        snackbar.setActionTextColor(Color.YELLOW)
//        snackbar.addCallback(
//            object : BaseCallback<Snackbar?>() {
//                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                    super.onDismissed(transientBottomBar, event)
////                    if (checkDelete) deleteChatInbox(
////                        itemDelete.getOtherUserID(),
////                        itemDelete.getId()
////                    )
//                }
//            }
//        )
//        snackbar.show()
//
//    }
//
//    fun onClickAddLiseening1(view: View){
//        var intent = Intent(this@ListenerActivity,FilterActivity::class.java)
//        intent.putExtra(ContextApp.PAGE,ContextApp.LISEENING)
//        launchLiseening?.launch(intent)
//
//
//    }
//
//    var launchLiseening = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        result ->
//        when (result.resultCode) {
//
//            1 ->{
//
//                val filterTransfer = result.data?.getSerializableExtra("result") as FilterTransfer?;
//
//                Log.i("testFilter",  "\r\n title ${filterTransfer?.title} " +
//                        "\n cityId ${filterTransfer?.cityID}" +
//                        "\n category ${filterTransfer?.categoryID} " +
//                        "\n type ${filterTransfer?.typePrice ?: ""}         " +
//                        "\n priceFrom ${filterTransfer?.priceFrom ?: null}" +
//                        "\n priceTO ${filterTransfer?.priceTo ?: null}" +
//                        "\n agree  ${filterTransfer?.priceAgree ?: false}" +
//                        "\n free ${filterTransfer?.free ?: false}" +
//                        "\n date  ${filterTransfer?.registerDate ?: null}" +
//                        "\n advanced  ${filterTransfer?.typeAdvanced ?: null}" +
//                        "\n ip ${filterTransfer?.iP}"
//                )
//                var requestLiseen = RequestLiseening(
//                    type = typeMarket?:"",
//                    cityID = filterTransfer?.cityID?:"",
//                    categoryID = filterTransfer?.categoryID?:"",
//                    priceFrom = filterTransfer?.priceFrom?:0,
//                    priceTo = filterTransfer?.priceTo?:0,
//                    priceAgree = filterTransfer?.priceAgree?:false,
//                    free = filterTransfer?.free?:false,
//                    typeOFPrice = filterTransfer?.typeOFPrice?:""
//                )
//
//
//                liseeningViewModel?.createLiseening(liseening = requestLiseen)?.observe(this,
//                    CustomObserver(object : CustomObserver.ResultListener<ResponseTO>{
//                        override fun onSuccess(dataInput: ResponseTO) {
//
//                        }
//
//                        override fun onException(exception: Exception) {
//
//                        }
//
//                    }))
//
//            }
//
//        }
//
//    }
//
//}