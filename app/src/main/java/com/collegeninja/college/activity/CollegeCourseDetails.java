package com.collegeninja.college.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.fdscollege.college.R;

public class CollegeCourseDetails extends BaseActivity {
    String strCourseId, strCollegeName, strImageUrl;
    TextView tvHeader;
    ImageView imvHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_college_course_details, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
       // setContentView(R.layout.activity_college_course_details);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
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
