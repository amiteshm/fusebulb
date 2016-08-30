package com.insite.fusebulb.Models;

import java.io.File;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */

public class Slide {
    String title;
    String description;
    String picturePath;

    public void setTitle(String s) {
        this.title = s;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String s) {
        this.description = s;
    }

    public void setPicturePath(String s){
        this.picturePath = s;
    }

    public String getPicturePath(){
        return this.picturePath;
    }
}
