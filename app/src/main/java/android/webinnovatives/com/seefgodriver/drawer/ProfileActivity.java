package android.webinnovatives.com.seefgodriver.drawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webinnovatives.com.seefgodriver.R;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;
import android.webinnovatives.com.seefgodriver.interfaces.EditButtonListener;
import android.webinnovatives.com.seefgodriver.models.Driver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;


public class ProfileActivity extends AppCompatActivity implements EditButtonListener {

    EditButtonListener editButtonListener;
    EditText nameET, passwordET, emailET;
    ImageView edit;
    FrameLayout submit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editButtonListener = this;
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


    }

    @Override
    public void onEditButtonPressed() {
        nameET.setEnabled(true);
        passwordET.setEnabled(true);
        emailET.setEnabled(true);
        submit_btn.setVisibility(View.VISIBLE);
    }

}
