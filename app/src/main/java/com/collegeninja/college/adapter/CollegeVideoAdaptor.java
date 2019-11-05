package com.collegeninja.college.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collegeninja.college.model.Videos;
import com.fdscollege.college.R;

import java.util.List;

public class CollegeVideoAdaptor extends RecyclerView.Adapter<CollegeVideoAdaptor.myVideoView> {
    private List<Videos> videoList;
    private Activity mActivity;
    private Context mContext;

    public CollegeVideoAdaptor(Activity activity, List<Videos> videoList,  Context context){
        mActivity = activity;
        this.videoList = videoList;
        mContext = context;
    }

    @NonNull
    @Override
    public myVideoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_thumbnail, parent, false);

        return new myVideoView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final myVideoView holder, int position) {
        String videoURL = videoList.get(position).getVideoPath();
        String videoYouTubeURL = videoList.get(position).getYoutubeURL();
        holder.videoWebView.getSettings().setJavaScriptEnabled(true);
        holder.videoWebView.loadUrl(videoYouTubeURL);
        holder.videoWebView.setWebChromeClient(new WebChromeClient());

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class myVideoView extends RecyclerView.ViewHolder {
        VideoView videoViewCollegeVideo;
        TextView tvPlaceholder;
        WebView videoWebView;
        public myVideoView(@NonNull View itemView) {
            super(itemView);
            videoViewCollegeVideo = itemView.findViewById(R.id.college_video);
            videoWebView = itemView.findViewById(R.id.video_webview);

        }
    }
}
