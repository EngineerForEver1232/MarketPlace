package com.pedpo.pedporent.utills;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecAnimator extends RecyclerView.ItemAnimator {

private final static String TAG = "RecAnimator";

private final static int ANIMATION_TYPE_DISAPPEAR = 1;
private final static int ANIMATION_TYPE_MOVE = 2;

// must keep track of all pending/ongoing animations.
private final ArrayList<AnimInfo> pending = new ArrayList<>();
private final HashMap<RecyclerView.ViewHolder, AnimInfo> disappearances = new HashMap<>();
private final HashMap<RecyclerView.ViewHolder, AnimInfo> persistences = new HashMap<>();

@Override
public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
    pending.add(new AnimInfo(viewHolder, ANIMATION_TYPE_DISAPPEAR, 0));
    dispatchAnimationStarted(viewHolder);
    // new pending animation added, return true to indicate we want a call to runPendingAnimations()
    return true;
}

@Override
public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
    dispatchAnimationFinished(viewHolder);
    return false;
}

@Override
public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
    if (preLayoutInfo.top != postLayoutInfo.top) {
        // required movement
        int topDiff = preLayoutInfo.top - postLayoutInfo.top;
        AnimInfo per = persistences.get(viewHolder);
        if(per != null && per.isRunning) {
            // there is already an ongoing animation - update it instead
            per.top = per.holder.itemView.getTranslationY() + topDiff;
            per.start();
            // discard this animatePersistence call
            dispatchAnimationFinished(viewHolder);
            return false;
        }
        pending.add(new AnimInfo(viewHolder, ANIMATION_TYPE_MOVE, topDiff));
        dispatchAnimationStarted(viewHolder);
        // new pending animation added, return true to indicate we want a call to runPendingAnimations()
        return true;
    }
    dispatchAnimationFinished(viewHolder);
    return false;
}

@Override
public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
    dispatchAnimationFinished(oldHolder);
    dispatchAnimationFinished(newHolder);
    return false;
}

@Override
public void runPendingAnimations() {
    for (AnimInfo ai: pending) {
        ai.start();
    }
    pending.clear();
}

@Override
public void endAnimation(RecyclerView.ViewHolder item) {
    AnimInfo ai = disappearances.get(item);
    if (ai != null && ai.isRunning) {
        ai.holder.itemView.animate().cancel();
    }
    ai = persistences.get(item);
    if (ai != null && ai.isRunning) {
        ai.holder.itemView.animate().cancel();
    }
}

@Override
public void endAnimations() {
    for (AnimInfo ai: disappearances.values())
        if (ai.isRunning)
            ai.holder.itemView.animate().cancel();

    for (AnimInfo ai: persistences.values())
        if (ai.isRunning)
            ai.holder.itemView.animate().cancel();
}

@Override
public boolean isRunning() {
    return !pending.isEmpty() &&
            !disappearances.isEmpty() &&
            !persistences.isEmpty();
}

/** 
 * This is container for each animation. It's also cancel/end listener for them.
 * */
private final class AnimInfo implements Animator.AnimatorListener {
    private final RecyclerView.ViewHolder holder;
    private final int animationType;
    private float top;
    private boolean isRunning = false;

    private AnimInfo(RecyclerView.ViewHolder holder, int animationType, float top) {
        this.holder = holder;
        this.animationType = animationType;
        this.top = top;
    }

    void start(){
        View itemView = holder.itemView;
        itemView.animate().setListener(this);
        switch (animationType) {
            case ANIMATION_TYPE_DISAPPEAR:
                itemView.setPivotY(0f);
                itemView.animate().scaleX(0f).scaleY(0f).setDuration(getRemoveDuration());
                disappearances.put(holder, this);   // must keep track of all animations
                break;
            case ANIMATION_TYPE_MOVE:
                itemView.setTranslationY(top);
                itemView.animate().translationY(0f).setDuration(getMoveDuration());
                persistences.put(holder, this);     // must keep track of all animations
                break;
        }
        isRunning = true;
    }

    private void resetViewHolderState(){
        // reset state as if no animation was ran
        switch (animationType) {
            case ANIMATION_TYPE_DISAPPEAR:
                holder.itemView.setScaleX(1f);
                holder.itemView.setScaleY(1f);
                break;
            case ANIMATION_TYPE_MOVE:
                holder.itemView.setTranslationY(0f);
                break;
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        switch (animationType) {
            case ANIMATION_TYPE_DISAPPEAR:
                disappearances.remove(holder);
                break;
            case ANIMATION_TYPE_MOVE:
                persistences.remove(holder);
                break;
        }
        resetViewHolderState();
        holder.itemView.animate().setListener(null); // clear listener
        dispatchAnimationFinished(holder);
        if (!isRunning())
            dispatchAnimationsFinished();
        isRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        // jump to end state
        switch (animationType) {
            case ANIMATION_TYPE_DISAPPEAR:
                holder.itemView.setScaleX(0f);
                holder.itemView.setScaleY(0f);
                break;
            case ANIMATION_TYPE_MOVE:
                holder.itemView.setTranslationY(0f);
                break;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
}