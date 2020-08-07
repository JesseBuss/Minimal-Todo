package com.example.avjindersinghsekhon.minimaltodo.Settings;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.example.avjindersinghsekhon.minimaltodo.Analytics.AnalyticsApplication;
import com.example.avjindersinghsekhon.minimaltodo.Main.MainActivity;
import com.example.avjindersinghsekhon.minimaltodo.Main.view.MainFragment;
import com.example.avjindersinghsekhon.minimaltodo.R;

import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.LIGHTTHEME;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.RECREATE_ACTIVITY;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.THEME_PREFERENCES;
import static com.example.avjindersinghsekhon.minimaltodo.Main.model.PrefsHelper.THEME_SAVED;

public class SettingsActivity extends AppCompatActivity {

    AnalyticsApplication app;

    @Override
    protected void onResume() {
        super.onResume();
        app.send(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        app = (AnalyticsApplication) getApplication();
        String theme = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getString(THEME_SAVED, LIGHTTHEME);
        if (theme.equals(LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        if (backArrow != null) {
//            backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//
//        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.mycontent, new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    if (shouldRecreate()) {
                        MainActivity.startFreshActivity(this);
                    } else {
                        NavUtils.navigateUpFromSameTask(this);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldRecreate()) {
            MainActivity.startFreshActivity(this);
        } else {
            super.onBackPressed();
        }
    }

    private boolean shouldRecreate() {
        return getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false);
    }
}
