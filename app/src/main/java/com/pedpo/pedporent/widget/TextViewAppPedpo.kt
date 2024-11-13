package com.pedpo.pedporent.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.pedpo.pedporent.R
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.language.PrefManagerLanguage

class TextViewAppPedpo : MaterialTextView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
//        textSize = 12f
        var pref = PrefManagerLanguage(context);

        if (pref.language == ContextApp.EN)
            typeface = ResourcesCompat.getFont(context, R.font.poppins_regular);
        else
            typeface = ResourcesCompat.getFont(context, R.font.iran_sans_light);

        setTextColor(ContextCompat.getColor(context, R.color.add_image))
//        typeface = Typeface.createFromAsset(context.assets,"fonts/iran_sans.ttf")
    }

}