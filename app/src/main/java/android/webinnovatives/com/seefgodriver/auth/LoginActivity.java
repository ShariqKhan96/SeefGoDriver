package android.webinnovatives.com.seefgodriver.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.Common;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    FrameLayout loginBT;
    TextView noAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
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
                            Common.savePrefs(emailET.getText().toString(), passwordET.getText().toString(), LoginActivity.this);
                            startActivity(new Intent(LoginActivity.this, Home.class));
                            finish();
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
        noAccountTV = findViewById(R.id.no_account);
    }
}
