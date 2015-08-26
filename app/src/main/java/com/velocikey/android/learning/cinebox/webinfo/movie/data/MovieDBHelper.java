package com.velocikey.android.learning.cinebox.webinfo.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movieinfo.db";
    // Class fields
    private static final String LOG_TAG = MovieDBHelper.class.getSimpleName();
    // Database information
    private static final int DATABASE_VERSION = 1;
    private static final String CreateMovieinfoTable =
            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                    MovieContract.MovieEntry.COL_id + " INTEGER PRIMARY KEY, " +
                    MovieContract.MovieEntry.COL_title + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COL_releaseDate + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COL_popularity + " REAL NOT NULL, " +
                    MovieContract.MovieEntry.COL_rating + " REAL NOT NULL, " +
                    MovieContract.MovieEntry.COL_posterPath + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COL_overview + " TEXT NOT NULL " +
                    ");";
    // Object Fields
    //TODO needed?
    private final Context mContext;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(LOG_TAG, "-->Constructor");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO use utility method to create the table statement

        Log.v(LOG_TAG, "-->onCreate: ");
        db.execSQL(CreateMovieinfoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO currently just drop and recreate the database on version change
        Log.v(LOG_TAG, "-->onUpgrade from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO currently just drop and recreate the database on version change
        Log.v(LOG_TAG, "-->onDowngrade from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
