package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterCommentBinding
import com.pedpo.pedporent.model.comment.CommentData
import com.pedpo.pedporent.model.comment.CommentTO
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CommentAdapter : RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    private var list:ArrayList<CommentTO>?=null;

    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        this.list = ArrayList<CommentTO>();
    }

    fun adapter(list: ArrayList<CommentTO>?){
        this.list= list ?: ArrayList();
        notifyDataSetChanged()
    }
    fun insertAdapter(list: ArrayList<CommentTO>){

        this.list!!.add(list.lastIndex!!, list[list.lastIndex]!!)
        notifyItemInserted(list.lastIndex!!)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(list?.get(position)!!)

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    class ViewHolder(val binding: AdapterCommentBinding) : RecyclerView.ViewHolder(binding?.root){

        fun bindView(commentTO: CommentTO){
//            binding.tName.text = commentTO.description
            binding.viewModel = commentTO;
            Picasso.get().load(commentTO.image).placeholder(R.drawable.ic_profile_gray).into(binding.imgCircle)
        }

    }


}