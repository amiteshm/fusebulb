package com.insite.fusebulb;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.insite.fusebulb.Support.UserPreference;
import com.insite.fusebulb.Support.UserPreference.Language;


/**
 * Created by amiteshmaheshwari on 26/08/16.
 */
public class UserSettingsActivity extends Activity {

    public static final String lang_en = "en";
    public static final String lang_hi = "hi";

    private RadioButton selectedLanguageRadio;
    private RadioGroup languagePref;

    private UserPreference userSettings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettings = new UserPreference();

        setContentView(R.layout.activity_user_settings);

        languagePref = (RadioGroup) findViewById(R.id.user_language_selector);
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.save_settings_btn);
        Language userLanguage = userSettings.getUserLanguage(this);

        if (userLanguage != null) {
            if (userLanguage == Language.en) {
                RadioButton b = (RadioButton) findViewById(R.id.language_english);
                b.setChecked(true);
            } else if (userLanguage == Language.hi) {
                RadioButton b = (RadioButton) findViewById(R.id.language_hindi);
                b.setChecked(true);
            }

        }

        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Resources rs = getResources();
                selectedLanguageRadio = (RadioButton) findViewById(languagePref.getCheckedRadioButtonId());
                String language = selectedLanguageRadio.getText().toString();


                if (language.equals(rs.getString(R.string.English))) {
                    userSettings.setUserLanguage(UserSettingsActivity.this, lang_en);
                } else if (language.equals(rs.getString(R.string.Hindi))) {
                    userSettings.setUserLanguage(UserSettingsActivity.this, lang_hi);
                }

                Intent mainActivityIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);

            }
        });
    }
}
