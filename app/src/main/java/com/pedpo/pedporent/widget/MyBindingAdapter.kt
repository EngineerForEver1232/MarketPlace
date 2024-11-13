package com.pedpo.pedporent.widget

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.utills.ContextApp
import com.squareup.picasso.Picasso
import javax.annotation.Nullable

//@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
@BindingAdapter("imageUrl", "placeholder")
fun loadImage(view: ImageView,@Nullable url: String?, placeholder: Drawable) {

    if (!url.isNullOrEmpty())
        Picasso.get().load(url).error(placeholder).placeholder(placeholder).into(view)
    else
        view.setImageResource(R.drawable.placeholder)
}

@BindingAdapter("imageUrlP", "placeholderP")
fun loadImageProfile(view: ImageView,  url: String?, placeholder: Drawable) {
    if (url.isNullOrEmpty())
        view.setImageResource(R.drawable.ic_user_profile)
    else
//        view.setImageResource(R.drawable.ic_profile)
    Picasso.get().load(url).error(placeholder).placeholder(placeholder).into(view)
}

@BindingAdapter("app:visibilitySH")
fun goneUnless(view: View, visible: Boolean) {
    view.isVisible = !visible
}

@BindingAdapter("bind:visibilityWithText")
//@BindingAdapter("app:visibilitySH")
fun stringVisible(view: View, text: String?) {
    view.isVisible = !text.isNullOrEmpty()
}

//@BindingAdapter("app:visibilityTextTime1")
//fun brancheTime(view: View, enable: Boolean,emptyTextStartTime:Boolean,emptyTextEndTime:Boolean) {
//        view.isVisible = !enable;
//
//    view.isVisible =
//        !(emptyTextStartTime && emptyTextEndTime);
//}

@BindingAdapter("bind:enable", "bind:textStart1", "bind:textEnd1","bind:shift", "bind:textStart2", "bind:textEnd2")
fun brancheTime(view: View?, enable: Boolean, textStart1: String?, textEnd1: String?, shift: String?  ,textStart2: String?, textEnd2: String?) {

//    if (!enable){
//        //            view?.isVisible = false;
//    }
//        else {
            if (shift == ContextApp.SHIFT_1)
                view?.isVisible = !(textStart1.isNullOrEmpty() && textEnd1.isNullOrEmpty());
            else
                view?.isVisible = !(textStart2.isNullOrEmpty() && textEnd2.isNullOrEmpty());
//        }
}

    @BindingAdapter("bind:price")
    fun priceStr(view: View?, textPrice: Long?) {

    view?.isVisible = !(textPrice == 0L || textPrice ==null)

    Log.i("testBinding", "priceStr: ${textPrice}")

    }

@BindingAdapter("bind:enable", "bind:textStart1", "bind:textEnd1", "bind:textStart2", "bind:textEnd2")
fun brancheTimeAdd(view: View?, enable: Boolean, textStart1: String?, textEnd1: String?,textStart2: String?, textEnd2: String?) {
//        view?.isVisible = enable;

//    view?.isVisible = !(!textStart1.isNullOrEmpty() || !textEnd1.isNullOrEmpty());
//    view?.isVisible = !(!textStart2.isNullOrEmpty() || !textEnd2.isNullOrEmpty());

    if (textStart1.isNullOrEmpty() && textEnd1.isNullOrEmpty())
        view?.isVisible = true
    else if (textStart2.isNullOrEmpty() && textEnd2.isNullOrEmpty())
        view?.isVisible = true
      else
        view?.isVisible = false

    if (!enable)
        view?.isVisible = false;
}


@BindingAdapter("visibilityPrice", "priceMarket")
fun goneUnlessPrice(view: MaterialTextView, visible: Boolean, price: String?) {

    view.visibility = if (!visible) View.VISIBLE else View.GONE

    if (!price.isNullOrEmpty())
        view.text = price;
}
