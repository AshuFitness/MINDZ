package com.mindzglobal.www.mindz.fragments;



public class VideoDataSender {

    private static boolean isCall = false;
    private static String videoPath = "";
    private static String videoCategoryId = "";
    private static String videoDesc = "";

    public static String getVideoView_to() {
        return videoView_to;
    }

    public static void setVideoView_to(String videoView_to) {
        VideoDataSender.videoView_to = videoView_to;
    }

    private static String videoView_to = "";
    private static int videoHeight=0,videoWidth=0,video_duration=0;

    public static int getVideo_duration() {
        return video_duration;
    }

    public static void setVideo_duration(int video_duration) {
        VideoDataSender.video_duration = video_duration;
    }

    public static boolean isCall() {
        return isCall;
    }

    public static void setIsCall(boolean isCall) {
        VideoDataSender.isCall = isCall;
    }

    public static String getVideoPath() {
        return videoPath;
    }

    public static void setVideoPath(String videoPath) {
        VideoDataSender.videoPath = videoPath;
    }

    public static String getVideoCategoryId() {
        return videoCategoryId;
    }

    public static void setVideoCategoryId(String videoCategoryId) {
        VideoDataSender.videoCategoryId = videoCategoryId;
    }

    public static String getVideoDesc() {
        return videoDesc;
    }

    public static void setVideoDesc(String videoDesc) {
        VideoDataSender.videoDesc = videoDesc;
    }

    public static int getVideoHeight() {
        return videoHeight;
    }

    public static void setVideoHeight(int videoHeight) {
        VideoDataSender.videoHeight = videoHeight;
    }

    public static int getVideoWidth() {
        return videoWidth;
    }

    public static void setVideoWidth(int videoWidth) {
        VideoDataSender.videoWidth = videoWidth;
    }
}
