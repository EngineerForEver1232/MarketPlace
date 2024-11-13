package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pedpo.pedporent.R
import de.hdodenhof.circleimageview.CircleImageView

class PhoneAdapter : BaseAdapter {

    private var context: Context? = null
    private var countryCode: Array<String>? = null
    private var layoutInflater: LayoutInflater? = null
    private var countryFlag: IntArray? = null

    constructor(context: Context, countryFlag: IntArray, countryCode: Array<String>) {
        this.context = context;
        this.countryCode = countryCode;
        this.countryFlag = countryFlag;
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return countryCode?.size!!
    }

    override fun getItem(position: Int): Any {
        return countryCode!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class Handler {
        var countryFlag: CircleImageView? = null
        var textCountryCode: TextView? = null
    }

    override fun getView(position: Int, v: View?, viewGroup: ViewGroup?): View {

        var convertView: View? = null;

        var handler: Handler? = null
        if (convertView == null) {
            handler = Handler();
            convertView = layoutInflater?.inflate(R.layout.adapter_phone, null);
            handler.countryFlag = convertView?.findViewById<CircleImageView>(R.id.countryFlag)
            handler.textCountryCode = convertView?.findViewById<TextView>(R.id.textCountryCode)
            convertView?.tag = handler
        } else {
            handler = convertView.tag as Handler
        }
        handler.countryFlag?.setImageResource(countryFlag?.get(position) ?: R.drawable.flag_usa)
        handler.textCountryCode?.text = countryCode?.get(position) ?: "+1"

        return convertView!!

    }
}