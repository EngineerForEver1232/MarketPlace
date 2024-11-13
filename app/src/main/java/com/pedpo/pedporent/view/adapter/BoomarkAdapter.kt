package com.pedpo.pedporent.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterBookmarkBinding
import com.pedpo.pedporent.listener.ClickAdapterBookmark
import com.pedpo.pedporent.model.bookmark.BookmarkTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyMutable
import com.pedpo.pedporent.utills.NumberFormatDigits
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import javax.inject.Inject

class BoomarkAdapter : RecyclerView.Adapter<BoomarkAdapter.ViewHolder> {

    private var list: ArrayList<BookmarkTO>? = null;
    var clickAdapterBookmark: ClickAdapterBookmark? = null;
    var numberFormat: NumberFormatDigits? = null;

    @Inject
    constructor()

    fun updateAdapter(list: List<BookmarkTO>?) {
        this.list = list as ArrayList<BookmarkTO>?;
        notifyDataSetChanged()
    }
    fun removeItem(position: Int?) {
        this.list?.removeAt(position?:0)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var prefManagerLanguage = PrefManagerLanguage(parent.context);
        numberFormat = NumberFormatDigits(prefManagerLanguage);
        val binding =
            AdapterBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.initView(list?.get(position),position)

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(private val binding: AdapterBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun initView(bookmarkTO: BookmarkTO?,position: Int?) {
            binding.viewModel = bookmarkTO;

            if (bookmarkTO?.free == true) {
                binding.tPrice.text = binding?.root.context.getString(R.string.free);
            } else if (bookmarkTO?.priceAgree == true) {
                binding.tPrice.text = binding?.root.context.getString(R.string.an_agreement)
            } else {
//                if (bookmarkTO?.rentPriceDay != null) {
//                    binding.tPrice.text = numberFormat?.convertToDigist(bookmarkTO.rentPriceDay ?: 0L)
//                }

                if (bookmarkTO?.type == ContextApp.RENT)
                    binding.tPrice.text =  "$"+ numberFormat?.convertToDigist(bookmarkTO.rentPriceDay ?: 0)+" "+bookmarkTO.showType;
                else
                    binding.tPrice.text = "$"+ numberFormat?.convertToDigist(bookmarkTO?.rentPriceDay ?: 0)

            }

            Log.i("testBookmark", "initView: ${bookmarkTO?.rentPriceDay}")

            binding.icBookmark.setOnClickListener {

                MyMutable.mutableMarketID.value = bookmarkTO?.marketID


                clickAdapterBookmark?.onClickAdapterPaging(
                    bookmark = bookmarkTO, ContextApp.BOOKMARK, binding = binding,position)
            }
            binding.root.setOnClickListener {
                clickAdapterBookmark?.onClickAdapterPaging(bookmarkTO, ContextApp.DETAILS,binding, position = position)
            }

        }

    }

}