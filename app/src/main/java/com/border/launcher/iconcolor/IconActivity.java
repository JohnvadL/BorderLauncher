package com.border.launcher.iconcolor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.border.launcher.IconCache;
import com.border.launcher.LauncherAppState;
import com.border.launcher.R;
import com.border.launcher.compat.LauncherAppsCompat;
import com.border.launcher.compat.LauncherAppsCompatV16;
import com.border.launcher.compat.LauncherAppsCompatVL;
import com.border.launcher.compat.UserHandleCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class IconActivity extends Activity implements IconColorRecyclerViewAdapter.ItemClickListener {

    private PackageManager mPm;
    private Context mContext;
    IconColorRecyclerViewAdapter adapter;
    private List<ActivityInfo> activityList;

    public static String packageName;
    public static Integer newColor;
    public static String label;
    private IconCache iconCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newColor = null;
        packageName = null;
        label = null;

        setContentView(R.layout.activity_icon);
        setTitle("Change Icon Color");
        mPm = getApplicationContext().getPackageManager();
        mContext = getApplicationContext();
        activityList = getActivityList();

        Collections.sort(activityList, new Comparator<ActivityInfo>() {
            @Override
            public int compare(ActivityInfo lhs, ActivityInfo rhs) {
                return lhs.loadLabel(mContext.getPackageManager()).toString().compareTo(
                        rhs.loadLabel(mContext.getPackageManager()).toString()
                );
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.apps_list_color);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new IconColorRecyclerViewAdapter(mContext, activityList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("IconActivity", "OnResume");
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
    public void onItemClick(View view, int position) {
        ColorPickerDialogSingleApp dialog = new ColorPickerDialogSingleApp(IconActivity.this);

        label = (String) activityList.get(position).loadLabel(mContext.getPackageManager());
        packageName = activityList.get(position).packageName;

        // Simple code to add
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (newColor != null) {
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
                    app.getModel().onPackageIconsUpdated(new HashSet<>(Arrays.asList(packageName)),
                            UserHandleCompat.myUserHandle());
                    // app.getModel().forceReload(); a
                    app.reloadWorkspace();
                }
                packageName = null;
                newColor = null;
                label = null;
            }
        });

        dialog.show();
    }
}