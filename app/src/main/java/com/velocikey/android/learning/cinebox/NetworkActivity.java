package com.velocikey.android.learning.cinebox;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by Joseph White on 12-Aug-2015
 * Currently a stub activity to handle work done that requires network activity (and that may
 * be controlled whether the network is available via wi-fi or mobile data network access).
 *
 * @since 1.0
 */
public class NetworkActivity extends Activity {
    // Class fields
    private static final String LOG_TAG = NetworkActivity.class.getSimpleName();
    // Object Fields
    private boolean refreshDisplay = true;
    private SharedPreferences sharedPreferences;
    private boolean connectedWiFi;
    private boolean connectedMobile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //TODO expand when handling network controlled activity
//        receiver = new NetworkReceiver();
//        this.registerReceiver(receiver, filter);
    }
    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateConnectedFlags();
        //TODO laod page if need to refresh?
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (receiver != null) {
//            this.unregisterReceiver(receiver);
//        }
    }

    /**
     * Set the connected flags
     */
    private void updateConnectedFlags() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            connectedWiFi = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            connectedMobile = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            connectedWiFi = false;
            connectedMobile = false;
        }
    }

    public boolean isConnected() {
        //TODO expand to handle connectivity only over particular connection (e.g. WiFi only)
        return connectedWiFi || connectedMobile;
    }

    public boolean isConnectedWiFi() { return connectedWiFi;}
    public boolean isConnectedMobile() { return connectedMobile;}

    public void setRefreshDisplay(boolean refreshDisplay) {
        this.refreshDisplay = refreshDisplay;
    }
    public boolean isRefreshDisplay() {
        return refreshDisplay;
    }
}
