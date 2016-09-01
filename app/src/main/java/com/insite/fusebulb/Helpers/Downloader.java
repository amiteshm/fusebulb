package com.insite.fusebulb.Helpers;

/**
 * Created by amiteshmaheshwari on 27/08/16.
 */

import android.content.Context;

import com.insite.fusebulb.Support.UserPreference;
import com.insite.fusebulb.Support.UserPreference.Language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by amiteshmaheshwari on 13/07/16.
 */

public class Downloader {

    private static final String TAG = "Downloader";
    public static String HOST_NAME;
    public static String APP_FOLDER = "";
    private static Context context;
    private static Language langPref;
    private static NetworkHelper networkHelper;


    public Downloader(Context mContext){
        UserPreference userSettings = new UserPreference();
        this.context = mContext;
        this.langPref = userSettings.getUserLanguage(context);
        this.APP_FOLDER = getAppFolder();
        this.HOST_NAME = getHostName();
        this.networkHelper = new NetworkHelper(context);
    }


    public static String getLanguageString(){
        if(langPref == Language.hi){
            return "hi";
        }else{
            return "en";
        }
    }

    public static String getHostName(){
        return "http://phitoor.com/insite1/" + getLanguageString() +"/";
    }

    public static String getAppFolder(){
        return context.getFilesDir().toString()+"/insite/" + getLanguageString()+"/";
    }

    public static File getFile(String file_path) {
        File file = new File(APP_FOLDER, file_path);

        try {

            if (!file.exists()) {
                networkHelper.checkForInternetConnectivity();

                URL url = new URL(HOST_NAME + file_path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.connect();

                File mydir = file.getParentFile();

                if (!mydir.exists()) {
                    mydir.mkdirs();
                }

                FileOutputStream fileOutput = new FileOutputStream(file);
                //Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                int downloadedSize = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fileOutput.close();
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}

