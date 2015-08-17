package com.velocikey.android.learning.cinebox.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieDBHelper;

import java.util.HashSet;

/**
 * Created by Joseph White on 14-Aug-2015
 *
 * @since 1.0
 */
public class TestDB extends AndroidTestCase {
    // Class fields
    private static final String LOG_TAG = TestDB.class.getSimpleName();

    public void setUp() {
        deleteTheDatabase();
    }

    // Object Fields
    //TODO move private methods down
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNames = new HashSet<>();
        tableNames.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: database has not been created properly.", c.moveToFirst());

        do {
            tableNames.remove(c.getString(0));
        } while (c.moveToNext());
        c.close();

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Errr: unable to query database for table information", c.moveToFirst());

        final HashSet<String> columns = new HashSet<>();
        columns.add(MovieContract.MovieEntry._ID);
        columns.add(MovieContract.MovieEntry.COL_title);
        columns.add(MovieContract.MovieEntry.COL_releaseDate);
        columns.add(MovieContract.MovieEntry.COL_popularity);
        columns.add(MovieContract.MovieEntry.COL_rating);
        columns.add(MovieContract.MovieEntry.COL_posterPath);
        columns.add(MovieContract.MovieEntry.COL_overview);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columns.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: not all column entries present", columns.isEmpty());
        c.close();
    }

    public void testMovieTable() {
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createMovieValues();

        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);
        assertTrue(movieRowId != -1);

        //Now retrieve the row and check
        Cursor movieCursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue("Error: no records returned", movieCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("testInsertReadDB movienEntry failed to validate",
                movieCursor, movieValues);

        assertFalse("Error: ore than one record returned", movieCursor.moveToNext());
        movieCursor.close();
        dbHelper.close();
    }

}
