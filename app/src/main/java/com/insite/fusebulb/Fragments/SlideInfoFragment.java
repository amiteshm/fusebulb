package com.insite.fusebulb.Fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insite.fusebulb.Helpers.ViewAnimationUtils;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.R;

/**
 * Created by amiteshmaheshwari on 30/08/16.
 */
public class SlideInfoFragment extends Fragment {

    private TextView titleView;
    private TextView descriptionView;
    private ImageView backgroundView;

    public enum DisplayState {
        TITLE,
        DESCRIPTION,
        BLANK
    }

    private DisplayState slideDisplayState;

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_info, vg, false);

        titleView = (TextView) view.findViewById(R.id.slide_info_slideTitle);
        descriptionView = (TextView) view.findViewById(R.id.slide_info_slideDescription);
        backgroundView = (ImageView) view.findViewById(R.id.slide_info_slideBackground);

        view.findViewById(R.id.fabBulb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transition();
            }
        });

        return view;
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
        descriptionView.setVisibility(View.GONE);
        slideDisplayState = DisplayState.TITLE;
    }


}
