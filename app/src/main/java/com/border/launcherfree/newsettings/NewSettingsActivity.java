package com.border.launcherfree.newsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.border.launcherfree.R;
import com.border.launcherfree.iconcolor.ColorPickerDialogAllApps;
import com.border.launcherfree.iconcolor.IconActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class NewSettingsActivity extends Activity {

    Button allApps;
    Button oneApp;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);
        getActionBar().hide();

        allApps = findViewById(R.id.all_apps_button);
        oneApp = findViewById(R.id.single_app_button);
        allApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allApps.setEnabled(false);
                Intent intent = new Intent(NewSettingsActivity.this, ColorPickerDialogAllApps.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        oneApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneApp.setEnabled(false);
                Intent intent = new Intent(NewSettingsActivity.this, IconActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        allApps.setEnabled(true);
        oneApp.setEnabled(true);
    }

}
