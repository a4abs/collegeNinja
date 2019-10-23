package com.collegeninja.college.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.collegeninja.college.adapter.IntroAdapter;
import com.fdscollege.college.R;

public class IntroActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mViewPager = (ViewPager) findViewById(R.id.intro_viewpager);

        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));




    }
}
