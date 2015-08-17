package com.velocikey.android.learning.cinebox.webinfo.movie.data;

import android.content.ContentValues;

import com.velocikey.android.learning.cinebox.webinfo.movie.MovieInfo;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */
public class MovieDBUtility {
    // Class fields
    private static final String LOG_TAG = MovieDBUtility.class.getSimpleName();
    // Object Fields

    /**
     * Utility to help create the ContentValues to insert movie information into the database
     *
     * @param movieInfo the movie entry from which to create ContentValues
     * @return all movie information in a ContentValues object
     */
    public static ContentValues getMovieValues(MovieInfo movieInfo) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry._ID, movieInfo.getMovieId());
        movieValues.put(MovieContract.MovieEntry.COL_title, movieInfo.getTitle());
        movieValues.put(MovieContract.MovieEntry.COL_releaseDate, movieInfo.getReleaseDate());
        movieValues.put(MovieContract.MovieEntry.COL_popularity, movieInfo.getPopularity());
        movieValues.put(MovieContract.MovieEntry.COL_rating, movieInfo.getRating());
        movieValues.put(MovieContract.MovieEntry.COL_posterPath, movieInfo.getPosterPath());
        movieValues.put(MovieContract.MovieEntry.COL_overview, movieInfo.getOverview());
        return movieValues;
    }
}
