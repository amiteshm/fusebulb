package com.insite.fusebulb.Adapters;

/**
 * Created by amiteshmaheshwari on 05/09/16.
 */

import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewGroup;

import com.insite.fusebulb.Custom.TouchImageView;
import com.insite.fusebulb.Helpers.ViewAnimationUtils;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Slide> _slideList;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<Slide> imagePaths) {
        this._activity = activity;
        this._slideList= imagePaths;
    }

    public enum DisplayState {
        TITLE,
        DESCRIPTION,
        BLANK
    }

    private DisplayState slideDisplayState;


    @Override
    public int getCount() {
        return this._slideList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CoordinatorLayout) object);
    }

    public void setSlideState(DisplayState state){
        this.slideDisplayState = state;
    }

    public DisplayState getSlideState(){
        return slideDisplayState;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final TextView titleView, descriptionView;
        ImageView backgroundImage;

        Slide slide = _slideList.get(position);

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.slide_info, container,
                false);
        
        titleView = (TextView) viewLayout.findViewById(R.id.slide_info_slideTitle);
        descriptionView = (TextView) viewLayout.findViewById(R.id.slide_info_slideDescription);
        backgroundImage = (ImageView) viewLayout.findViewById(R.id.slide_info_slideBackground);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        titleView.setText(slide.getTitle());
        descriptionView.setText(slide.getDescription());

        backgroundImage.setBackground(Drawable.createFromPath(slide.getPicturePath()));

        ViewAnimationUtils.expand(titleView, true);
        descriptionView.setVisibility(View.GONE);
        setSlideState(DisplayState.TITLE);

        viewLayout.findViewById(R.id.fabBulb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transition(titleView, descriptionView, getSlideState());
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((CoordinatorLayout) object);
    }

    public void transition(TextView titleView, TextView descriptionView, DisplayState state){
        if(state == DisplayState.BLANK ){
            ViewAnimationUtils.expand(titleView, false);
            setSlideState(DisplayState.TITLE);
        } else if(state == DisplayState.TITLE){
            ViewAnimationUtils.expand(descriptionView, false);
            setSlideState(DisplayState.DESCRIPTION);
        }
        else if(state == DisplayState.DESCRIPTION) {
            ViewAnimationUtils.collapse(descriptionView);
            ViewAnimationUtils.collapse(titleView);
            setSlideState(DisplayState.BLANK);
        }
    }


}