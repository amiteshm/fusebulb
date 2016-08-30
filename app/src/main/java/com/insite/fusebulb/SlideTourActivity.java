package com.insite.fusebulb;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;


import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Models.Slide;
import com.insite.fusebulb.Models.SlideView;
import com.insite.fusebulb.Models.TourStop;
import com.insite.fusebulb.Parsers.SlideParser;
import com.insite.fusebulb.Parsers.TourParser;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */
public class SlideTourActivity extends FragmentActivity {
    //DrawerLayout drawer;
    private final String TAG = "SlideTourActivity";
    private ListView listViewDrawer;
    private ArrayList<TourStop> stopList;
    float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 70;

    TextView descriptionView;
    Downloader downloader;
    TourStop currStop;
    Slide currSlide;

    int currStopIndex;
    int currSlideIndex;

    SlideView slideView;
    Animation slideInLeftAnimation, slideInRightAnimation;
    FragmentManager fm = getSupportFragmentManager();


    private Downloader getDownloader() {
        if (downloader == null) {
            downloader = new Downloader(this);
        }
        return downloader;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_tour);
        Bundle extras = getIntent().getExtras();
        String tourSourcePath = extras.getString("TOUR_SOURCE");
        try {
            TourParser stopsParser = new TourParser(this, tourSourcePath);
            stopsParser.execute();
            stopList = stopsParser.get();
            initializeView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        setCurrentStop(0);
        setNextSlide();

    }


    private void setCurrentStop(int index) {
        if (index >= stopList.size()) {
            closeTour();
        } else {
            currStopIndex = index;
            currStop = stopList.get(currStopIndex);
            currSlideIndex = 0;
        }
    }


    private void setPreviouSlide() {

        if (currSlideIndex == 0) {


        } else {
            try {
                currSlideIndex = currSlideIndex - 1;
                SlideParser slideParser = new SlideParser(getDownloader(), getSlidePath(currStop, currSlideIndex));
                currSlide = slideParser.execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(currSlide != null){
            slideView.setView(currSlide, slideInRightAnimation);
        }
    }

    private void setNextSlide() {
        if (currSlideIndex + 1 > currStop.getMediaSize()) {
            setCurrentStop(currStopIndex + 1);
        }
        if (currStop == null) {
            closeTour();
        } else {
            try {
                currSlideIndex = currSlideIndex + 1;
                SlideParser slideParser = new SlideParser(getDownloader(), getSlidePath(currStop, currSlideIndex));
                currSlide = slideParser.execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        slideView.setView(currSlide, slideInLeftAnimation);
    }

    private String getSlidePath(TourStop stop, int slideIndex) {
        return stop.getTourSource() + slideIndex + ".xml";
    }

    private void closeTour() {

    }

    private void initializeView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.slide_tour_toolbar);
//        setSupportActionBar(toolbar);

//        drawer = (DrawerLayout) findViewById(R.id.slide_tour_drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.slide_tour_nav_view);
//        View hView = navigationView.getHeaderView(0);


        //navigationView.setNavigationItemSelectedListener(this);


        descriptionView = (TextView) findViewById(R.id.slide_tour_slideDescription);
        findViewById(R.id.fabBulb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideView.transition();
            }
        });

        slideInLeftAnimation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_right);
        slideInRightAnimation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_left);

        slideView = new SlideView(findViewById(R.id.slide_tour));


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
                    swipeDown();
                } else if (Math.abs(deltaY) > MIN_DISTANCE) {
                    swipeUp();
                }
                break;
            }
        }
        return false;
    }

    public void swipeLeft() {
        //Toast.makeText(this, "SwipeLeft--------------", Toast.LENGTH_LONG).show();
        setNextSlide();
    }

    public void swipeRight() {
        //Toast.makeText(this, "SwipeLeft--------------", Toast.LENGTH_LONG).show();
        Log.d(TAG, "--------------SwipeRight");
        setPreviouSlide();

    }

    public void swipeDown() {
        //bringInView(descriptionView);
        slideView.transition();
    }

    public void swipeUp() {
        slideView.transition();
    }


//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_close) {
//            drawer.closeDrawer(GravityCompat.START);
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
}
