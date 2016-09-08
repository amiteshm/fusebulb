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

    static final int MIN_DISTANCE = 200;

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
        if (mOnSwipeOutListener != null) {
            mOnSwipeOutListener.onSwipeOutAtStart();
        }
    }

    private void onSwipeOutAtEnd() {
        if (mOnSwipeOutListener != null) {
            mOnSwipeOutListener.onSwipeOutAtEnd();
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
    public boolean onTouchEvent(MotionEvent ev){

        if(getCurrentItem()==0 || getCurrentItem()==getAdapter().getCount()-1){
            final int action = ev.getAction();
            float x = ev.getX();
            boolean deltaX = Math.abs(x - mStartDragX) > MIN_DISTANCE;
            switch(action & MotionEventCompat.ACTION_MASK){
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (getCurrentItem()==0 && x>mStartDragX && deltaX) {
                        onSwipeOutAtStart();
                    }
                    if (getCurrentItem()==getAdapter().getCount()-1 && x<mStartDragX && deltaX){
                        onSwipeOutAtEnd();
                    }
                    break;
            }
        }else{
            mStartDragX=0;
        }
        return super.onTouchEvent(ev);

    }


    public interface OnSwipeOutListener {
        void onSwipeOutAtStart();

        void onSwipeOutAtEnd();

    }
}
