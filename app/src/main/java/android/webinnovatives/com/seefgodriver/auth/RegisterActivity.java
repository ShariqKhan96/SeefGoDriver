package android.webinnovatives.com.seefgodriver.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webinnovatives.com.seefgodriver.Home;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.Common;
import android.widget.EditText;
import android.widget.FrameLayout;


public class RegisterActivity extends AppCompatActivity {
    EditText emailET, passwordET, userNameET;
    FrameLayout registerBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForEmptyFields(emailET.getText().toString().trim(), passwordET.getText().toString().trim(), userNameET.getText().toString().trim())) {
                    if (checkValidEmail(emailET.getText().toString().trim())) {
                        if (checkValidPassword(passwordET.getText().toString().trim())) {
                            String email = emailET.getText().toString();
                            Log.e("Email", email);
                            //Common.savePrefs(email, passwordET.getText().toString(), userNameET.getText().toString(),RegisterActivity.this);
                            startActivity(new Intent(RegisterActivity.this, DetailsActivity.class));
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

    private void initUi() {

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        registerBT = findViewById(R.id.register_button);
        userNameET = findViewById(R.id.user_name);

    }

    private boolean checkForEmptyFields(String emailText, String passwordText, String userNameText) {
        if ((emailText.isEmpty() || emailText.equals("")) || (passwordText.isEmpty() || passwordText.equals(""))
                || (emailText.isEmpty() || userNameText.equals(""))) {
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

    private void showAlertBox(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
}
