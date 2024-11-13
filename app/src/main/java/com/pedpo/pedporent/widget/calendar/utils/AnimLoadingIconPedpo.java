package com.pedpo.pedporent.widget.calendar.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pedpo.pedporent.R;

public class AnimLoadingIconPedpo {

    private static AnimLoadingIconPedpo animLoadingIconPedpo = new AnimLoadingIconPedpo();
    public static AnimLoadingIconPedpo getInstance()
    {
        return animLoadingIconPedpo;
    }

    public Animation anim(Context context)
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_progress);
        return animation;
    }
}
