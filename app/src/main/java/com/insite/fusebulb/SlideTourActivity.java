package com.insite.fusebulb;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.insite.fusebulb.Fragments.SlideCameraActionFragment;
import com.insite.fusebulb.Fragments.SlideImageFragment;
import com.insite.fusebulb.Fragments.SlideMapActionFragment;
import com.insite.fusebulb.Fragments.SlideInfoFragment;
import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Helpers.FileDownloader;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.Models.TourStop;
import com.insite.fusebulb.Parsers.SlideParser;
import com.insite.fusebulb.Parsers.TourParser;
import com.insite.fusebulb.Support.UserPreference;
import com.insite.fusebulb.Support.UserPreference.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by amiteshmaheshwari on 28/08/16.
 */
public class SlideTourActivity extends FragmentActivity {
    private final String TAG = "SlideTourActivity";
    private ArrayList<TourStop> stopList;
    float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 70;
    String tourSourcePath;
    Language langPref;
    Downloader downloader;
    Animation slideInLeftAnimation, slideInRightAnimation;
    TourPlayer tourPlayer;
    FragmentManager fmanager;
    SlideInfoFragment slideInfoFragment;
    SlideMapActionFragment slideMapActionFragment;
    SlideCameraActionFragment slideCameraActionFragment;
    SlideImageFragment slideImageFragment;


