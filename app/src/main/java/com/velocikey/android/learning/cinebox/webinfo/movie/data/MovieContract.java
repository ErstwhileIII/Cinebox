package com.velocikey.android.learning.cinebox.webinfo.movie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */
public class MovieContract {
    // Information needed for corresponding Provider
    //TODO how to put authority into string reference a\
    public static final String CONTENT_AUTHORITY = "com.velocikey.android.learning.cinebox.app";
    public static final String PATH_MOVIE = "movie";
    // Class fields
    private static final String LOG_TAG = MovieContract.class.getSimpleName();
    private static final Uri CONTENT_URI_BASE = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {
        // information for Provider to use
        public static final Uri CONTENT_URI =
                CONTENT_URI_BASE.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Database information for Movie information table and column definition
        public static final String TABLE_NAME = "movie_info";
        //Remember _ID column defined in BaseColumns
        public static final String COL_id = BaseColumns._ID;
        public static final String COL_title = "title";
        public static final String COL_releaseDate = "release_date";
        public static final String COL_popularity = "popularity";
        public static final String COL_rating = "rating";
        public static final String COL_posterPath = "poster_path";
        public static final String COL_overview = "overview";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    //TODO add methods that extract information from the URIs
}
