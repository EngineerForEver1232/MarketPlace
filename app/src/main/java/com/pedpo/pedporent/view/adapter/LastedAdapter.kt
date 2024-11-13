package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPaginBinding
import com.pedpo.pedporent.listener.ClickAdapterPaging
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.UtillsApp
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.paging.seeMoreVisitedMarket.model.PaginTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ActivityScoped
 class LastedAdapter : RecyclerView.Adapter<LastedAdapter.ViewHolder> {

    private var context: Context? = null;
    private var layoutInflater: LayoutInflater? = null;
    private var list: List<PaginTO>? = null;
    var clickAdapterPaging: ClickAdapterPaging?=null;
    @Inject
    lateinit var utillsApp:UtillsApp
    @Inject
    lateinit var prefApp: PrefApp

    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<PaginTO>();
    }

    fun updateAdapter(list: List<PaginTO>?) {
        this.list = list ?: ArrayList();
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPaginBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        var view = layoutInflater?.inflate(R.layout.adapter_lasted, parent, false);
        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.i("adapterLaster",list?.get(position)?.rentPriceDay!!.toString())
//        binding.tPrice?.text = list?.get(position)?.rentPriceDay!!.toString();
//        binding.tTitle?.text = list?.get(position)?.title!!.toString();
//        Picasso.get().load(list?.get(position)?.photoAddress!!).placeholder(R.drawable.placeholder).into(binding.image)
//        binding.image?.setImageResource(list?.get(position)?.photoAddress!!)
        holder.initView(paginTO = list?.get(position))

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }

    inner class ViewHolder(private val binding: AdapterPaginBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(paginTO: PaginTO?){
            if (paginTO!=null)
            binding.viewModel=paginTO

            binding.root.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.DETAILS,binding)
            }
            binding.icBookmark.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO, ContextApp.BOOKMARK,binding)
            }
            binding.icLike.setOnClickListener {
                clickAdapterPaging?.onClickAdapterPaging(paginTO = paginTO, ContextApp.LIKE,binding)
            }


            if (paginTO?.free==true){
                binding.tPrice.text = context?.getString(R.string.free)
            }else if (paginTO?.priceAgree==true){
                binding.tPrice.text = context?.getString(R.string.an_agreement)
            }else {
                binding.tPrice.text = paginTO?.rentPriceDay.toString()
            }
            Log.i("testMyItem", "initView: ${paginTO?.isLikeByIp}" +
                    " ${paginTO?.isBookMarkByUser}")


            if (prefApp.getToken().isEmpty())
                binding.icBookmark.visibility = View.INVISIBLE;
            else
                binding.icBookmark.visibility = View.VISIBLE;

            if (paginTO?.isActive == null || paginTO.isActive == true){
                binding.viewNoActive.isVisible = false
                binding.layoutIsActive.isVisible = false
            } else{
                binding.viewNoActive.isVisible = true
                binding.layoutIsActive.isVisible = true
            }

            Log.i("adapterLasted", "isActive :  ${paginTO?.isActive}")
            Log.i("adapterLasted", "startTime :  ${paginTO?.startTimeInactive}")

            if (paginTO?.startTimeInactive!=null && paginTO.endTimeInactive!=null){
                val startDate = utillsApp.startDate(paginTO.startTimeInactive?:"")
                val endDate = utillsApp.endDate( paginTO.endTimeInactive?:"")

                binding.tStartDate.text = startDate.get(Calendar.DAY_OF_MONTH).toString()
                binding.tEndDate.text = endDate.get(Calendar.DAY_OF_MONTH).toString()

//                binding.startMonth.text = startDate.getDisplayName(startDate.get(Calendar.MONTH)+1,0, Locale.CANADA);
                binding.startMonth.text = startDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)
                binding.endMonth.text = endDate.getDisplayName(Calendar.MONTH , Calendar.SHORT, Locale.CANADA)

            }

        }

    }

}