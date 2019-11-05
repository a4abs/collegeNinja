package com.collegeninja.college.model;

public class Videos {
    private int id;
    private String video, videoPath, youtubeURL;

    public Videos(){

    }

    public Videos(int id,  String video, String videoPath,String youtubeURL){

        this.id = id;
        this.video = video;
        this.videoPath = videoPath;
        this.youtubeURL = youtubeURL;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }
}
