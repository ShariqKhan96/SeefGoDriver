package android.webinnovatives.com.seefgodriver;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.atomic.AtomicInteger;

import io.paperdb.Paper;

public class App extends Application {
    public static final String NOTIFICATION_CHANNEL_ID = "SeefGo_Ch_01";

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        long pattern[] = {1000, 1000};
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "SeefGo",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);

            //manager.createNotificationChannel(notificationChannel);
        }
    }

}

