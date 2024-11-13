package com.pedpo.pedporent.widget

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CustomItemDecoration: ItemDecoration {
    private var padddingStartEnd: Int?=null;
    private var padddingDefault: Int?=null;

    constructor(context: Context){
        val metrics = context.resources.displayMetrics
        padddingStartEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, metrics).toInt()
        padddingDefault = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, metrics).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        if (parent.layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            if (itemPosition == 0) {
                outRect.left = padddingStartEnd!!
            } else {
                outRect.left = padddingDefault!!
            }
            val adapter = parent.adapter
            if (adapter != null && itemPosition == adapter.itemCount - 1) {
                outRect.right = padddingStartEnd!!
            }
        } else {
            if (itemPosition == 0) {
                outRect.right = padddingStartEnd!!
            } else {
                outRect.right = padddingDefault!!
            }
            val adapter = parent.adapter
            if (adapter != null && itemPosition == adapter.itemCount - 1) {
                outRect.left = padddingStartEnd!!
            }
        }
    }

}