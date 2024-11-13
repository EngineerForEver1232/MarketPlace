package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.databinding.AdapterCityBinding
import com.pedpo.pedporent.listener.ClickAdapterPlace
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.room.entity.place.CountryEntity
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.typeOf


class PlaceAdapter @Inject constructor(
    private var context: Context,
    private var prefManagerLanguage: PrefManagerLanguage
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    private var layoutInflater: LayoutInflater? = null;
    private var list: List<PlaceTO>;
    var clickAdapterPlace: ClickAdapterPlace? = null;
    private var typePlace: String? = null;


    init {
        layoutInflater = LayoutInflater.from(context);
        list = ArrayList();
    }

    //    fun <T : Any> updateAdapter(list: List<T>,typePlace:String?,objectType: Class<out T>){
////        this.list =  list as List<T>
//        Log.i("testPlaceLocal", "updateAdapter: ${objectType.simpleName} ")
//        this.list.clear()
//        if (objectType.simpleName == CountryEntity::class.java.simpleName){
//            for (country in list as List<CountryEntity>){
//                var placeTO = PlaceTO();
//                placeTO.id =   country.id
//                placeTO.name =   country.name
//                this.list.add(placeTO)
//            }
//        }
//
//        this.typePlace = typePlace;
//        notifyDataSetChanged()
//    }

    fun updateAdapter(list: List<PlaceTO>, typePlace: String?) {
        this.list = list;

        this.typePlace = typePlace;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCityBinding.inflate(layoutInflater!!, parent, false)
        return ViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (prefManagerLanguage.language == ContextApp.EN)
//            holder.binding.text.text = list?.get(position)?.englishName
//        else
            holder.binding.text.text = list?.get(position)?.name

        holder.binding.root.setOnClickListener {
            if (clickAdapterPlace != null)
                clickAdapterPlace?.OnItemClickListenerAdapter(list?.get(position)!!, typePlace)
        }

    }

    override fun getItemCount(): Int {
        return list?.size!!;
    }


    class ViewHolder(val binding: AdapterCityBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
        }
    }

}