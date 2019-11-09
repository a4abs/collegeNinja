package com.collegeninja.college.activity;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.fdscollege.college.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoActivity extends AppCompatActivity {

    VideoView videoViewCollegeVideoFullView;
    ProgressBar progressBarVideo;
    String videoURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        videoURL = getIntent().getStringExtra("videoURL");
        videoViewCollegeVideoFullView = findViewById(R.id.college_video_full_View);
        progressBarVideo = findViewById(R.id.video_progressbar);
        videoViewCollegeVideoFullView.setVideoURI(Uri.parse(videoURL));
        MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoViewCollegeVideoFullView);
        videoViewCollegeVideoFullView.setMediaController(mediacontroller);
        videoViewCollegeVideoFullView.requestFocus();
        videoViewCollegeVideoFullView.start();
        progressBarVideo.setVisibility(View.VISIBLE);

        videoViewCollegeVideoFullView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaPlayer.start();
                        progressBarVideo.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
