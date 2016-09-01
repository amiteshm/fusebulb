package com.insite.fusebulb.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.insite.fusebulb.Helpers.ViewAnimationUtils;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;

/**
 * Created by amiteshmaheshwari on 01/09/16.
 */

public class SlideImageFragment extends android.support.v4.app.Fragment {

    private ImageView backgroundView;


    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_image, vg, false);

        backgroundView = (ImageView) view.findViewById(R.id.slide_image_slideBackground);

        view.findViewById(R.id.fabCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        return view;
    }

    public void setView(String filePath, Animation animation){
        backgroundView.setBackground(Drawable.createFromPath(filePath));
    }

    private void startCamera(){
        SlideTourActivity activity = ((SlideTourActivity)getActivity());
        activity.startActivity(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE));

    }


}
