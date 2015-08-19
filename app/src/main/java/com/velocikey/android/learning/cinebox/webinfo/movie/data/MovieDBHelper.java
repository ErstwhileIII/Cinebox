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
    private static final int DATABASE_VERSION = 2;
    // Object Fields
    //TODO needed?
    private final Context mContext;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(LOG_TAG, "constructor");
        mContext = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO use utility method to create the table statement
        final String CreateMovieinfoTable =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COL_title + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COL_releaseDate + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COL_popularity + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COL_rating + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COL_posterPath + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COL_overview + " TEXT NOT NULL " +
                        ");";

        Log.v(LOG_TAG, "onCreate: about to create database " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL(CreateMovieinfoTable);

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO currently just drop and recreate the database on version change
        Log.v(LOG_TAG, "onUpgrade from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
