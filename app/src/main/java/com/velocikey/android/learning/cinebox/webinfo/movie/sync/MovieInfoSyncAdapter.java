package com.velocikey.android.learning.cinebox.webinfo.movie.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.velocikey.android.learning.cinebox.R;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieInfo;
import com.velocikey.android.learning.cinebox.webinfo.movie.WebApiTMDB;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieDBUtility;

import java.util.ArrayList;

/**
 * Created by Joseph White on 16-Aug-2015
 */
public class MovieInfoSyncAdapter extends AbstractThreadedSyncAdapter {
    //TODO is public really needed?
    //Synchronization interval, in seconds (i.e. once a day)
    public static final long SYNC_INTERVAL = 24 * 60 * 60;
    public static final long SYNC_FLEXTIME = SYNC_INTERVAL / 6;
    // Class fields
    private static final String LOG_TAG = MovieInfoSyncAdapter.class.getSimpleName();

    // Object fields

    /**
     * Creates an {@link AbstractThreadedSyncAdapter}.
     *
     * @param context        the {@link Context} that this is running within.
     * @param autoInitialize if true then sync requests that have
     *                       {@link ContentResolver#SYNC_EXTRAS_INITIALIZE} set will be internally handled by
     *                       {@link AbstractThreadedSyncAdapter} by calling
     *                       {@link ContentResolver#setIsSyncable(Account, String, int)} with 1 if it
     */
    public MovieInfoSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, long syncInterval, long flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieInfoSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null. Invocations
     * of this method are guaranteed to be serialized.
     *
     * @param account    the account that should be synced
     * @param extras     SyncAdapter-specific parameters
     * @param authority  the authority of this sync request
     * @param provider   a ContentProviderClient that points to the ContentProvider for this
     *                   authority
     * @param syncResult SyncAdapter-specific parameters
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String sortByValue = "popularity.desc";
        Log.d(LOG_TAG, "onPerformSync");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String movieOrder = preferences.getString(getContext().getString(R.string.pref_movie_order_key),
                getContext().getString(R.string.pref_movie_order_defaultvalue));
        Log.v(LOG_TAG, "Requested movie order is: " + movieOrder);

        WebApiTMDB tmdb = new WebApiTMDB();
        Log.v(LOG_TAG, "about to get movies");


        //TODO handle these strings in preferences
        if (movieOrder.equalsIgnoreCase("popularity")) {
            sortByValue = "popularity.desc";
        } else if (movieOrder.equalsIgnoreCase("vote_average")) {
            sortByValue = "vote_average.desc";
        } else if (movieOrder.equalsIgnoreCase("release_date")) {
            sortByValue = "release_date.desc";
        }

        int pagesToGet = 2;
        int currentPage = 0;
        ArrayList<MovieInfo> result = tmdb.getMovieInfo(sortByValue, currentPage, pagesToGet);
        currentPage = currentPage + pagesToGet;
        Log.v(LOG_TAG, "back from movie request");
        //TODO consider putting into database here?

        // put MovieInfo elements into ContentValues

        //TODO extend provider to have a "delete all"
        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);

        ContentValues[] movieInformation = new ContentValues[result.size()];
        for (int i = 0; i < result.size(); i++) {
            movieInformation[i] = MovieDBUtility.getMovieValues(result.get(i));
        }
        getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieInformation);
    }

}
