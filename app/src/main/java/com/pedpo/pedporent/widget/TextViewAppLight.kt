package com.pedpo.pedporent.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.pedpo.pedporent.R
import com.google.android.material.textview.MaterialTextView

class TextViewAppLight : MaterialTextView {

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
//        textSize = 12f
        typeface =ResourcesCompat.getFont(context, R.font.iran_sans_light)
//        typeface = Typeface.createFromAsset(context.assets,"fonts/iran_sans.ttf")
    }

}