package com.velocikey.android.learning.cinebox.utility;

/**
 * Created by Joseph White on 25-Aug-2015
 */
public class DebugHelper {
    // Class fields
    private static final String LOG_TAG = DebugHelper.class.getSimpleName();

    // Object Fields
    public static String getMessage(String message, Object testForNull) {
        if (testForNull == null) {
            return message + " is null.";
        } else {
            return message + " is NOT null.";
        }
    }
}
