package android.webinnovatives.com.seefgodriver.auth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.Common;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.webinnovatives.com.seefgodriver.network.VolleySingleton;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    FrameLayout loginBT;
    TextView noAccountTV;
    CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        String decider = Paper.book().read(ConstantManager.REMEMBER_ME, "null");

        if (decider.equals("true")) {
            remember_me.setChecked(true);
            Driver user = Paper.book().read(ConstantManager.USER_DECRYPTED_OBJECT);
            emailET.setText(user.getDriver_email());
            passwordET.setText(user.getDriver_password());

        } else
            remember_me.setChecked(false);

        remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!TextUtils.isEmpty(emailET.getText().toString()) && !TextUtils.isEmpty(passwordET.getText().toString())) {
                        Driver user = new Driver();
                        Paper.book().write(ConstantManager.REMEMBER_ME, "true");
                        user.setDriver_email(emailET.getText().toString());
                        user.setDriver_password(passwordET.getText().toString());

                        Paper.book().write(ConstantManager.USER_DECRYPTED_OBJECT, user);
                    } else
                        Toast.makeText(LoginActivity.this, "Field(s) Empty", Toast.LENGTH_SHORT).show();
                } else Paper.book().write(ConstantManager.REMEMBER_ME, "false");
            }
        });
        noAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForEmptyFields(emailET.getText().toString().trim(), passwordET.getText().toString().trim())) {
                    if (checkValidEmail(emailET.getText().toString().trim())) {
                        if (checkValidPassword(passwordET.getText().toString().trim())) {
//                            Common.savePrefs(emailET.getText().toString(), passwordET.getText().toString(), "Dummy", LoginActivity.this);
//                            startActivity(new Intent(LoginActivity.this, Home.class));
//                            finish();
                            callService();


                        } else {
                            showAlertBox("Password should not less than 6 letters");
                        }
                    } else {
                        showAlertBox("Invalid Email");
                    }
                } else {
                    showAlertBox("Fields Empty");


                }
            }
        });
    }

    private void callService() {


        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Authenticating");
        dialog.setMessage("Please Wait");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "driverlogin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            JSONObject root = new JSONObject(response);
                            Log.e("RESPONSE", root.toString());

                            if (root.getString("status").equals("1")) {

                                Driver user = new Gson().fromJson(root.getJSONObject("user").toString(), Driver.class);
                                Paper.book().write(ConstantManager.CURRENT_USER, user);



                                Driver mUser = new Driver();
                                mUser.setDriver_email(emailET.getText().toString());
                                mUser.setDriver_password(passwordET.getText().toString());
                                Paper.book().write(ConstantManager.USER_DECRYPTED_OBJECT, mUser);


                                Common.savePrefs(emailET.getText().toString(), passwordET.getText().toString(), user.getDriver_name(), LoginActivity.this);

                            //intent to jump from one screen to another

                                startActivity(new Intent(LoginActivity.this, Home.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "" + "No user found with this credentials!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", emailET.getText().toString());
                map.put("pass", passwordET.getText().toString());
                return map;
            }
        };
        // Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void showAlertBox(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Validation Failed");
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean checkForEmptyFields(String emailText, String passwordText) {
        if ((emailText.isEmpty() || emailText.equals("")) || (passwordText.isEmpty() || passwordText.equals(""))) {
            return false;
        }
        return true;
    }

    private boolean checkValidEmail(String emailText) {
        return (!TextUtils.isEmpty(emailText) && Patterns.EMAIL_ADDRESS.matcher(emailText).matches());
    }

    private boolean checkValidPassword(String passwordText) {
        if (TextUtils.isEmpty(passwordText) || passwordText.length() < 6)
            return false;
        return true;
    }

    private void initUi() {
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        loginBT = findViewById(R.id.login_button);
        remember_me = findViewById(R.id.remember_me);
        noAccountTV = findViewById(R.id.no_account);
    }
}
