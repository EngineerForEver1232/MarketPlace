package com.pedpo.pedporent.view.paging.seeMoreLastMarket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.LoadStateFooterViewItemBinding
import com.pedpo.pedporent.utills.ContextApp
import androidx.core.view.isVisible

class LoadStateViewHolder(private val binding: LoadStateFooterViewItemBinding, retry:()->Unit)
    : RecyclerView.ViewHolder(binding.root){


        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }


    fun bind(loadState:LoadState){
        if (loadState is LoadState.Error){
            binding.errorMsg.text = ContextApp.EMOJI_ANGRY
        }

        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object{
        fun create(parent:ViewGroup , retry: () -> Unit):LoadStateViewHolder{
            val binding = LoadStateFooterViewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return LoadStateViewHolder(binding,retry)
        }
    }

}