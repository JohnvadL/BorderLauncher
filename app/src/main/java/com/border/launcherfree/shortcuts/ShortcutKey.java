package com.border.launcherfree.shortcuts;

import android.content.ComponentName;
import android.content.Intent;

import com.border.launcherfree.ShortcutInfo;
import com.border.launcherfree.compat.UserHandleCompat;
import com.border.launcherfree.util.ComponentKey;

/**
 * A key that uniquely identifies a shortcut using its package, id, and user handle.
 */
public class ShortcutKey extends ComponentKey {

    public ShortcutKey(String packageName, UserHandleCompat user, String id) {
        // Use the id as the class name.
        super(new ComponentName(packageName, id), user);
    }

    public String getId() {
        return componentName.getClassName();
    }

    public static ShortcutKey fromInfo(ShortcutInfoCompat shortcutInfo) {
        return new ShortcutKey(shortcutInfo.getPackage(), shortcutInfo.getUserHandle(),
                shortcutInfo.getId());
    }

    public static ShortcutKey fromIntent(Intent intent, UserHandleCompat user) {
        String shortcutId = intent.getStringExtra(
                ShortcutInfoCompat.EXTRA_SHORTCUT_ID);
        return new ShortcutKey(intent.getPackage(), user, shortcutId);
    }

    public static ShortcutKey fromShortcutInfo(ShortcutInfo info) {
        return fromIntent(info.getPromisedIntent(), info.user);
    }
}
