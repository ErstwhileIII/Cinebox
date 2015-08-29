# Cinebox
List movies by popularity or rating from The Movie Database (TMDB).

Note. You will need to implement a class that holds the API key for TMDB.  The class should be like the one below (whter "api+key"
is replaced by your TMDB API key value:


package com.velocikey.android.learning.cinebox.webinfo;

/**
 * Created by Joseph White on 13-Aug-2015
 *
 * @since 1.0
 */
public class WebApiKeys {
    // Class fields
    private static final String LOG_TAG = WebApiKeys.class.getSimpleName();
    /**
     * Personal API Key for TMDB
     */
    private static final String TMDB_API_KEY_NAME = "api_key";

    // Object Fields
    public static String getTMDBApiKey() {
        return TMDB_API_KEY_NAME;
    }
}
