package com.example.avjindersinghsekhon.minimaltodo.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

import com.example.avjindersinghsekhon.minimaltodo.Analytics.AnalyticsApplication;
import com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment;
import com.example.avjindersinghsekhon.minimaltodo.R;
import com.example.avjindersinghsekhon.minimaltodo.Utility.PreferenceKeys;

import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.DARKTHEME;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.LIGHTTHEME;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.RECREATE_ACTIVITY;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.THEME_PREFERENCES;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.THEME_SAVED;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    AnalyticsApplication app;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_layout);
        app = (AnalyticsApplication) getActivity().getApplication();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceKeys preferenceKeys = new PreferenceKeys(getResources());
        if (key.equals(preferenceKeys.night_mode_pref_key)) {
            SharedPreferences themePreferences = getActivity().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor themeEditor = themePreferences.edit();
            //We tell our MainLayout to recreate itself because mode has changed
            themeEditor.putBoolean(RECREATE_ACTIVITY, true);

            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(preferenceKeys.night_mode_pref_key);
            if (checkBoxPreference.isChecked()) {
                //Comment out this line if not using Google Analytics
                app.send(this, "Settings", "Night Mode used");
                themeEditor.putString(THEME_SAVED, DARKTHEME);
            } else {
                themeEditor.putString(THEME_SAVED, LIGHTTHEME);
            }
            themeEditor.apply();

            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
