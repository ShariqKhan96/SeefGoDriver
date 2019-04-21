package android.webinnovatives.com.seefgodriver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webinnovatives.com.seefgodriver.auth.LoginActivity;
import android.webinnovatives.com.seefgodriver.common.Common;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.drawer.HelpActivity;
import android.webinnovatives.com.seefgodriver.drawer.NotificationsActivity;
import android.webinnovatives.com.seefgodriver.drawer.OpportunitiesActivity;
import android.webinnovatives.com.seefgodriver.drawer.ProfileActivity;
import android.webinnovatives.com.seefgodriver.drawer.TaskActivity;
import android.webinnovatives.com.seefgodriver.interfaces.ProfileCredentialsChangedListener;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.models.Vehicle;
import android.webinnovatives.com.seefgodriver.models.Warehouse;
import android.webinnovatives.com.seefgodriver.network.VolleySingleton;
import android.webinnovatives.com.seefgodriver.services.LocationService;
import android.webinnovatives.com.seefgodriver.services.LocationUpdateProvider;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ProfileCredentialsChangedListener {

    private static final int REQUEST = 100;
    private GoogleMap mMap;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private TextView name;
    Driver driver;
    Switch aSwitch;
    TextView userStatusTV;
    private TextView email;
    public static ProfileCredentialsChangedListener profileCredentialsChangedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        driver = Paper.book().read(ConstantManager.CURRENT_USER);
        ;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        aSwitch = findViewById(R.id.user_status);
        userStatusTV = findViewById(R.id.status_TV);
        profileCredentialsChangedListener = this;

        updateTokenToServer(FirebaseInstanceId.getInstance().getInstanceId());


        boolean switchStatus = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getBoolean("status", true);
        if (switchStatus) {
            aSwitch.setChecked(true);
            userStatusTV.setText("Online");
        } else {
            userStatusTV.setText("Offline");
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    updateStatusToServer(1);
                    getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).edit().putBoolean("status", true).apply();
                    userStatusTV.setText("Online");
                } else {
                    updateStatusToServer(0);
                    getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).edit().putBoolean("status", false).apply();
                    userStatusTV.setText("Offline");
                }
            }
        });

        name.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.NAME, "Shariq Khan"));
        email.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.EMAIL, "Shariqmack@gmail.com"));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // checkForPermissions();

        switchServices();
    }

    private void switchServices() {
        stopService(new Intent(this, LocationService.class));
        startService(new Intent(this, LocationUpdateProvider.class));
    }

    private void updateTokenToServer(Task<InstanceIdResult> instanceIdResultTask) {
        instanceIdResultTask.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    final String token = task.getResult().getToken();

                    Log.e("TOKEN:", task.getResult().getToken() + " to be updated!");
                    StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "drivertoken.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("RESPONSE :", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Home.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", driver.getDriver_id());
                            map.put("token", token);
                            return map;
                        }
                    };
                    Volley.newRequestQueue(Home.this).add(request);
                } else {
                    Log.e("TOKENFailed :", task.getException().getMessage());
                }
            }
        });


    }

    private void updateStatusToServer(final int status) {

        final String id = driver.getDriver_id();

        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "driverstatus.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE :", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR :", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status", status + "");
                map.put("id", id);
                return map;
            }
        };
        Volley.newRequestQueue(Home.this).add(request);
    }

    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "Permission already Granted", Toast.LENGTH_SHORT).show();
            displayLocationSettingsRequest(this);
            Log.e("checkForPermissions", "Here");

            return;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                displayLocationSettingsRequest(this);
                Log.e("onPermissionsResult", "Here");
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("Here Success", "All location settings are satisfied.");
                        Intent intent = new Intent(Home.this, LocationService.class);
                        startService(intent);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(Home.class.getSimpleName(), "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(Home.class.getSimpleName(), "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(Home.class.getSimpleName(), "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS) {
            Toast.makeText(this, "Location on", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Common.resetPrefs(Home.this);
                    Intent intent = new Intent(Home.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ;
                }
            });
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_packages) {
            // Handle the camera action
            Intent intent = new Intent(this, OpportunitiesActivity.class);
            startActivity(intent);



        } else if (id == R.id.nav_notificaitons) {
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_earning) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        callService();

        // Add a marker in Sydney and move the camera


        if (Common.isConnected(this)) {
            if (ConstantManager.CURRENT_LATLNG == null) {
                final ProgressDialog dialog = new ProgressDialog(Home.this);
                dialog.setTitle("Please Wait");
                dialog.setMessage("Getting your location");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if (ConstantManager.CURRENT_LATLNG == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setTitle("Warning");
                            int permission = ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION);
                            if (permission == PackageManager.PERMISSION_GRANTED)
                                builder.setMessage("App will misbehave due to denial of requested permissions completely!");
                            else builder.setMessage("Slow internet connection");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            });

                            builder.show();

                        } else {

                            LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                        }

                    }
                }, 0);
            } else {
                LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
                mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
            }
        } else {
            Toast.makeText(this, "No internet! App won't work.", Toast.LENGTH_SHORT).show();
        }

    }

    private void callService() {
        final ProgressDialog dialog = new ProgressDialog(Home.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Getting nearby warehouses");
        dialog.setMessage("Please Wait");
        dialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "nearby.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Warehouse>>() {
                        }.getType();
                        List<Warehouse> warehouses = gson.fromJson(response, listType);


                        if (warehouses.size() != 0) {
                            for (Warehouse wareHouse : warehouses) {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(wareHouse.getWarehouse_lat()), Double.parseDouble(wareHouse.getWarehouse_long()))).title(wareHouse.getWarehouse_name())).setSnippet(wareHouse.getWarehouse_address());
                            }
                        } else {
                            Log.e("WAREHOUSES :", "SIZE 0");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(Home.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                Log.e("DRIVER_ID :", driver.getDriver_id());
                map.put("id", driver.getDriver_id());
                return map;

            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onChanged() {
        Driver driver = Paper.book().read(ConstantManager.CURRENT_USER);
        name.setText(driver.getDriver_name());
        email.setText(driver.getDriver_email());
    }
}
