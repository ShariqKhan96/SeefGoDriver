package android.webinnovatives.com.seefgodriver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webinnovatives.com.seefgodriver.App;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hp on 4/9/2019.
 */

public class FBMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN :", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(FBMessagingService.class.getSimpleName(), "onMessageReceived");
        Map<String, String> data = remoteMessage.getData();

        Log.e("messageBack", remoteMessage.getData().get("title") + " " + remoteMessage.getData().get("message"));
        String message = data.get("message");
        String title = data.get("title");
        if (message != null || title != null)
            showNotification(message, title);
//        else
//            showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }

    public void showNotification(String message, String title) {
        Intent dIntent;
        PendingIntent dismissIntent = null;
//        String[] title_action = title.split(":");
//        Log.e("TitleHash", title_action[0] + title_action[1]);
        long pattern[] = {1000, 1000};

        Log.e("title", title);

        dIntent = new Intent(this, Home.class);
        dIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        dismissIntent = PendingIntent.getActivity(this, 999, dIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        switch (title_action[1]) {
//            case "Home":
//                dIntent = new Intent(this, Home.class);
//                dIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                dismissIntent = PendingIntent.getActivity(this, 999, dIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                break;
//            case "Roster":
//                dIntent = new Intent(this, NotificationToRosterReceiver.class);
////                dIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                dIntent.putExtra("launch_main", "true");
//                dismissIntent = PendingIntent.getBroadcast(this, 999, dIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        }

        Notification builder = new NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.truck_logo)
                .setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(dismissIntent)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NotificationID.getID(), builder);


    }
}

class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }
}
