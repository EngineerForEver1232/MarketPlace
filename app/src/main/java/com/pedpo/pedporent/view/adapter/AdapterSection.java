package com.pedpo.pedporent.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pedpo.pedporent.R;
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsTO;

import java.util.ArrayList;
import java.util.List;

public class AdapterSection extends ArrayAdapter<TicketSectionsTO> {

    Context context;
    //	int resource;
//	int textViewResourceId;
    List<TicketSectionsTO> mList, filteredPeople, mListAll;

    public AdapterSection(Context context, int resource, int textViewResourceId,
                          List<TicketSectionsTO> mList) {
        super(context, 0, mList);
        this.context = context;
//		this.resource = resource;
//		this.textViewResourceId = textViewResourceId;
        this.mList = mList;
        mListAll = mList;
        filteredPeople = new ArrayList<TicketSectionsTO>();
    }

    public void update(List<TicketSectionsTO> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public TicketSectionsTO getItem(int position) {

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
            textView.setText(mList.get(position).getTicketSectionName());
        }
        TicketSectionsTO people = mList.get(position);
        if (people != null) {
            TextView textView = (TextView) view.findViewById(R.id.text);
            if (textView != null) {
                textView.setText(people.getTicketSectionName());
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
            List<TicketSectionsTO> filteredList = (List<TicketSectionsTO>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (TicketSectionsTO people : filteredList) {
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
                for (TicketSectionsTO people : mListAll) {
                    if (people.getTicketSectionName().contains(constraint)) {
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
            return ((TicketSectionsTO) resultValue).getTicketSectionName();
        }
    };

}