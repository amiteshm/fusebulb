package com.insite.fusebulb.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insite.fusebulb.Models.TourStop;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;
import com.insite.fusebulb.Support.UserPreference.Language;

import java.util.Locale;


/**
 * Created by amiteshmaheshwari on 30/08/16.
 */
public class SlideCameraActionFragment extends Fragment {
    TextView primaryText;
    Button skipButton, showSuggestionBtn;
    ImageView background;
    SlideTourActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_action_camera, container, false);

        primaryText = (TextView) view.findViewById(R.id.primaryText_slidePrimaryActionCamera);
        showSuggestionBtn = (Button) view.findViewById(R.id.primaryBtn_slideActionCamera);
        skipButton = (Button) view.findViewById(R.id.btn_slideSecondaryActionCamera);
        background = (ImageView)view.findViewById(R.id.background_slideActionCamera);
        activity = ((SlideTourActivity)getActivity());
        return view;
    }

    public void setView(Language languagePref, final String file_path) {
        primaryText.setText(formatPrimaryText(languagePref));
        showSuggestionBtn.setText(formatSecondaryText(languagePref));

        background.setBackground(Drawable.createFromPath(file_path));
        showSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.nextSlide();
            }
        });

        skipButton.setText(formatSkipText(languagePref));
        skipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                activity.setNextStop();
            }
        });
    }

    private String formatSecondaryText(Language language){
        if(language == Language.hi){
            return activity.getResources().getString(R.string.hi_see_suggestion);
        } else{
            return activity.getResources().getString(R.string.en_see_suggestion);
        }
    }

    private String formatPrimaryText(Language language){
        if(language == Language.hi){
            return activity.getResources().getString(R.string.hi_take_photos);
        } else{
            return activity.getResources().getString(R.string.en_take_photos);
        }
    }

    private String formatSkipText(Language language){
        if(language == Language.hi){
            return activity.getResources().getString(R.string.hi_skip_take_photo);
        } else{
            return activity.getResources().getString(R.string.en_skip_take_photo);
        }
    }


}

