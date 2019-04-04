package android.webinnovatives.com.seefgodriver.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    EditText cnicET, vehicleTypeET, vehicleRegistrationET, licenseNoET;
    FrameLayout submitBtn;
    String cnicVAR, vehicleTypeVAR, vehicleRegistrationVAR, licenseNoVAR;
    String vehicleTypes[] = {"Truckx1", "Truckx2", "Truckx3", "Truckx4"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cnicET = findViewById(R.id.cnic);
        vehicleTypeET = findViewById(R.id.vehicle_type);
        vehicleRegistrationET = findViewById(R.id.vehicle_registration);
        licenseNoET = findViewById(R.id.license_no);
        submitBtn = findViewById(
                R.id.submit_button
        );
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields();
                if (validateFields()) {
                    callService();

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
                    }
                });

                builder.show();
            }
        });

    }

    private void callService() {

        StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "driverregistration.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                return map;
            }
        };

        //Toast.makeText(DetailsActivity.this, "Request to be send on server", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailsActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateFields() {
        return !cnicVAR.isEmpty() && !vehicleRegistrationVAR.isEmpty() && !vehicleTypeVAR.isEmpty() && !licenseNoVAR.isEmpty();
    }

    private void setFields() {
        cnicVAR = cnicET.getText().toString();
        vehicleTypeVAR = vehicleTypeET.getText().toString();
        vehicleRegistrationVAR = vehicleRegistrationET.getText().toString();
        licenseNoVAR = licenseNoET.getText().toString();
    }
}
