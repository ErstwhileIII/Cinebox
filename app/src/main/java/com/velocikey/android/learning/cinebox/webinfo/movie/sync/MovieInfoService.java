package com.velocikey.android.learning.cinebox.webinfo.movie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MovieInfoService extends Service {
    // Class fields
    private static final String LOG_TAG = MovieInfoService.class.getSimpleName();
    private static final Object syncAdapterLock = new Object();
    // Object fields
    private static MovieInfoSyncAdapter syncAdapter = null;

    public MovieInfoService() {
    }

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "onCreate:");
        synchronized (syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new MovieInfoSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
