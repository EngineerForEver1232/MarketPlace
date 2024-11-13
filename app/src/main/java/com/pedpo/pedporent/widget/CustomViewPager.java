package com.pedpo.pedporent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.pedpo.pedporent.widget.calendar.SwipeDirection;

public class CustomViewPager extends ViewPager {

    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (this.isSwipeAllowed(event)) {
//            return super.onInterceptTouchEvent(event);
//        }
//
//        return false;
//    }
//
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if (this.isSwipeAllowed(event)) {
//            return super.onInterceptTouchEvent(event);
//        }
//
//        return false;
//    }


    private boolean isSwipeAllowed(MotionEvent event) {
        if(this.direction == SwipeDirection.ALL) return true;

        if(direction == SwipeDirection.NONE )//disable any swipe
            return false;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.RIGHT ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 0 && direction == SwipeDirection.LEFT ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }




    private float initialXValue;
    private SwipeDirection direction;

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}