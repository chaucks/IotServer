package com.xcoder.iotserver.utensil;

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

}
