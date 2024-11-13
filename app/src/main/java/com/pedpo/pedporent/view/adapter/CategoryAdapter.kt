package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterCategoryBinding
import com.pedpo.pedporent.listener.ClickAdapterCategory
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.squareup.picasso.Picasso
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private var layoutInflater: LayoutInflater? = null;
    var clickAdapterCategory: ClickAdapterCategory? = null;
    private var list: List<CategoryTO>? = null;
    private var type: String? = null;

    @Inject
    lateinit var prefLang: PrefManagerLanguage;
//    lateinit var binding:AdapterCategoryBinding

    @Inject
    constructor(@ActivityContext context: Context) {
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList<CategoryTO>();

    }

    fun updateAdapter(list: List<CategoryTO>, type: String?) {
        this.list = list ?: ArrayList();
        this.type = type;
        notifyDataSetChanged();
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        var view = layoutInflater?.inflate(R.layout.adapter_category,parent,false)
        return ViewHolder(binding!!);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Log.i("categoryTypeAdapter", "adapter: ${list?.get(position)?.isHome}")

        if (!list?.get(position)?.appIconeAddress.isNullOrEmpty()) {
            Picasso.get().load(list?.get(position)?.appIconeAddress!!)
                .into(holder.binding.icCategory)
            holder.binding.icCategory.visibility = View.VISIBLE;
            holder.binding.icArrow.visibility = View.VISIBLE;
        } else {
            holder.binding.icCategory.setImageResource(R.drawable.ic_dot) ;
//            holder.binding.icCategory.visibility = View.INVISIBLE;
            holder.binding.icArrow.visibility = View.GONE;
        }

        holder.binding.nameCategory.text = if (prefLang.language == ContextApp.EN)
            list?.get(position)?.englishCategoryMarketName ?: ""
        else
            list?.get(position)?.categoryMarketName ?: ""

        holder.itemView.setOnClickListener {
            if (clickAdapterCategory != null)
                clickAdapterCategory?.OnItemClickListenerAdapter(list?.get(position)!!, type ?: "")
        }

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    class ViewHolder(val binding: AdapterCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}