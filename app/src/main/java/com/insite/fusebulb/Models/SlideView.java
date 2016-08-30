package com.insite.fusebulb.Models;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insite.fusebulb.Helpers.ViewAnimationUtils;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;

/**
 * Created by amiteshmaheshwari on 30/08/16.
 */
public class SlideView {

    private TextView titleView;
    private TextView descriptionView;
    private ImageView backgroundView;
    private LinearLayout contentView;
    private Animation slideAnimation;
    private View rootView;

    public enum DisplayState {
        TITLE,
        DESCRIPTION,
        BLANK
    }

    private DisplayState slideDisplayState;

    public SlideView(View root){
        titleView = (TextView) root.findViewById(R.id.slide_tour_slideTitle);
        descriptionView = (TextView) root.findViewById(R.id.slide_tour_slideDescription);
        backgroundView = (ImageView) root.findViewById(R.id.slide_tour_slideBackground);
        contentView = (LinearLayout) root.findViewById(R.id.slide_tour_contentView);
        rootView = root;
    }

    public void transition(){
        if(slideDisplayState == DisplayState.BLANK ){
            ViewAnimationUtils.expand(titleView, false);
            slideDisplayState = DisplayState.TITLE;
        } else if(slideDisplayState == DisplayState.TITLE){
            ViewAnimationUtils.expand(descriptionView, false);
            slideDisplayState = DisplayState.DESCRIPTION;
        }
        else if(slideDisplayState == DisplayState.DESCRIPTION) {
            ViewAnimationUtils.collapse(descriptionView);
            ViewAnimationUtils.collapse(titleView);
            slideDisplayState = DisplayState.BLANK;
        }
    }


    public void setView(Slide slide, Animation animation){
        titleView.setText(slide.getTitle());
        descriptionView.setText(slide.getDescription());
        backgroundView.setBackground(Drawable.createFromPath(slide.getPicturePath()));
        ViewAnimationUtils.expand(titleView, true);
        //titleView.setVisibility(View.VISIBLE);

        descriptionView.setVisibility(View.GONE);
        slideDisplayState = DisplayState.TITLE;
    }


}
