package android.webinnovatives.com.seefgodriver.auth;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webinnovatives.com.seefgodriver.R;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

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
                    Toast.makeText(DetailsActivity.this, "Request to be send on server", Toast.LENGTH_SHORT).show();
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
            }
        });

    }

    private boolean validateFields() {
        return !cnicVAR.isEmpty() && !vehicleRegistrationVAR.isEmpty() && !vehicleTypeVAR.isEmpty() && !licenseNoVAR.isEmpty();
    }

    private void setFields() {
        cnicVAR = cnicET.getText().toString();
        //vehicleTypeVAR = vehicleTypeET.getText().toString();
        vehicleRegistrationVAR = vehicleRegistrationET.getText().toString();
        licenseNoVAR = licenseNoET.getText().toString();
    }
}
