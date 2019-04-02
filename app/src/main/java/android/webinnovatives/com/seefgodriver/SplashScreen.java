package android.webinnovatives.com.seefgodriver;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webinnovatives.com.seefgodriver.auth.LoginActivity;
import android.webinnovatives.com.seefgodriver.common.ConstantManager;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSharedPreferences(ConstantManager.SHARED_PREFERENCES, MODE_PRIVATE).getString(ConstantManager.EMAIL, "null")
                        .equals("null"))
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                else startActivity(new Intent(SplashScreen.this, Home.class));

                finish();
            }
        }, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
