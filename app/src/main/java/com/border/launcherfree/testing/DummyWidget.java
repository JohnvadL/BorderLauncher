package com.border.launcherfree.testing;

import android.appwidget.AppWidgetProviderInfo;

import com.border.launcherfree.CustomAppWidget;
import com.border.launcherfree.R;

public class DummyWidget implements CustomAppWidget {
    @Override
    public String getLabel() {
        return "Dumb Launcher Widget";
    }

    @Override
    public int getPreviewImage() {
        return 0;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public int getWidgetLayout() {
        return R.layout.zzz_dummy_widget;
    }

    @Override
    public int getSpanX() {
        return 2;
    }

    @Override
    public int getSpanY() {
        return 2;
    }

    @Override
    public int getMinSpanX() {
        return 1;
    }

    @Override
    public int getMinSpanY() {
        return 1;
    }

    @Override
    public int getResizeMode() {
        return AppWidgetProviderInfo.RESIZE_BOTH;
    }
}
