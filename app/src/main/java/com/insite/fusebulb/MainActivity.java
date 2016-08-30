package com.insite.fusebulb;

/**
 * Created by amiteshmaheshwari on 26/08/16.
 */


import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.content.Intent;

//import com.fusebulb.sidebar.Parser.CityParser;

import com.insite.fusebulb.Parsers.CityParser;
import com.insite.fusebulb.Support.UserPreference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private UserPreference userSettings;
    private String userLanguage;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettings = new UserPreference();
        userLanguage = getUserLanguagePref();
        if (userLanguage == null || userLanguage.equals("")) {
            Intent userSettingIntent = new Intent(this, UserSettingsActivity.class);
            startActivity(userSettingIntent);
        }

        setContentView(R.layout.activity_main);

        // MENU START
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

//        //MENU END
        RecyclerView cityRecyclerView;
        cityRecyclerView = (RecyclerView) findViewById(R.id.tour_list_view);
        cityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cityRecyclerView.setLayoutManager(mLayoutManager);

        MediaPlayer mediaPlayer = new MediaPlayer();
        CityParser cityParser = new CityParser(this, getUserLanguagePref(), mediaPlayer, "delhi_tours.xml", cityRecyclerView);
        cityParser.execute();

    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }


    public String getUserLanguagePref() {
        return userSettings.getUserLanguage(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_close) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_language) {
            Intent userSettingIntent = new Intent(this, UserSettingsActivity.class);
            startActivity(userSettingIntent);
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            Resources rs = getResources();
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, rs.getString(R.string.share_app_message));
            startActivity(shareIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
