package com.border.launcherfree.util;

import android.view.MotionEvent;

public interface TouchController {
    boolean onTouchEvent(MotionEvent ev);
    boolean onInterceptTouchEvent(MotionEvent ev);
}
