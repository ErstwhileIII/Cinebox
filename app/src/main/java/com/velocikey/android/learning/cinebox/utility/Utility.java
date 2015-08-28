package com.velocikey.android.learning.cinebox.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.velocikey.android.learning.cinebox.R;

/**
 * Created by Joseph White on 28-Aug-2015
 */
public class Utility {
    // Class fields
    private static final String LOG_TAG = Utility.class.getSimpleName();
    // Object Fields

    public static String getPreferedMovieOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String movieOrder = preferences.getString(context.getString(R.string.pref_movie_order_key),
                context.getString(R.string.pref_movie_order_defaultvalue));
        return movieOrder;
    }

    public static String getLastQueryOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lastQueryOrder = preferences.getString(context.getString(R.string.pref_last_query_order), "");
        return lastQueryOrder;
    }

    public static long getLastQueryTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastQuery = preferences.getLong(context.getString(R.string.pref_last_query_time), 0);
        return lastQuery;
    }

    public static void setLastQueryInfo(Context context, String queryOrder, long queryTime) {

        SharedPreferences.Editor preferenceEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferenceEditor.putString(context.getString(R.string.pref_last_query_order), queryOrder);
        preferenceEditor.putLong(context.getString(R.string.pref_last_query_time), queryTime);
        preferenceEditor.commit();
    }

}
