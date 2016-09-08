package com.insite.fusebulb.Custom;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.insite.fusebulb.Fragments.PagerFragment;
import com.insite.fusebulb.Models.Tour;

/**
 * Created by amiteshmaheshwari on 08/09/16.
 */


public class TourViewPager extends android.support.v4.view.ViewPager {

    float mStartDragX;
    float x = 0;
    OnSwipeOutListener mOnSwipeOutListener;
    float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 70;

    public TourViewPager(Context context) {
        super(context);
    }

    public TourViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mOnSwipeOutListener = listener;
    }

    private void onSwipeOutAtStart() {
        if (mOnSwipeOutListener!=null) {
            mOnSwipeOutListener.onSwipeOutAtStart();
        }
    }

    private void onSwipeOutAtEnd() {
        if (mOnSwipeOutListener!=null) {
            mOnSwipeOutListener.onSwipeOutAtEnd();
        }
    }

    private void onSwipeUpDown(){
        if(mOnSwipeOutListener!=null){
            mOnSwipeOutListener.onSwipeUpDown();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction() & MotionEventCompat.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mStartDragX = ev.getX();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent){

        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();

                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                float XvsY = Math.abs(deltaX) - Math.abs(deltaY);

                if (getCurrentItem() == 0 && XvsY > 0 && deltaX > MIN_DISTANCE ) {
                    onSwipeOutAtStart();
                } else if (getCurrentItem()==getAdapter().getCount()-1 && XvsY > 0 && Math.abs(deltaX) > MIN_DISTANCE) {
                    onSwipeOutAtEnd();
                } else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    onSwipeUpDown();
                }
                break;
            }
        }
        return false;

    }

    public interface OnSwipeOutListener {
        void onSwipeOutAtStart();
        void onSwipeOutAtEnd();
        void onSwipeUpDown();
    }
}
