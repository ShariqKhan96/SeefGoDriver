package android.webinnovatives.com.seefgodriver.drawer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.interfaces.EditButtonListener;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class ProfileActivity extends AppCompatActivity implements EditButtonListener {

    EditButtonListener editButtonListener;
    EditText nameET, passwordET, emailET;
    ImageView edit;
    FrameLayout submit_btn;
    Driver driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editButtonListener = this;
        driver = Paper.book().read(ConstantManager.CURRENT_USER);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameET = findViewById(R.id.user_name);
        passwordET = findViewById(R.id.password);
        emailET = findViewById(R.id.email);
        edit = toolbar.findViewById(R.id.edit);
        submit_btn = findViewById(R.id.submit_btn);
        Driver user = Paper.book().read(ConstantManager.CURRENT_USER);
        nameET.setText(user.getDriver_name());
        emailET.setText(user.getDriver_email());
        passwordET.setText("(md5)=>" + user.getDriver_password());
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "TODO Later", Toast.LENGTH_LONG).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editButtonListener.onEditButtonPressed();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this, R.style.MyAlertDialogStyle);
                dialog.setTitle("Updating Profile");
                dialog.setMessage("Please Wait");
                dialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "update.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                try {
                                    JSONObject root = new JSONObject(response);
                                    Toast.makeText(ProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (root.getString("status").equals("1")) {
                                        driver.setDriver_email(emailET.getText().toString());
                                        driver.setDriver_name(nameET.getText().toString());
                                        driver.setDriver_password(passwordET.getText().toString());

                                        Paper.book().delete(ConstantManager.CURRENT_USER);
                                        Paper.book().write(ConstantManager.CURRENT_USER, driver);
                                        Home.profileCredentialsChangedListener.onChanged();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ProfileActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", nameET.getText().toString());
                        map.put("email", emailET.getText().toString());
                        map.put("password", passwordET.getText().toString());
                        map.put("id", driver.getDriver_id());
                        map.put("check", 0 + "");
                        return map;
                    }
                };
                Volley.newRequestQueue(ProfileActivity.this).add(request);
            }
        });


    }

    @Override
    public void onEditButtonPressed() {
        nameET.setEnabled(true);
        passwordET.setEnabled(true);
        emailET.setEnabled(true);
        submit_btn.setVisibility(View.VISIBLE);
    }

}
