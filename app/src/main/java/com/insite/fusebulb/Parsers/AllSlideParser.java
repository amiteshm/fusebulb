package com.insite.fusebulb.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.insite.fusebulb.Helpers.Downloader;
import com.insite.fusebulb.Models.Slide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by amiteshmaheshwari on 06/09/16.
 */
public class AllSlideParser extends AsyncTask<Void, Integer, ArrayList<Slide>> {

    private static final String TAG = "TourParser";
    protected static final String NS = null;
    private Context context;
    private Downloader downloader;
    private String slide_source;
    private ProgressDialog progDailog;


    public AllSlideParser(Context app_context,  String slide_source_path) {
        context = app_context;
        downloader = new Downloader(context);
        slide_source = slide_source_path;
    }

    @Override
    protected ArrayList<Slide> doInBackground(Void... params) {
        Log.d(TAG, "Downloading file:" + slide_source);
        File cityToursFile = downloader.getFile(slide_source);
        ArrayList<Slide> slides = null;
        try {
            FileInputStream is = new FileInputStream(cityToursFile);
            slides = parseSlides(is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return slides;
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
    protected void onPostExecute(ArrayList<Slide> result) {
        super.onPostExecute(result);
        progDailog.dismiss();
        //tourRecyclerView.setAdapter(new StopListAdapter(context, result));
    }

    public ArrayList<Slide> parseSlides(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readSlides(parser);
    }


    public ArrayList<Slide> readSlides(XmlPullParser parser) throws XmlPullParserException, IOException{
        ArrayList<Slide> slidesList = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NS, "slides");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("slide")){
                slidesList.add(readSlide(parser));
            } else {
                skip(parser);
            }
        }
        return slidesList;
    }

    public Slide readSlide(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, NS, "slide");
        Slide slide = new Slide();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();

            if (tag.equals("title")) {
                slide.setTitle(readText(parser));
            } else if (tag.equals("description")) {
                slide.setDescription(readText(parser));
            } else if (tag.equals("picture")) {
                File f = downloader.getFile(readText(parser));
                slide.setPicturePath(f.getAbsolutePath());
            } else {
                skip(parser);
            }
        }
        return slide;
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    protected String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
