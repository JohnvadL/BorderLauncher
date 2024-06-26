/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.border.launcherfree.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.UserHandle;

import com.border.launcherfree.Utilities;
import com.border.launcherfree.util.LongArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UserManagerCompatVL extends UserManagerCompatV17 {
    private static final String USER_CREATION_TIME_KEY = "user_creation_time_";

    private final PackageManager mPm;
    private final Context mContext;

    UserManagerCompatVL(Context context) {
        super(context);
        mPm = context.getPackageManager();
        mContext = context;
    }

    @Override
    public void enableAndResetCache() {
        synchronized (this) {
            mUsers = new LongArrayMap<>();
            mUserToSerialMap = new HashMap<>();
            List<UserHandle> users = mUserManager.getUserProfiles();
            if (users != null) {
                for (UserHandle user : users) {
                    long serial = mUserManager.getSerialNumberForUser(user);
                    UserHandleCompat userCompat = UserHandleCompat.fromUser(user);
                    mUsers.put(serial, userCompat);
                    mUserToSerialMap.put(userCompat, serial);
                }
            }
        }
    }

    @Override
    public List<UserHandleCompat> getUserProfiles() {
        synchronized (this) {
            if (mUsers != null) {
                List<UserHandleCompat> users = new ArrayList<>();
                users.addAll(mUserToSerialMap.keySet());
                return users;
            }
        }

        List<UserHandle> users = mUserManager.getUserProfiles();
        if (users == null) {
            return Collections.emptyList();
        }
        ArrayList<UserHandleCompat> compatUsers = new ArrayList<UserHandleCompat>(
                users.size());
        for (UserHandle user : users) {
            compatUsers.add(UserHandleCompat.fromUser(user));
        }
        return compatUsers;
    }

    @Override
    public CharSequence getBadgedLabelForUser(CharSequence label, UserHandleCompat user) {
        if (user == null) {
            return label;
        }
        return mPm.getUserBadgedLabel(label, user.getUser());
    }

    @Override
    public long getUserCreationTime(UserHandleCompat user) {
        SharedPreferences prefs = Utilities.getPrefs(mContext);
        String key = USER_CREATION_TIME_KEY + getSerialNumberForUser(user);
        if (!prefs.contains(key)) {
            prefs.edit().putLong(key, System.currentTimeMillis()).apply();
        }
        return prefs.getLong(key, 0);
    }
}

