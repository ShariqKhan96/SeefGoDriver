package android.webinnovatives.com.seefgodriver.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by hp on 4/9/2019.
 */

public class FBMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN :", s);
    }

}
