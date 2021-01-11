package com.border.launcher.iconcolor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.border.launcher.LauncherAppState;
import com.border.launcher.R;
import com.border.launcher.compat.LauncherAppsCompat;
import com.border.launcher.compat.LauncherAppsCompatV16;
import com.border.launcher.compat.LauncherAppsCompatVL;
import com.border.launcher.compat.UserHandleCompat;
import com.flask.colorpicker.ColorPickerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ColorPickerDialogAllApps extends Activity implements View.OnClickListener {

    ColorPickerView colorPicker;
    private PackageManager mPm;
    private Context mContext;
    private ProgressBar progressBar;
    Button applyButton;
    Button cancelButton;
    LauncherAppState app = LauncherAppState.getInstanceNoCreate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define based features for an alert Dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.color_picker_dialog);

        applyButton = findViewById(R.id.dialog_color_apply_button);
        applyButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.dialog_color_cancel_button);
        cancelButton.setOnClickListener(this);


        progressBar = findViewById(R.id.progress_bar);
        colorPicker = findViewById(R.id.color_picker_view);
        mPm = getApplicationContext().getPackageManager();
        mContext = getApplicationContext();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_color_apply_button:
                progressBar.setVisibility(View.VISIBLE);
                applyButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                Thread mThread = new Thread() {
                    @Override
                    public void run() {

                        int newColor = colorPicker.getSelectedColor();
                        List<ActivityInfo> packages = getActivityList();
                        progressBar.setMax(packages.size());
                        int progress = 0;
                        List<String> packageNames = new ArrayList<>();

                        for (ActivityInfo activity : packages) {
                            progressBar.setProgress(progress);

                            String label = activity.loadLabel(mPm).toString();
                            String packageName = activity.packageName;

                            SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString
                                    (R.string.preference_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt(label, newColor);
                            editor.apply();

                            String hexColor = String.format("#%06X", (0xFFFFFF & newColor));
                            Log.e("IconActivity", "Applying Color:" + hexColor +
                                    " to " + packageName);

                            LauncherAppsCompat model = LauncherAppsCompat.getInstance(mContext);

                            if (model instanceof LauncherAppsCompatV16) {
                                for (
                                        LauncherAppsCompat.OnAppsChangedCallbackCompat callback :
                                        ((LauncherAppsCompatV16) model).mCallbacks) {
                                    callback.onPackageAdded(packageName, UserHandleCompat.myUserHandle());
                                }
                            } else {
                                for (
                                        LauncherAppsCompat.OnAppsChangedCallbackCompat callback :
                                        ((LauncherAppsCompatVL) model).getCallbacks()) {
                                    callback.onPackageAdded(packageName, UserHandleCompat.myUserHandle());
                                }
                            }

                            LauncherAppState app = LauncherAppState.getInstanceNoCreate();
                            app.getIconCache().updateIconsForPkg(packageName, UserHandleCompat.myUserHandle());
                            packageNames.add(packageName);
                            progress++;
                        }

                        app.getModel().onPackageIconsUpdated(new HashSet<>(packageNames),
                                UserHandleCompat.myUserHandle());
                        app.reloadWorkspace();
                        finish();
                        ColorPickerDialogAllApps.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                };

                mThread.start();
                break;
            default:
                finish();
                ColorPickerDialogAllApps.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
