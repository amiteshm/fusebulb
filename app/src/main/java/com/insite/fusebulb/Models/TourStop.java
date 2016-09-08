package com.insite.fusebulb.Models;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */
public class TourStop {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private String description;
    private Location location;

    private int slideSize;
    private String tourSource;

    private String picturePath;

    public String getPictureAbsolutePath() {
        return pictureAbsolutePath;
    }

    public void setPictureAbsolutePath(String pictureAbsolutePath) {
        this.pictureAbsolutePath = pictureAbsolutePath;
    }

    private String pictureAbsolutePath;

    private int photoSampleSize;
    private String photoSampleSource;

    private ArrayList<Slide> slideList;


    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }


    public TourStop() {
        slideList = new ArrayList<Slide>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPhotoSampleSource() {
        return photoSampleSource;
    }

    public void setPhotoSampleSource(String photoSampleSource) {
        this.photoSampleSource = photoSampleSource;
    }

    public int getPhotoSampleSize() {
        return photoSampleSize;
    }

    public void setPhotoSampleSize(int photoSampleSize) {
        this.photoSampleSize = photoSampleSize;
    }

    public int getSlideSize() {
        return this.slideSize;
    }

    public void setSlideSize(int size) {
        this.slideSize = size;
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

    public String getSlidePath(int slideIndex) {
        return getTourSource() + slideIndex + ".xml";
    }

    public String getSampleImagePath(int slideIndex) {
        return getPhotoSampleSource() + slideIndex + ".jpg";
    }
}

