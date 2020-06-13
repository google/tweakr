package com.google.tweakr.sample.preferences;

import android.os.Bundle;

import com.google.tweakr.preferences.TweakrPreferencesRepo;
import com.google.tweakr.sample.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class TweakrPreferencesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private TweakrPreferencesRepo repo;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            repo = new TweakrPreferencesRepo(getContext());

            // Uncomment if you want to load static preferences here and add the new ones from Tweakr at the bottom.
//            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//            PreferenceScreen screen = getPreferenceScreen();

            // Comment out this line if you are loading your own static prefs above.
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getContext());


            PreferenceGroup group = new PreferenceCategory(getContext());
            screen.addPreference(group);
            repo.populatePreferences(group);

            setPreferenceScreen(screen);
        }
    }
}