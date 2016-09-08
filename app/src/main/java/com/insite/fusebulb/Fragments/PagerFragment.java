package com.insite.fusebulb.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.insite.fusebulb.Adapters.FullScreenImageAdapter;
import com.insite.fusebulb.Custom.TourViewPager;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.Parsers.AllSlideParser;
import com.insite.fusebulb.PlayTourActivity;
import com.insite.fusebulb.R;
import com.insite.fusebulb.SlideTourActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.v4.view.ViewPager.*;

/**
 * Created by amiteshmaheshwari on 06/09/16.
 */


public class PagerFragment extends Fragment {
    private TourViewPager viewPager;
    private FullScreenImageAdapter adapter;
    protected PlayTourActivity activity;

    float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 70;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_pager_layout, container, false);
        viewPager = (TourViewPager) view.findViewById(R.id.pager);
        activity = ((PlayTourActivity) getActivity());
        return view;
    }

    public void setPager(final ArrayList<Slide> slideArrayList, int current_item) {
        adapter = new FullScreenImageAdapter(activity,
                slideArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(current_item);


        TourViewPager.OnSwipeOutListener l = new TourViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
                activity.restartCurrTour();
            }

            @Override
            public void onSwipeOutAtEnd() {
                activity.setNextTour();
            }

        };
        viewPager.setOnSwipeOutListener(l);
    }

    public Integer getCurrentSlide() {
        return viewPager.getCurrentItem();
    }


}