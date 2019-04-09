package android.webinnovatives.com.seefgodriver.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import java.util.concurrent.ThreadLocalRandom;

public class LocationService extends Service {

    private static final String TAG = "LocationService";
    LocationRequest request;
    FusedLocationProviderClient client;
    LocationCallback locationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreate");
//        String client_id = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString("client_id", "01");
//        String emp_id = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString("employee_id", "01");

        requestLocationUpdates();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        client.removeLocationUpdates(locationCallback);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void requestLocationUpdates() {
        request = new LocationRequest();
        // Random r = new Random();
        int intervalTime = ThreadLocalRandom.current().nextInt(10000, 20000);
        // int intervalTime = r.nextInt(7000 - 5000) + 5000;
        int fastestInterval = intervalTime - ThreadLocalRandom.current().nextInt(2000, 3000);
        Log.e("intervals", "fastest " + fastestInterval + " intervalTime " + intervalTime);
        request.setInterval(intervalTime);

        request.setFastestInterval(fastestInterval);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(10f);
        client = LocationServices.getFusedLocationProviderClient(this);


        int permission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    locationCallback = this;
                    Location location = locationResult.getLastLocation();
                    Log.e(TAG, (location.getProvider()));

                    if (location != null) {
                        ConstantManager.CURRENT_LATLNG = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.e(TAG, "location update " + ConstantManager.CURRENT_LATLNG.toString());

                    }
                }
            }, Looper.myLooper());
        } else {
            Toast.makeText(this, "Turn on the device location from device settings!!", Toast.LENGTH_SHORT).show();
        }


    }


}
