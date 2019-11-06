package com.collegeninja.college.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fdscollege.college.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isNetworkConnected()) {
            Thread background = new Thread() {
                public void run() {
                    try {
                        sleep(5 * 1000);

                        SharedPreferences pref = getSharedPreferences("college", 0);
                        String token = pref.getString("token", "");

                        if (token.length() > 10) {
                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            //Intent i=new Intent(getBaseContext(),BaseActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(getBaseContext(), IntroActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            background.start();
        } else {
            Toast.makeText(this, "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
