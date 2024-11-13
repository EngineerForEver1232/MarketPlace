package com.pedpo.pedporent.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.pedpo.pedporent.R
import com.google.android.material.textview.MaterialTextView

class TextViewBoldOpen : MaterialTextView {


    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context,attributeSet: AttributeSet) : super(context,attributeSet){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        init()
    }

    private fun init() {
//        typeface = Typeface.DEFAULT_BOLD
        typeface = ResourcesCompat.getFont(context, R.font.iran_sans_bold)

    }

}