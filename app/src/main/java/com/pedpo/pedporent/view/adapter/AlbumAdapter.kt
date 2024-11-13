package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterAlbumBinding
import com.pedpo.pedporent.listener.IOnClickDialog
import com.pedpo.pedporent.model.details.PhotoDetailTO
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    private var list: List<PhotoDetailTO>? = null;
//    lateinit var binding: AdapterAlbumBinding;
     var iOnClickDialog: IOnClickDialog? = null;

    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<PhotoDetailTO>();
    }

    fun updateAdapter(list:List<PhotoDetailTO>){
        this.list = list;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class ViewHolder(private val binding: AdapterAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun initView(photoDetailTO: PhotoDetailTO?){
            binding.viewModel = photoDetailTO;
        }

    }
}