    public enum SliderType {
        IMAGE,
        INFO,
        ACTION_MAP,
        ACTION_CAMERA
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_tour);
        Bundle extras = getIntent().getExtras();
        tourSourcePath = extras.getString("TOUR_SOURCE");
    }

    protected void onStart() {
        super.onStart();
        initializeView();
        try {
            TourParser stopsParser = new TourParser(this, tourSourcePath);
            stopsParser.execute();
            stopList = stopsParser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        tourPlayer = new TourPlayer(stopList);
        tourPlayer.setCurrTour(0);

    }

    private Downloader getDownloader() {
        if (downloader == null) {
            downloader = new Downloader(this);
        }
        return downloader;
    }



    private void initializeView() {
        slideInLeftAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_right);
        slideInRightAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_left);
        fmanager = getSupportFragmentManager();
        slideInfoFragment = new SlideInfoFragment();
        slideMapActionFragment = new SlideMapActionFragment();
        slideCameraActionFragment = new SlideCameraActionFragment();
        slideImageFragment = new SlideImageFragment();

        UserPreference userSettings = new UserPreference();
        langPref = userSettings.getUserLanguage(this);
    }



    public boolean onTouchEvent(MotionEvent touchevent) {
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

                if (XvsY > 0 && deltaX > MIN_DISTANCE) {
                    swipeRight();
                } else if (XvsY > 0 && Math.abs(deltaX) > MIN_DISTANCE) {
                    swipeLeft();
                } else if (deltaY > MIN_DISTANCE) {
                    swipeUpDown();
                } else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    swipeUpDown();
                }
                break;
            }
        }
        return false;
    }

    public void swipeLeft() {
        tourPlayer.nextSlide();
    }

    public void swipeRight() {
        tourPlayer.previousSlide();
    }

    public void swipeUpDown() {
        if (tourPlayer.getSlideType() == SliderType.INFO) {
            slideInfoFragment.transition();
        }
    }

    public void nextSlide() {
        tourPlayer.nextSlide();
    }

    public void setNextStop(){
        tourPlayer.setNextStop();
    }

    public void setSlideType(SliderType slideType) {
        tourPlayer.setSliderType(slideType);
    }

    private class TourPlayer {
        private TourStop currStop;
        private int stopIndex;
        private int slideIndex;
        private SliderType sliderType;
        private ArrayList<TourStop> stopList;

        public TourPlayer(ArrayList<TourStop> list) {
            stopList = list;
        }

        public void endTour() {
            onBackPressed();
        }

        public void setSliderType(SliderType type) {

            if (this.sliderType != type) {
                this.sliderType = type;
                FragmentTransaction fragmentTransaction = fmanager.beginTransaction();
                if (sliderType == SliderType.ACTION_MAP) {
                    fragmentTransaction.replace(R.id.slide_tour_holder, slideMapActionFragment);
                } else if (sliderType == SliderType.INFO) {
                    fragmentTransaction.replace(R.id.slide_tour_holder, slideInfoFragment);
                    slideIndex = 0;
                } else if(sliderType == SliderType.ACTION_CAMERA){
                    fragmentTransaction.replace(R.id.slide_tour_holder, slideCameraActionFragment);
                } else if(sliderType == SliderType.IMAGE){
                    fragmentTransaction.replace(R.id.slide_tour_holder, slideImageFragment);
                    slideIndex = 0;
                }
                fragmentTransaction.commit();
                fmanager.executePendingTransactions();
            }
        }

        public SliderType getSlideType() {
            return this.sliderType;
        }

        public void setCurrTour(int index) {
            if (index < 0) {
                endTour();
            } else if (index >= stopList.size()) {
                endTour();
            } else {
                currStop = stopList.get(index);
                stopIndex = index;
                slideIndex = 0;
                sliderType = SliderType.INFO;
                start();
            }
        }

        public void showMapAction(TourStop stop) {
            setSliderType(SliderType.ACTION_MAP);
            FileDownloader fd = new FileDownloader(SlideTourActivity.this);
            try {
                File f = fd.execute(stop.getPicturePath()).get();
                stop.setPicturePath(f.getAbsolutePath());
                slideMapActionFragment.setView(langPref, stop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        public void showCameraAction(TourStop stop){
            setSliderType(SliderType.ACTION_CAMERA);
            FileDownloader fd = new FileDownloader(SlideTourActivity.this);
            try {
                File f = fd.execute(stop.getSampleImagePath(1)).get();
                slideCameraActionFragment.setView(langPref, f.getAbsolutePath());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        public void showInfoSlide(Slide slide, Animation animation) {
            setSliderType(SliderType.INFO);
            slideInfoFragment.setView(slide, animation);
        }

        public void showImageSlide(String path, Animation animation){
            setSliderType(SliderType.IMAGE);
            slideImageFragment.setView(path, animation);
        }

        public void start() {
            if (currStop.getLocation() != null) {
                slideIndex = 0;
                showMapAction(currStop);
            } else if (slideIndex == 0) {
                nextSlide();
            } else {
                setCurrTour(stopIndex - 1);
            }
        }

        public void nextSlide() {
            if (sliderType == SliderType.INFO) {
                if (slideIndex + 1 >= (currStop.getSlideSize()+1)) {
                    end();
                } else {
                    try {
                        slideIndex = slideIndex + 1;
                        SlideParser slideParser = new SlideParser(SlideTourActivity.this, getDownloader(), currStop.getSlidePath(slideIndex));
                        showInfoSlide(slideParser.execute().get(), slideInRightAnimation);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } else if (sliderType == SliderType.ACTION_MAP) {
                setSliderType(SliderType.INFO);
                nextSlide();
            } else if (sliderType == SliderType.ACTION_CAMERA){
                setSliderType(SliderType.IMAGE);
                nextSlide();
            } else if(sliderType == SliderType.IMAGE){
                if (slideIndex + 1 == (currStop.getPhotoSampleSize()+1)) {
                    setNextStop();
                } else {
                    try {
                        slideIndex = slideIndex + 1;
                        FileDownloader fd = new FileDownloader(SlideTourActivity.this);
                        showImageSlide(fd.execute(currStop.getSampleImagePath(slideIndex)).get().getAbsolutePath(), slideInRightAnimation);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void previousSlide() {
            if (sliderType == SliderType.INFO) {
                if (slideIndex == 1) {
                    start();
                } else {
                    try {
                        slideIndex = slideIndex - 1;
                        SlideParser slideParser = new SlideParser(SlideTourActivity.this, getDownloader(), currStop.getSlidePath(slideIndex));
                        showInfoSlide(slideParser.execute().get(), slideInLeftAnimation);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setNextStop(){
            setCurrTour(stopIndex + 1);
        }

        public void end() {
            if (currStop.getPhotoSampleSource() == null) {
                setNextStop();
            } else {
                showCameraAction(currStop);
            }
        }

    }

}
