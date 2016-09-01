package com.insite.fusebulb.Parsers;

/**
 * Created by amiteshmaheshwari on 27/08/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import com.insite.fusebulb.Adapters.TourCardAdapter;
import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Models.Tour;
import com.insite.fusebulb.Support.UserPreference;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CityParser extends Parser {

    private static final String XML_TOUR_LON =  "lon";
    private static final String XML_TOUR_LAT =  "lat";

    private static final String TAG = "CityParser";
    private Context context;
    private Downloader downloader;
    private String tour_source;
    private RecyclerView tourRecyclerView;
    private ProgressDialog progDailog;
    private UserPreference.Language languagePref;
    private MediaPlayer previewPlayer;


    public CityParser(Context app_context, UserPreference.Language language, String tour_source_path, RecyclerView recyclerView){
        context = app_context;
        downloader = new Downloader(context);
        tour_source = tour_source_path;
        tourRecyclerView = recyclerView;
        this.languagePref = language;
    }


    @Override
    protected ArrayList doInBackground(Void... params) {
        Log.d(TAG, "Downloading file:" + tour_source);
        File cityToursFile = downloader.getFile(tour_source);
        ArrayList<Tour> tourList = new ArrayList<Tour>();
        try {
            FileInputStream is = new FileInputStream(cityToursFile);
            tourList = parseCityTours(context, is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tourList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }


    @Override
    protected void onPostExecute(ArrayList result) {
        super.onPostExecute(result);
        progDailog.dismiss();
        //TODO
        tourRecyclerView.setAdapter(new TourCardAdapter(context, languagePref, result));
    }

    public ArrayList<Tour> parseCityTours(Context context, InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readTours(context, parser);
    }

    private ArrayList<Tour> readTours(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Tour> tourList = new ArrayList<Tour>();
        parser.require(XmlPullParser.START_TAG, NS, "tours");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("tour")){
                tourList.add(readTour(context, parser));
            } else {
                skip(parser);
            }
        }
        return tourList;
    }

    public Tour readTour(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "tour");
        Tour tour= new Tour();
        while (parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if(tag.equals("id")){
                tour.setId(Integer.parseInt(readText(parser)));
            }
            else if(tag.equals("name")){
                tour.setName(readText(parser));
            }
            else if(tag.equals("tour_type")){
                tour.setTourType(Integer.parseInt(readText(parser)));
            } else if(tag.equals("media_type")){
                tour.setMediaType(readText(parser));
            } else if(tag.equals("size")){
                tour.setMediaSize(Integer.parseInt(readText(parser)));
            }
            else if(tag.equals("location")){
                tour.setLocation(parser.getAttributeValue(NS, XML_TOUR_LAT), parser.getAttributeValue(NS, XML_TOUR_LON));
                parser.nextTag();
            } else if(tag.equals("picture")){
                String file_path = readText(parser);
                tour.setPicture(downloader.getFile(file_path));
            } else if (tag.equals("summary")){
                tour.setSummary(readText(parser));
            } else if(tag.equals("price")){
                tour.setPrice(Integer.parseInt(readText(parser)));
            } else if(tag.equals("preview")){
                String file_path = readText(parser);
                tour.setPreviewSource(file_path);
                downloader.getFile(file_path);
            } else if(tag.equals("clips")){
                tour.setTourSource(readText(parser));
            }
            else {
                skip(parser);
            }
        }
        return tour;
    }
}
