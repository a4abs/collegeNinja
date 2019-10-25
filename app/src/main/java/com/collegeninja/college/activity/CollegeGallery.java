package com.collegeninja.college.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.collegeninja.college.adapter.FullScreenImageAdapter;
import com.fdscollege.college.R;

import java.util.ArrayList;

public class CollegeGallery extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_college_gallery);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        ArrayList imageUrl = i.getStringArrayListExtra("arrayList");
        viewPager = findViewById(R.id.gallery_pager);
        adapter = new FullScreenImageAdapter(this,imageUrl);
        viewPager.setAdapter(adapter);

    }

}
