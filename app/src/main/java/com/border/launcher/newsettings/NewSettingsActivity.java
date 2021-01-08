package com.border.launcher.newsettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.border.launcher.R;
import com.border.launcher.iconcolor.ColorPickerDialogAllApps;
import com.border.launcher.iconcolor.IconActivity;


public class NewSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);
        getActionBar().hide();

        Button allApps = findViewById(R.id.all_apps_button);
        Button oneApp = findViewById(R.id.single_app_button);


        allApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewSettingsActivity.this, ColorPickerDialogAllApps.class);
                startActivity(intent);
            }
        });

        oneApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewSettingsActivity.this, IconActivity.class);
                startActivity(intent);
            }
        });
    }
}
