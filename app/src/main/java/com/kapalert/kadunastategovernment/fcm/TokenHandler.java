package com.kapalert.kadunastategovernment.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.LoginActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.Utils;

/**
 * Created by win10 on 9/5/2017.
 */

public class TokenHandler extends FirebaseInstanceIdService {

    private static final String TAG = "Token Tag";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Constants.setUser(this, null);
        Utils.intentTo(this, LoginActivity.class);
    }

}
