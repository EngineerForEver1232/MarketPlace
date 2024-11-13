package com.pedpo.pedporent.widget.calendar.adapter.miladi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedpo.pedporent.R;

import java.util.ArrayList;

public class AdapterGridTitle extends ArrayAdapter  {

    private ArrayList arrayListTitle ;
    private LayoutInflater layoutInflater ;

    public AdapterGridTitle(Context context, int resource , ArrayList arrayListtitle) {
        super(context, resource);
        this.arrayListTitle = arrayListtitle ;
        this.layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        convertView = this.layoutInflater.inflate(R.layout.item_title_calendar , parent ,false);
        TextView textView = convertView.findViewById(R.id.textview_item);
        textView.setText(arrayListTitle.get(position)+"");

        return convertView;
    }

    @Override
    public int getCount() {
        return arrayListTitle.size();
    }
}
