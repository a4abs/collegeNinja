package com.collegeninja.college.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.fdscollege.college.R;

public class CollegeCourseDetails extends AppCompatActivity {
    String strCourseId, strCollegeName, strImageUrl;
    TextView tvHeader;
    ImageView imvHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_course_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvHeader = findViewById(R.id.header);
        imvHeaderImage = findViewById(R.id.header_image);

        strCourseId = getIntent().getStringExtra("id");
        strCollegeName = getIntent().getStringExtra("title");
        strImageUrl = getIntent().getStringExtra("image");

        tvHeader.setText(strCollegeName);

        Glide.with(getApplicationContext()).load(strImageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(imvHeaderImage);

    }
}
