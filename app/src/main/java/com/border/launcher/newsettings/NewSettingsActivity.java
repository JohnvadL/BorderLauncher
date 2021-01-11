package com.border.launcher.newsettings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.border.launcher.R;
import com.border.launcher.iconcolor.ColorPickerDialogAllApps;
import com.border.launcher.iconcolor.IconActivity;
import com.border.launcher.iconcolor.QSBColorActivity;


public class NewSettingsActivity extends Activity {

     Button allApps;
     Button oneApp;
//     Button searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);
        getActionBar().hide();

        allApps = findViewById(R.id.all_apps_button);
        oneApp = findViewById(R.id.single_app_button);
//        searchBar = findViewById(R.id.search_color_change);
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


//        searchBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                QSBColorActivity dialog = new QSBColorActivity(NewSettingsActivity.this);
//
//                // Simple code to add
//                if (dialog.getWindow() != null) {
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                }
//
//                dialog.show();
//
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        allApps.setEnabled(true);
        oneApp.setEnabled(true);
    }

}
