package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AdapterIconsBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.databinding.AdapterIconsCategoryStoreBinding
import com.pedpo.pedporent.listener.ClickIconCategoryStore
import com.pedpo.pedporent.model.store.category.CategoryStoreTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.collections.ArrayList

@ActivityScoped
class  IconCategoryStore : RecyclerView.Adapter<IconCategoryStore.ViewHolder> {

    private var context: Context? = null;
    private var layoutInflater: LayoutInflater? = null;
    private var list: List<CategoryStoreTO>? = null;
    var clickAdapterCategory: ClickIconCategoryStore?=null;
    @Inject
    lateinit var prefLang: PrefManagerLanguage;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = ArrayList<CategoryStoreTO>();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterIconsCategoryStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding);
    }

    private var dark:Boolean =false;
    fun updateAdapter() {
        dark = true;
        notifyDataSetChanged()
    }
    fun updateAdapter(list: List<CategoryStoreTO>) {
        this.list = list ;
        positionAdapter = 0;
        notifyDataSetChanged()

    }

    private var listView = ArrayList<MaterialCardView>();
    private var listViewIc = ArrayList<ImageView>();
    private var listViewText = ArrayList<MaterialTextView>();

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        listView.add(holder.binding.cardView)
        listViewIc.add(holder.binding.icIcon)
        listViewText.add(holder.binding.tIcon)
//        binding.tIcon?.text = list?.get(position)?.nameIcon;
//        binding.icIcon?.setImageResource(list?.get(position)?.icon!!);

//        holder.tIcon?.text = list?.get(position)?.nameIcon;
//        holder.icon?.setImageResource(list?.get(position)?.icon!!);

        holder.bind(list!![position],position = position)

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    var positionAdapter = 0;
    inner class ViewHolder(val binding: AdapterIconsCategoryStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryStoreTO, position: Int?) {


            binding.viewModel = category;
            binding.icIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.gray_standard))
            binding.cardView.strokeColor = ContextCompat.getColor(context!!, R.color.gray_standard)
            binding.tIcon.setTextColor(ContextCompat.getColor(context!!, R.color.gray_standard))

            binding.tIcon.text = if (prefLang.language == ContextApp.EN)
                list?.get(position?:0)?.categoryStoreName ?: ""
            else
                list?.get(position?:0)?.categoryStoreName ?: ""

            binding.root.setOnClickListener {
                positionAdapter = position!!;
                for (cardView in listView) {
                    cardView.strokeColor = ContextCompat.getColor(context!!, R.color.gray_standard)
                    cardView.cardElevation = 0f
                }
                for (image in listViewIc) {
                    image.setColorFilter(ContextCompat.getColor(context!!, R.color.gray_standard))
                }
                for (textView in listViewText) {
                    textView.setTextColor(ContextCompat.getColor(context!!, R.color.gray_standard))
                }
                binding.icIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorPrimary))
                binding.cardView.strokeColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
                binding.tIcon.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

//                binding.cardView.cardElevation = 10f

                if (clickAdapterCategory!=null)
                    clickAdapterCategory?.OnItemClickAdapter(categoryTO = category)
            }

            if (positionAdapter==position && position != 0){
                binding.icIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorPrimary))
                binding.cardView.strokeColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
                binding.tIcon.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }

            if (positionAdapter==0 && position == 0){
                binding.icIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorPrimary))
                binding.cardView.strokeColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
                binding.tIcon.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            }

        }


//        fun bind(yourDataType: Category?, onSelect: (Category?) -> Unit) {
//            // bind your view here
//            binding.root.setOnClickListener {
//                onSelect(yourDataType)
//            }
//        }
    }

}