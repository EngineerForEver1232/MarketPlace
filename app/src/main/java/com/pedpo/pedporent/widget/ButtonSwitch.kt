package com.pedpo.pedporent.widget

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R

class ButtonSwitch {

    var context:Context?=null
    constructor(context: Context){
        this.context = context
    }

        fun btnSwiche(t1: MaterialTextView, t2: MaterialTextView, t3: MaterialTextView,
                      v1: View, v2: View, v3: View,
                      img1: ImageView, img2: ImageView, img3: ImageView,
                      number:Int
        ){
            if (t1.tag != null && t1.tag.equals("true"))
                return;
            if (number==1) {
                if (t2.tag == null || t2.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_left))
                    v2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_left))
                } else if (t3.tag != null && t3.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_left))
                    v3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_left))
                }
            }else if (number==2){
                if (t2.tag == null || t2.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_right))
                    v2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_right))
                } else if (t3.tag != null && t3.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_left))
                    v3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_left))
                }
            }else if (number==3){
                if (t2.tag == null || t2.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_right))
                    v2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_right))
                } else if (t3.tag != null && t3.tag.equals("true")) {
                    v1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_refresh_right))
                    v3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.trans_line_right))
                }
            }
            selectBtnCategory(t1, img1, v1)

            unSelectBtnCategory(t2, img2, v2)
            unSelectBtnCategory(t3, img3, v3)

        }
        fun selectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view:View?){
            textview?.tag = "true"
            textview?.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            imageView?.setColorFilter(ContextCompat.getColor(context!!,R.color.colorPrimary))
            view?.isVisible = true
        }
        fun unSelectBtnCategory(textview: MaterialTextView?, imageView: ImageView?, view:View?){
            textview?.tag = "false"
            textview?.setTextColor(ContextCompat.getColor(context!!,R.color.text_category_type))
            imageView?.setColorFilter(ContextCompat.getColor(context!!,R.color.text_category_type))
            view?.isVisible = false
        }


}