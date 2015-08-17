package com.velocikey.android.learning.cinebox.webinfo.movie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    // Class fields
    private static final String LOG_TAG = AuthenticatorService.class.getSimpleName();
    // Object fields
    AccountAuthenticator mAccountAuthenticator;

    public AuthenticatorService() {
    }

    /**
     * Establish the AccountAuthenticator for this service
     */
    @Override
    public void onCreate() {
        mAccountAuthenticator = new AccountAuthenticator(this);
    }

    /**
     * WHen the system binds to this service (to make the remote procedure call) return the
     * authenticarot's IBinder
     *
     * @param intent the intent used to launch the service.
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAccountAuthenticator.getIBinder();

    }
}
