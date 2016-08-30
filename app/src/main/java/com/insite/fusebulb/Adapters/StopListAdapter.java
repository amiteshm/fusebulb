package com.insite.fusebulb.Adapters;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Models.TourStop;
import com.insite.fusebulb.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 24/07/16.
 */

public class StopListAdapter extends BaseAdapter {

    private ArrayList<TourStop> stops;
    private LayoutInflater stopInf;
    //private String APP_FOLDER;

    public StopListAdapter(Context c, ArrayList<TourStop> theStops) {
        stops = theStops;
        stopInf = LayoutInflater.from(c);
        //APP_FOLDER = Downloader.getAppFolder();

    }

    public int getCount() {
        return stops.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (RelativeLayout) stopInf.inflate(R.layout.tour_stop, parent, false);
        }
        TextView clipTitleView = (TextView) convertView.findViewById(R.id.playlist_stop_title);
        //ImageView thumbnilView = (ImageView) convertView.findViewById(R.id.playlist_clip_thumbnil);
        TourStop currStop= stops.get(position);
        clipTitleView .setText(currStop.getName());
        //File thumbnilFile = new File(APP_FOLDER, currClip.getThumbnil());
        //thumbnilView.setImageBitmap(BitmapFactory.decodeFile(currClip.getThumbnil()));
        //thumbnilView.setImageURI(Uri.fromFile(thumbnilFile));
        convertView.setTag(position);
        return convertView;
    }

}