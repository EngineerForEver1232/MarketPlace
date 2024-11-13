package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterPhotoUnderBinding
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.listener.ClickAdapterAlbum
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PhotoUnderAdapter : RecyclerView.Adapter<PhotoUnderAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    private var context: Context? = null;
    private var list = ArrayList<PhotoTO>();
    var clickAdapterAlbum: ClickAdapterAlbum? = null;
    lateinit var binding:AdapterPhotoUnderBinding


    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
//        list?.add(PhotoTO(R.drawable.ic_baseline_camera_alt_24, null))
//        list?.add(PhotoTO(R.drawable.ic_gallery_market, null))
//        list?.add(PhotoTO(R.drawable.ic_gallery_market, null))
//        list?.add(PhotoTO(R.drawable.ic_gallery_market, null))
//        list?.add(PhotoTO(R.drawable.ic_gallery_market, null))
//        list?.add(PhotoTO(R.drawable.ic_gallery_market, null))
    }

    fun updateAdapter(list: ArrayList<PhotoTO>) {
        this.list = list;
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AdapterPhotoUnderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            binding.icCamera.setImageResource(list.get(position).photoResource!!)
            binding.icCamera.setColorFilter(ContextCompat.getColor(context!!,R.color.gray_standard_bold))
            binding.tImg.text = context?.getString(R.string.ad_image);
        } else {
            binding.icCamera.setImageResource(R.drawable.ic_gallery_market)
            binding.icCamera.setColorFilter(ContextCompat.getColor(context!!,R.color.gray_standard))
            binding.tImg.text = context?.getString(R.string.image);
        }

//        holder.itemView.setOnClickListener {
//            clickAdapter?.OnItemClickListenerAdapter(TransferPhoto(position, "test"))
//        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class ViewHolder(binding: AdapterPhotoUnderBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}