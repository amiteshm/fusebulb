package com.insite.fusebulb.Models;

import android.location.Location;

import java.io.File;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */
public class TourStop {
    private String name;
    private Location location;
    private int mediaSize;
    private String tourSource;


    public TourStop() {

    }

    public int getMediaSize() {
        return this.mediaSize;
    }

    public void setMediaSize(int size) {
        this.mediaSize = size;
    }

    public void setTourSource(String clipSource) {
        this.tourSource = clipSource;
    }

    public String getTourSource() {
        return this.tourSource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(double lat, double lon) {
        this.location = new Location("manual");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    public void setLocation(String lat_string, String lon_string) {
        this.location = new Location("manual");
        this.location.setLatitude(Double.parseDouble(lat_string));
        this.location.setLongitude(Double.parseDouble(lon_string));
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }
}

