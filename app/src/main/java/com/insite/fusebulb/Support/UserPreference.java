package com.insite.fusebulb.Support;

/**
 * Created by amiteshmaheshwari on 26/08/16.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.insite.fusebulb.UserSettingsActivity;


public class UserPreference {
    public static final String USER_SETTINGS= "USER_SETTINGS";
    public static final String USER_LANG = "language";

    public enum Language {
        en,
        hi
    }

    public UserPreference (){
    }


    public void setUserLanguage(Context context, String user_language){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_LANG, user_language);
        editor.commit();
    }


    public Language getUserLanguage(Context context){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        String userLang = settings.getString(USER_LANG, null);

        if(userLang == null){
            return null;
        } else if(userLang.equals("en")){
            return Language.en;
        } else if(userLang.equals("hi")) {
            return Language.hi;
        }
        return null;

    }


}
