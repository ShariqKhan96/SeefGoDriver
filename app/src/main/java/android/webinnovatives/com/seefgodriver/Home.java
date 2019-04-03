package android.webinnovatives.com.seefgodriver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.webinnovatives.com.seefgodriver.drawer.NotificationsActivity;
import android.webinnovatives.com.seefgodriver.drawer.ProfileActivity;
import android.webinnovatives.com.seefgodriver.drawer.TaskActivity;
import android.webinnovatives.com.seefgodriver.services.LocationService;
import android.widget.TextView;
import android.widget.Toast;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final int REQUEST = 100;
    private GoogleMap mMap;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        name.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.NAME, "Shariq Khan"));
        email.setText(getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.EMAIL, "Shariqmack@gmail.com"));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //checkForPermissions();
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
            Common.resetPrefs(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_notificaitons) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
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
                    LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

                }
            }, 1500);
        } else {
            LatLng sydney = new LatLng(ConstantManager.CURRENT_LATLNG.latitude, ConstantManager.CURRENT_LATLNG.longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("You"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        }
    }

}
