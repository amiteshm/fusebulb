package com.insite.fusebulb.Fragments;


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
public class SlideMapActionFragment extends Fragment {

    TextView titleText, descriptionText;
    Button skipButton, primaryActionButton;
    ImageView background;
    SlideTourActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_action_map, container, false);
        titleText = (TextView) view.findViewById(R.id.primaryText_slidePrimaryAction);
        descriptionText = (TextView) view.findViewById(R.id.description_slidePrimaryAction);
        primaryActionButton = (Button) view.findViewById(R.id.secondaryText_slidePrimaryAction);
        skipButton = (Button) view.findViewById(R.id.btn_slideSecondaryAction);
        background = (ImageView)view.findViewById(R.id.background_slideAction);
        activity = ((SlideTourActivity)getActivity());
        return view;
    }

    public void setView(Language languagePref, final TourStop tour) {
        titleText.setText(tour.getName());
        primaryActionButton.setText(formatSecondaryText(languagePref));
        descriptionText.setText(tour.getDescription())  ;
        background.setBackground(Drawable.createFromPath(tour.getPicturePath()));
        primaryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_directions(tour);
            }
        });

        skipButton.setText(formatSkipText(languagePref));
        skipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                activity.nextSlide();
            }
        });
    }

    private String formatSecondaryText(Language language){
        if(language == Language.hi){
            return activity.getResources().getString(R.string.hi_show_directions);
        } else{
            return activity.getResources().getString(R.string.en_show_directions);
        }
    }

    private String formatSkipText(Language language){
        if(language == Language.hi){
            return activity.getResources().getString(R.string.hi_skip_show_direction);
        } else{
            return activity.getResources().getString(R.string.en_skip_show_direction);
        }
    }

    private void show_directions(TourStop tour){
        final Location location = tour.getLocation();
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", location.getLatitude(), location.getLongitude(), tour.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        activity.startActivity(intent);
    }


}

