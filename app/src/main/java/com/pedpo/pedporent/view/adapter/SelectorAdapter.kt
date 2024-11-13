package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterDepositBinding
import com.pedpo.pedporent.listener.ReturnContentSelector
import com.pedpo.pedporent.model.market.TypeOfguaranteeTO
import com.pedpo.pedporent.utills.ContextApp
import javax.inject.Inject

class SelectorAdapter: RecyclerView.Adapter<SelectorAdapter.ViewHolder> {

    private var list: Array<String>? = null;
    private var context:Context?=null;
    var returnContent: ReturnContentSelector?=null;

    @Inject
    constructor(context: Context){
        this.context = context;
//        this.list = arrayListOf(R.array.deposit) as ArrayList<String>
//        this.list = context.resources.getStringArray(R.array.deposit)

    }


    fun updateAdapter(list: Array<String>) {
        this.list = list ?: arrayOf();

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = AdapterDepositBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(list?.get(position),position = position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0;
    }

    var typeOfguaranteeTO = TypeOfguaranteeTO();

    inner class ViewHolder(val binding: AdapterDepositBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(str:String?,position:Int?) {
            binding.text.text = str ;



            binding.text.setOnCheckedChangeListener(){ view,isChecked->
                check(position, isChecked,binding)
                returnContent?.returnContentSelector("",str,position = position)
            }
        }

    }

    fun check(position: Int?,isChecked:Boolean,binding: AdapterDepositBinding){
        when (position) {
            ContextApp.NATIONAL_ID_CARD -> {
                typeOfguaranteeTO.nationalCard = isChecked;
//                binding.text.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            }
            ContextApp.CERTIFICATE ->
                typeOfguaranteeTO.identityCard = isChecked;

            ContextApp.PROMISSORY_NOTE ->
                typeOfguaranteeTO.promissoryNote = isChecked;

            ContextApp.CHECK ->
                typeOfguaranteeTO.check = isChecked;

            ContextApp.OTHER -> {
                typeOfguaranteeTO.other = isChecked;
            }
        }

    }

}