package com.collegeninja.college.activity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
