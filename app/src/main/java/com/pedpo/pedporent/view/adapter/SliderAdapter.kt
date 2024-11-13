package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.SliderViewpagerBinding
import com.pedpo.pedporent.listener.IOnClickDialog
import com.pedpo.pedporent.model.SliderTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SliderAdapter : RecyclerView.Adapter<SliderAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    private var list: ArrayList<SliderTO>? = null;
//    lateinit var binding: AdapterAlbumBinding;
     var iOnClickDialog: IOnClickDialog? = null;



    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<SliderTO>();
        list?.add(SliderTO(context.getString(R.string.sliderTitle1),ContextCompat.getDrawable(context,R.drawable.img_slider1),context.getString(R.string.slider1)))
        list?.add(SliderTO(context.getString(R.string.sliderTitle2),ContextCompat.getDrawable(context,R.drawable.img_slider2),context.getString(R.string.slider2)))
        list?.add(SliderTO(context.getString(R.string.sliderTitle3),ContextCompat.getDrawable(context,R.drawable.img_slider3),context.getString(R.string.slider3)))
        list?.add(SliderTO(context.getString(R.string.sliderTitle4),ContextCompat.getDrawable(context,R.drawable.img_slider4),context.getString(R.string.slider4)))
        list?.add(SliderTO(context.getString(R.string.sliderTitle5),ContextCompat.getDrawable(context,R.drawable.img_slider5),context.getString(R.string.slider5)))

    }

//    fun updateAdapter(list:ArrayList<SliderTO>){
//        this.list = list;
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SliderViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        var view = layoutInflater?.inflate(R.layout.adapter_album, parent, false);
        return ViewHolder(binding!!);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            iOnClickDialog?.onClickDialog()
        }

        holder.initView(list?.get(position))
//        Picasso.get().load(list?.get(position)?.image!!).into(binding.imgAlbum)

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }

    class ViewHolder(private val binding: SliderViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(photoDetailTO: SliderTO?){
            binding.viewModel = photoDetailTO;
        }

    }
}