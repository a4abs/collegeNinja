package com.collegeninja.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.fdscollege.college.R;

public class IntroTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_two);

        FrameLayout intro_one = findViewById(R.id.intro_two);
        intro_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(getBaseContext(),IntroThreeActivity.class);
//                startActivity(i);
//                finish();
            }
        });

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(5*1000);
                    Intent i=new Intent(getBaseContext(),IntroThreeActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();
    }
}
