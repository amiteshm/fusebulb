package com.insite.fusebulb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.insite.fusebulb.Adapters.FullScreenImageAdapter;
import com.insite.fusebulb.Fragments.PagerFragment;
import com.insite.fusebulb.Fragments.SlideCameraActionFragment;
import com.insite.fusebulb.Fragments.SlideImageFragment;
import com.insite.fusebulb.Fragments.SlideInfoFragment;
import com.insite.fusebulb.Fragments.SlideMapActionFragment;
import com.insite.fusebulb.Fragments.TourDirectionActionFragment;
import com.insite.fusebulb.Helpers.FileDownloader;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.Models.Tour;
import com.insite.fusebulb.Models.TourStop;
import com.insite.fusebulb.Parsers.AllSlideParser;
import com.insite.fusebulb.Parsers.TourParser;
import com.insite.fusebulb.Support.UserPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by amiteshmaheshwari on 06/09/16.
 */
public class PlayTourActivity extends FragmentActivity {

    private static final Integer MAP_ACTION_SLIDE = -100;
    private FullScreenImageAdapter adapter;

    private PagerFragment pagerFragment;

    FragmentManager fmanager;
    TourDirectionActionFragment tourDirectionActionFragment;
    UserPreference.Language langPref;
    String tourSourcePath;
    ArrayList<TourStop> stopList;
    Integer currTourIndex;
    Integer currSlideIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tour);
        Bundle extras = getIntent().getExtras();
        tourSourcePath = extras.getString("TOUR_SOURCE");
        TourParser tourParser = new TourParser(this, tourSourcePath);
        tourParser.execute();
        try {
            stopList = tourParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        initializeView();
    }


    private void setCurrentSlide(int slideIndex) {
        currSlideIndex = slideIndex;
    }

    public Integer getCurrentSlide() {
        if (currSlideIndex == null) {
            currSlideIndex = MAP_ACTION_SLIDE;
        }
        return currSlideIndex;
    }

    private TourStop getCurrentStop() {
        if (currTourIndex == null) {
            return stopList.get(0);
        } else {
            return stopList.get(currTourIndex);
        }
    }


    public void setCurrTour(int index) {
        if (index < stopList.size()) {
            currTourIndex = index;
        }
    }

    private void setTour(int index){
        if(index >=0 && index < stopList.size()){
            setCurrentSlide(MAP_ACTION_SLIDE);
            setCurrTour(index);
            setSlider(getCurrentStop(), getCurrentSlide());
        }
    }

    public void restartCurrTour(){
        setCurrentSlide(MAP_ACTION_SLIDE);
        setTour(getCurrentStop().getId());
    }

    public void setNextTour(){
        setTour(getCurrentStop().getId()+1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSlider(getCurrentStop(), getCurrentSlide());
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(getCurrentSlide() != MAP_ACTION_SLIDE){
            setCurrentSlide(pagerFragment.getCurrentSlide());
        }
    }

    private ArrayList<Slide> fetchSlides(TourStop currStop) {
        ArrayList<Slide> slideArrayList = null;

        try {
            AllSlideParser slideParser = new AllSlideParser(this, currStop.getTourSource());
            slideParser.execute();
            slideArrayList = slideParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return slideArrayList;
    }

    private void initializeView() {
        fmanager = getSupportFragmentManager();
        pagerFragment = new PagerFragment();
        tourDirectionActionFragment = new TourDirectionActionFragment();
        UserPreference userSettings = new UserPreference();
        langPref = userSettings.getUserLanguage(this);
    }



    public void startTour() {
        setCurrentSlide(0);
        setSlider(getCurrentStop(), getCurrentSlide());
    }


    public void setMapAction(int tourId, int index){
        setCurrentSlide(MAP_ACTION_SLIDE);



    }

    public void setSlider(TourStop stop, Integer tourSlideIndex) {
        if (tourSlideIndex == MAP_ACTION_SLIDE) {
            FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
            fragmentTransaction.replace(R.id.play_tour_holder, tourDirectionActionFragment);
            fragmentTransaction.commit();

            fmanager.executePendingTransactions();
            FileDownloader fd = new FileDownloader(PlayTourActivity.this);
            try {
                File f = fd.execute(getCurrentStop().getPicturePath()).get();
                stop.setPictureAbsolutePath(f.getAbsolutePath());
                tourDirectionActionFragment.setView(langPref, stop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            ArrayList<Slide> slideArrayList = fetchSlides(stop);
            FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
            fragmentTransaction.replace(R.id.play_tour_holder, pagerFragment);
            fragmentTransaction.commit();
            fmanager.executePendingTransactions();
            pagerFragment.setPager(slideArrayList, tourSlideIndex);
        }
    }

    public void showMapAction(TourStop stop) {
        FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
        fragmentTransaction.replace(R.id.slide_tour_holder, tourDirectionActionFragment);
        fragmentTransaction.commit();
        FileDownloader fd = new FileDownloader(PlayTourActivity.this);
        try {
            File f = fd.execute(stop.getPicturePath()).get();
            stop.setPicturePath(f.getAbsolutePath());
            tourDirectionActionFragment.setView(langPref, stop);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
