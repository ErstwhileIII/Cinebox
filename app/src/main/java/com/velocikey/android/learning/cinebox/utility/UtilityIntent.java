package com.velocikey.android.learning.cinebox.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Joseph White on 27-Aug-2015
 */
public class UtilityIntent {
    // Class fields
    private static final String LOG_TAG = UtilityIntent.class.getSimpleName();
    // Object Fields

    /**
     * Creates and starts an intent to view the item specified by the viewTarget string,
     * letting the use choose which application should hand the viewing.
     *
     * @param context    parent Activity from which to launch the intent
     * @param viewTarget string for the item to be viewed (uri)
     */
    public static void startIntent(Context context, String viewTarget) {
        startIntent(context, Uri.parse(viewTarget));
    }

    /**
     * Creates and starts an intent to view the item specified by the viewTarget URI,
     * letting the use choose which application should hand the viewing.
     *
     * @param context    parent Activity from which to launch the intent
     * @param viewTarget string for the item to be viewed (uri)
     */
    public static void startIntent(Context context, Uri viewTarget) {
        Log.v(LOG_TAG, "starting an intent to view " + viewTarget.toString() + " for " + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(viewTarget);
        context.startActivity(intent);
    }
}
