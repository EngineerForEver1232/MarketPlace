package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterShowImageBinding
import com.pedpo.pedporent.model.details.PhotoDetailTO
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ShowImageAdapter : RecyclerView.Adapter<ShowImageAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    private var list: ArrayList<PhotoDetailTO>? = null;
    lateinit var binding: AdapterShowImageBinding;


    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<PhotoDetailTO>();
//        list?.add(R.drawable.motor1)
//        list?.add(R.drawable.motor2)
//        list?.add(R.drawable.motor3)
//        list?.add(R.drawable.motor5)
//        list?.add(R.drawable.motor6)
    }

    fun updateAdapter(list:ArrayList<PhotoDetailTO>){
        this.list = list;
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AdapterShowImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        var view = layoutInflater?.inflate(R.layout.adapter_album, parent, false);
        return ViewHolder(binding!!);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        binding.imgAlbum.setImageResource(list?.get(position)!!)
        Picasso.get().load(list?.get(position)?.image!!).into(binding.imgAlbum)
    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }

    class ViewHolder(binding: AdapterShowImageBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}