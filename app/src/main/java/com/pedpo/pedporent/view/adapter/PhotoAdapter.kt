package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterPhotoBinding
import com.pedpo.pedporent.model.PhotoTO
import com.pedpo.pedporent.listener.ClickAdapterAlbum
import com.pedpo.pedporent.listener.IClickDialog
import com.pedpo.pedporent.view.dialog.FragmentDialogSettingPhoto
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>, IClickDialog {

    private var layoutInflater: LayoutInflater? = null;
    private var context: Context? = null;
    private var list = ArrayList<PhotoTO>();
    var clickAdapterAlbum: ClickAdapterAlbum? = null;
    private var viewHolder: ViewHolder? = null;
    lateinit var binding: AdapterPhotoBinding;


    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    fun updateAdapter(list: ArrayList<PhotoTO>) {
        this.list = list;
        for (p in list){
            Log.i("testEdit", "updateAdapter1: ${p.photoUri}")
        }
        notifyDataSetChanged();
    }

    fun delete(photoTO: PhotoTO, position: Int) {
        list?.remove(photoTO)
        notifyItemRangeRemoved(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = AdapterPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        var view = layoutInflater?.inflate(R.layout.adapter_photo, parent, false);
        return ViewHolder(binding!!)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 1)
            this.viewHolder = holder;

        if (position==0){
            holder.img?.visibility = View.GONE;
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.constraintCamera?.setBackgroundColor(Color.TRANSPARENT)
        }else{
            if (list.get(position).bitmap != null) {
                holder.img?.visibility = View.VISIBLE;
                if (list.get(position).bitmap != null)
                    holder.img?.setImageBitmap(list.get(position).bitmap)
            } else if (list.get(position).photoUri !=null){
                Picasso.get().load(list.get(position).photoUri).into(holder.img)
//            holder.img?.setImageURI(list.get(position).photoUri)
            }else{
                holder.img?.setImageResource(list.get(position).photoResource!!)
            }
        }

//        if (list.get(position).photoResource != 0) {
//            holder.img?.visibility = View.GONE;
//            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
//            holder.constraintCamera?.setBackgroundColor(Color.TRANSPARENT)
//        } else if (list.get(position).bitmap != null) {
//            holder.img?.visibility = View.VISIBLE;
//            if (list.get(position).bitmap != null)
//                holder.img?.setImageBitmap(list.get(position).bitmap)
//        } else {
//            Picasso.get().load(list.get(position).photoUri).into(holder.img)
////            holder.img?.setImageURI(list.get(position).photoUri)
//        }

        if (position == 1) {
            holder.frameT?.visibility = View.VISIBLE;
        } else
            holder.frameT?.visibility = View.GONE;

        Log.i("chooseImage", "counter : " + list?.get(position).counter)

        holder.itemView.setOnClickListener {
            clickItemView(list?.get(position), position)
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    class ViewHolder(binding: AdapterPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        //        @BindView(R.id.img)
//        lateinit var img: ImageView;
//        @BindView(R.id.constraintCamera)
//        lateinit var constraintCamera: ConstraintLayout;
//        @BindView(R.id.frameT)
//        lateinit var frameT: FrameLayout;
        var img: ImageView? = null;
        var constraintCamera: ConstraintLayout? = null;
        var frameT: FrameLayout? = null;

        init {
//            ButterKnife.bind(this, binding.root)
            img = binding.img;
            constraintCamera = binding.constraintCamera
            frameT = binding.frameT;
        }
    }

    private fun clickItemView(photoTO: PhotoTO, position: Int) {
        if (position == 0) {
            clickAdapterAlbum?.onItemClickChoosePhoto(photoTO, position = position)
        } else {
            var dialogSettingPhoto = FragmentDialogSettingPhoto().newInstance(position = position)
            var fragmentActivity: FragmentActivity = context as FragmentActivity;
            dialogSettingPhoto?.iClickDialog = this;
            dialogSettingPhoto.show(
                fragmentActivity.supportFragmentManager,
                "dialogSelectPhoto"
            )
        }
    }

    override fun OnClickDialog_OrginalPhoto(position: Int) {
        clickAdapterAlbum?.setOrginalPhotoInAlbums_Adapter(
            list.get(position),
            position = position,
            binding!!
        )
    }

    override fun OnClickDialog_DeletePhoto(position: Int) {
        clickAdapterAlbum?.onDeletePhotoFromAlbums_Adapter(
            list.get(position), position = position,
            binding!!
        )
    }

}