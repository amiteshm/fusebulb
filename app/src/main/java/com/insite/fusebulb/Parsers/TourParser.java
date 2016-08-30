package com.insite.fusebulb.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;

import com.insite.fusebulb.Adapters.StopListAdapter;
import com.insite.fusebulb.Adapters.TourCardAdapter;
import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Models.Tour;
import com.insite.fusebulb.Models.TourStop;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 28/08/16.
 */
public class TourParser extends Parser {

    private static final String XML_TOUR_LON = "lon";
    private static final String XML_TOUR_LAT = "lat";


    private static final String TAG = "TourParser";
    private Context context;
    private Downloader downloader;
    private String tour_source;
    private ProgressDialog progDailog;


    public TourParser(Context app_context, String tour_source_path) {
        context = app_context;
        downloader = new Downloader(context);
        tour_source = tour_source_path;
    }


    @Override
    protected ArrayList doInBackground(Void... params) {
        Log.d(TAG, "Downloading file:" + tour_source);
        File cityToursFile = downloader.getFile(tour_source);
        ArrayList<TourStop> stopList = new ArrayList<TourStop>();
        try {
            FileInputStream is = new FileInputStream(cityToursFile);
            stopList = parseTourStop(context, is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopList;
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
        //tourRecyclerView.setAdapter(new StopListAdapter(context, result));
    }

    public ArrayList<TourStop> parseTourStop(Context context, InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readStops(context, parser);
    }

    private ArrayList<TourStop> readStops(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<TourStop> stopList = new ArrayList<TourStop>();
        parser.require(XmlPullParser.START_TAG, NS, "stops");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("stop")) {
                stopList.add(readStop(context, parser));
            } else {
                skip(parser);
            }
        }
        return stopList;
    }

    public TourStop readStop(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "stop");
        TourStop stop = new TourStop();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();

            if (tag.equals("name")) {
                stop.setName(readText(parser));
            } else if (tag.equals("size")) {
                stop.setMediaSize(Integer.parseInt(readText(parser)));
            } else if (tag.equals("location")) {
                stop.setLocation(parser.getAttributeValue(NS, XML_TOUR_LAT), parser.getAttributeValue(NS, XML_TOUR_LON));
                parser.nextTag();
            } else if (tag.equals("source")) {
                stop.setTourSource(readText(parser));
            } else {
                skip(parser);
            }
        }
        return stop;
    }
}
