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
import com.pedpo.pedporent.listener.ClickIconCategory
import com.pedpo.pedporent.model.market.category.CategoryTO
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.collections.ArrayList

@ActivityScoped
class  IconsAdapter : RecyclerView.Adapter<IconsAdapter.ViewHolder> {

    private var context: Context? = null;
    private var layoutInflater: LayoutInflater? = null;
    private var list: List<CategoryTO>? = null;
    var clickAdapterCategory: ClickIconCategory?=null;
    @Inject
    lateinit var prefLang: PrefManagerLanguage;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list = ArrayList<CategoryTO>();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterIconsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!);
    }

    private var dark:Boolean =false;
    fun updateAdapter() {
        dark = true;
        notifyDataSetChanged()
    }
    fun updateAdapter(list: List<CategoryTO>) {
        this.list = list
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
    inner class ViewHolder(val binding: AdapterIconsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryTO, position: Int?) {

            Log.i("IconsAdapter", "bind: 0")


            binding.viewModel = category;
            binding.icIcon.setColorFilter(ContextCompat.getColor(context!!, R.color.gray_standard))
            binding.cardView.strokeColor = ContextCompat.getColor(context!!, R.color.gray_standard)
            binding.tIcon.setTextColor(ContextCompat.getColor(context!!, R.color.gray_standard))

            binding.tIcon.text = if (prefLang.language == ContextApp.EN)
                list?.get(position?:0)?.englishCategoryMarketName ?: ""
            else
                list?.get(position?:0)?.categoryMarketName ?: ""

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
                    clickAdapterCategory?.OnItemClickListenerAdapter(categoryTO = category)
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