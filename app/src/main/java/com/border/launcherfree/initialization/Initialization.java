package com.border.launcherfree.initialization;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.border.launcherfree.R;

import java.util.ArrayList;
import java.util.List;

public class Initialization extends Activity {

    private PackageManager mPm;
    private static int counter = 0;
    public static Boolean initializing = false;
    private ProgressBar progress;
    private static int maxProgress;
    private static Button finishSelf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializing = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        ActionBar actionBar = getActionBar();
        actionBar.hide();


        mPm = getApplicationContext().getPackageManager();
        maxProgress = getActivityList().size();
        Log.e("Initialization", "MaxProgress" + maxProgress);
        progress = findViewById(R.id.loading_bar);

        finishSelf = findViewById(R.id.init_get_started_button);
        finishSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public List<ActivityInfo> getActivityList() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = mPm.queryIntentActivities(mainIntent, 0);
        List<ActivityInfo> list =
                new ArrayList<>(infos.size());
        for (ResolveInfo info : infos) {
            list.add(info.activityInfo);
        }
        return list;
    }

    public synchronized static void updateProgress() {
        ++counter;
        float percentage = (float) counter/ maxProgress * 100;

        finishSelf.setText(String.format("%s%%", (int) percentage));
        if (counter >= maxProgress) {
            finishSelf.setText(R.string.start_launcher);
        }
        Log.e("Initialization", "progress:" + counter);
    }
}
