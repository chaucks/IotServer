package com.xcoder.iotserver.utensil;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

public class X {

    public static final int DEFAULT_STRING_BUILDER_CAPACITY = 512;

    public static final File EXTERNAL_STORAGE_DIRECTORY_FILE = Environment.getExternalStorageDirectory();

    public static final String EXTERNAL_STORAGE_DIRECTORY_PATH = EXTERNAL_STORAGE_DIRECTORY_FILE.getPath();

    public static final String EXTERNAL_STORAGE_DIRECTORY_ABSOLUTE_PATH = EXTERNAL_STORAGE_DIRECTORY_FILE.getAbsolutePath();

    /**
     * Get android external storage path
     *
     * @param relativePaths relativePaths
     * @return path
     */
    public static String appendExternalStorageDirectory(final String... relativePaths) {
        final StringBuilder builder = new StringBuilder(DEFAULT_STRING_BUILDER_CAPACITY);
        builder.append(EXTERNAL_STORAGE_DIRECTORY_ABSOLUTE_PATH);
        for (String path : relativePaths) {
            if (null != path) {
                builder.append(path);
            }
        }
        final String result = builder.toString();
        return result;
    }

    public static void objectsNotNull(Object... objects) {
        objectNotNull(objects);
        for (Object object : objects) {
            objectNotNull(object);
        }
    }

    public static void objectNotNull(Object object) {
        if (null == object) {
            throw new NullPointerException();
        }
    }

    public static boolean objectEqualsExists(Object object, Object... objects) {
        for (Object object0 : objects) {
            if (object.equals(object0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean objectEqualsOrValueSameExists(Object object, Object... objects) {
        for (Object object0 : objects) {
            if (object.equals(object0)) {
                return true;
            }
            if (object == object0) {
                return true;
            }
        }
        return false;
    }

    public static IntentFilter getIntentFilter(final String... actions) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        return intentFilter;
    }

    public static boolean actionMatch(Intent intent, Object... actions) {
        String action = intent.getAction();
        if (null == action) {
            return false;
        }
        return objectEqualsExists(action, actions);
    }

    public static boolean extraMatch(Intent intent, String key, Object defaultExtra, Object... extras) {
        Object extra = getExtraOrDefault(intent.getExtras(), key, defaultExtra);
        if (null == extra) {
            return false;
        }
        return objectEqualsOrValueSameExists(extra, extras);
    }

    public static Object getExtraOrDefault(Bundle bundle, String key, Object defaultExtra) {
        Object extra = bundle.get(key);
        if (null == extra) {
            return defaultExtra;
        }
        return extra;
    }
}
