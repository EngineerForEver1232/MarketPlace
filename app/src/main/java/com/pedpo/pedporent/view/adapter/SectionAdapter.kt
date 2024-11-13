package com.pedpo.pedporent.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.pedpo.pedporent.databinding.AdapterSectionsBinding
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsTO
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SectionAdapter : BaseAdapter {

    private var layoutInflater: LayoutInflater? = null;
    private var list: List<TicketSectionsTO>? = null;

    @Inject
    constructor(@ActivityContext context: Context) {
        this.layoutInflater = LayoutInflater.from(context);
        list = ArrayList<TicketSectionsTO>();
    }

    fun updateAdapter(list: List<TicketSectionsTO>?) {
        this.list = list?:ArrayList();
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list?.size ?: 0;
    }

    override fun getItem(position: Int): Any {
        return list?.get(position) ?: TicketSectionsTO()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() ?: 0;
    }

    inner class ViewHolder {
        var binding: AdapterSectionsBinding? = null;
        var view: View? = null;

        constructor(binding: AdapterSectionsBinding) {
            this.binding = binding;
            this.view = binding.root;
        }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        var holder: ViewHolder

        if (view == null) {
            var binding = AdapterSectionsBinding.inflate(layoutInflater!!, parent, false);
            holder = ViewHolder(binding = binding)
            holder.view = binding.root;
            holder?.view?.tag = holder
        }else{
         holder = view.getTag() as ViewHolder
        }

        holder.binding?.viewModel = list?.get(position)

        return holder?.view!!
    }


}