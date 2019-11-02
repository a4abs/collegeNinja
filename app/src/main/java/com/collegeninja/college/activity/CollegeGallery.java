package com.collegeninja.college.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
