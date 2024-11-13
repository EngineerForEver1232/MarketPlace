package com.pedpo.pedporent.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.pedpo.pedporent.R;
import com.pedpo.pedporent.model.place.LatLngPlace;
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsTO;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchPlace extends ArrayAdapter<LatLngPlace> {

    Context context;
    //	int resource;
//	int textViewResourceId;
    List<LatLngPlace> mList, filteredPeople, mListAll;

    public AdapterSearchPlace(Context context, int resource, int textViewResourceId,
                              List<LatLngPlace> mList) {
        super(context, 0, mList);
        this.context = context;
//		this.resource = resource;
//		this.textViewResourceId = textViewResourceId;
        this.mList = mList;
        mListAll = mList;
        filteredPeople = new ArrayList<LatLngPlace>();
    }

    public void update(List<LatLngPlace> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public LatLngPlace getItem(int position) {

        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.text_list, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(mList.get(position).getCityName());
        }
        LatLngPlace people = mList.get(position);
        if (people != null) {
            TextView textView = (TextView) view.findViewById(R.id.text);
            if (textView != null) {
                textView.setText(people.getCityName());
            }

        }
        return view;
    }


    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            List<LatLngPlace> filteredList = (List<LatLngPlace>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (LatLngPlace people : filteredList) {
                    add(people);
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filteredPeople.clear();
                for (LatLngPlace people : mListAll) {
                    if (people.getCityName().contains(constraint)) {
                        filteredPeople.add(people);
                    }
                }
                filterResults.values = filteredPeople;
                filterResults.count = filteredPeople.size();
            }
            return filterResults;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((LatLngPlace) resultValue).getCityName();
        }
    };

}