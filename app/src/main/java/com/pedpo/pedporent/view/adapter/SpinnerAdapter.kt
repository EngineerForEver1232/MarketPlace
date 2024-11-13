package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pedpo.pedporent.R

class SpinnerAdapter : BaseAdapter {

    private var list:List<String>?=null;
    private var context:Context?=null;
    private var layoutInflater: LayoutInflater? = null


//    @Inject
    constructor(context: Context,list: List<String>){
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context)

    }

    override fun getCount(): Int {
        return list?.size ?:0
    }

    override fun getItem(position: Int): Any {
        return list?.get(position)?: "";
    }

    override fun getItemId(position: Int): Long {
        return  list?.size?.toLong() ?:0L;
    }

    inner class Handler {
        var textCountryCode: TextView? = null
    }

    override fun getView(position: Int, v: View?, viewGroup: ViewGroup?): View {

        var convertView: View? = null;

        var handler: Handler? = null
        if (convertView == null) {
            handler = Handler();
            convertView = layoutInflater?.inflate(R.layout.adapter_spinner_menu, null)!!;
            handler.textCountryCode = convertView?.findViewById<TextView>(R.id.textCountryCode)
            convertView?.tag = handler
        } else {
            handler = convertView.tag as Handler?
        }
        handler?.textCountryCode!!.text = list!![position]

        return convertView!!

    }


}