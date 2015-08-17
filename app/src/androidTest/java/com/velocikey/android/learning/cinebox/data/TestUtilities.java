package com.velocikey.android.learning.cinebox.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.velocikey.android.learning.cinebox.webinfo.movie.MovieInfo;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */
public class TestUtilities extends AndroidTestCase {
    // Class fields
    private static final String LOG_TAG = TestUtilities.class.getSimpleName();
    // Object Fields


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned" + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, index == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value " + entry.getValue().toString() + "' did not match expected value" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(index));
        }
    }

    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        ArrayList<MovieInfo> debugMovieInfo = DebugMovieInfo.getMovieInfoList();
        MovieInfo movieInfo = debugMovieInfo.get(0);
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
