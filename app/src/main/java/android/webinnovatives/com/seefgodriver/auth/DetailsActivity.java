package android.webinnovatives.com.seefgodriver.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.Common;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.models.Vehicle;
import android.webinnovatives.com.seefgodriver.network.VolleySingleton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class DetailsActivity extends AppCompatActivity {

    EditText cnicET, vehicleTypeET, licenseNoET, cityNameEt;
    FrameLayout submitBtn;
    String cnicVAR, vehicleTypeVAR, licenseNoVAR;
    String vehicleTypes[];
    String vehicleIds[];
    String name, email, password, city;
    String cities[] = {"Karachi", "Lahore", "Islamabad", "Peshawar", "Multan", "Gwadar"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getVehicleType();
        cnicET = findViewById(R.id.cnic);
        cityNameEt = findViewById(R.id.city_name);

        vehicleTypeET = findViewById(R.id.vehicle_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Profile Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        licenseNoET = findViewById(R.id.license_no);
        submitBtn = findViewById(
                R.id.submit_button
        );
        name = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.NAME, "NULL");
        password = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.PASSWORD, "NULL");
        email = getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.EMAIL, "NULL");

        Log.e("email", email);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields();
                if (validateFields()) {
                    if (cnicVAR.length() == 13) {
                        if (licenseNoVAR.length() == 17) {
                            callService();
                        } else {
                            Toast.makeText(DetailsActivity.this, "Invalid License Number", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(DetailsActivity.this, "Invalid CNIC", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Some field(s) empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        vehicleTypeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Select type");
                builder.setItems(vehicleTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vehicleTypeET.setText(vehicleTypes[which]);
                        vehicleTypeVAR = vehicleIds[which];
                    }
                });

                builder.show();
            }
        });

        cityNameEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Select City");
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityNameEt.setText(cities[which]);
                        city = cities[which];

                    }
                });

                builder.show();
            }
        });


    }

    private void getVehicleType() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "vehicle.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
////                            JSONArray json = new JSONArray(response);
////                            vehicleTypes = new String[json.length()];
////                            vehicleIds = new String[json.length()];
////                            for (int i = 0; i < json.length(); i++) {
////                                JSONObject vehicle = json.getJSONObject(i);
////                                vehicleTypes[i] = vehicle.getString()
////
////                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Vehicle>>() {
                        }.getType();
                        List<Vehicle> vehicles = gson.fromJson(response, listType);

                        vehicleTypes = new String[vehicles.size()];
                        vehicleIds = new String[vehicles.size()];

                        for (int i = 0; i < vehicles.size(); i++) {
                            vehicleIds[i] = vehicles.get(i).getVehicle_id();
                            vehicleTypes[i] = vehicles.get(i).getVehicle_name() + " " + vehicles.get(i).getVehicle_size() + " meter";
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(DetailsActivity.this).addToRequestQueue(request);
    }

    private void callService() {
        final ProgressDialog dialog = new ProgressDialog(DetailsActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Registering");
        dialog.setMessage("Please Wait");
        dialog.show();
        //getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).edit().putString(ConstantManager.CNIC, cnicVAR).apply();
//        Intent intent = new Intent(DetailsActivity.this, Home.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();

        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "driverregistration.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject root = new JSONObject(response);
                            Log.e("Object", root.toString());
                            if (root.getString("status").equals("1")) {
                                //User user = new Gson().fromJson(root.getJSONObject("user").toString(), User.class);
                                //User user = new User("", )
                                Driver user = new Driver(root.getString("id"), name, password, cnicVAR, licenseNoVAR, "null", email, vehicleTypeVAR, "0");

                                Paper.book().write(ConstantManager.CURRENT_USER, user);

                                Log.e("Wrote", Paper.book().read(ConstantManager.CURRENT_USER).toString());
                                Common.savePrefs(email, password, name, DetailsActivity.this);
                                startActivity(new Intent(DetailsActivity.this, Home.class));
                                finish();
                            } else {
                                Toast.makeText(DetailsActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(DetailsActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("pass", password);
                map.put("cnic", cnicVAR);
                map.put("token", "null");
                map.put("license", licenseNoVAR);
                map.put("vid", vehicleTypeVAR);
                map.put("location", city);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 3,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(DetailsActivity.this).add(request);
        //VolleySingleton.getInstance(this).addToRequestQueue(request);

        //Toast.makeText(DetailsActivity.this, "Request to be send on server", Toast.LENGTH_SHORT).show();

    }

    private boolean validateFields() {
        return !cnicVAR.isEmpty() && !vehicleTypeVAR.isEmpty() && !licenseNoVAR.isEmpty() && !city.isEmpty();
    }

    private void setFields() {
        cnicVAR = cnicET.getText().toString();

        // vehicleTypeVAR = vehicleTypeET.getText().toString();
        licenseNoVAR = licenseNoET.getText().toString();
    }
}
