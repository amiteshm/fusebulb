package com.insite.fusebulb.Models;

/**
 * Created by amiteshmaheshwari on 27/08/16.
 */

import android.location.Location;
import java.io.File;

public class Tour {
    private int id;
    private String name;
    private Location location;
    private File picture;
    private String summary;
    private int city;
    private int mediaSize;
    private int price;
    private String previewSource;
    private String tourSource;
    private int tourType;
    private String mediaType;

    public static final int ATTRACTION = 100;
    public static final int CITY_TOUR = 200;

    public static final String AUDIO_TYPE = "audio";
    public static final String SLIDE_TYPE = "slides";


    public Tour() {

    }

    public int getMediaSize(){
        return this.mediaSize;
    }

    public void setMediaSize(int size){
        this.mediaSize = size;
    }

    public void setMediaType(String myMedia){
        this.mediaType = myMedia;
    }

    public String getMediaType(){
        return this.mediaType;
    }
    public void setId(String id){

        this.id = Integer.parseInt(id);
    }

    public String getPreviewSource(){
        return this.previewSource;
    }

    public void setPreviewSource(String source){
        this.previewSource = source;
    }

    public void setPrice(int rate){
        this.price = rate;
    }

    public int getPrice(){
        return this.price;
    }

    public void setTourSource(String clipSource){
        this.tourSource = clipSource;
    }

    public String getTourSource(){
        return this.tourSource;
    }

    public void setId(int id){

        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCity(int city){
        this.city = city;
    }

    public int getCity(){
        return this.city;
    }



    public void setLocation(double lat, double lon){
        this.location = new Location("manual");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
    }

    public void setLocation(String lat_string, String lon_string){
        this.location = new Location("manual");
        this.location.setLatitude(Double.parseDouble(lat_string));
        this.location.setLongitude(Double.parseDouble(lon_string));
    }

    public void setPicture(File photo_file) {
        this.picture = photo_file;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }


    public void setTourType(String tourType){
        this.tourType = Integer.parseInt(tourType);
    }

    public void setTourType(int tourType){
        this.tourType = tourType;
    }

    public Integer getId(){
        return this.id;
    }

    public Location getLocation(){
        return this.location;
    }

    public File getPicture(){
        return this.picture;
    }

    public String getName(){
        return this.name;
    }

    public String getSummary(){
        return this.summary;
    }

    public int getTourType(){
        return this.tourType;
    }


}
