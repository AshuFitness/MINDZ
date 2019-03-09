package com.mindzglobal.www.mindz.fragments;



public class VideoDataSender1 {

    private static boolean isCall = false;
    private static String videoPath = "";
    private static String videoCategoryId = "";
    private static String videoDesc = "";
    private static int videoHeight=0,videoWidth=0,video_duration=0;

    public static int getVideo_duration() {
        return video_duration;
    }

    public static void setVideo_duration(int video_duration) {
        VideoDataSender1.video_duration = video_duration;
    }

    public static boolean isCall() {
        return isCall;
    }

    public static void setIsCall(boolean isCall) {
        VideoDataSender1.isCall = isCall;
    }

    public static String getVideoPath() {
        return videoPath;
    }

    public static void setVideoPath(String videoPath) {
        VideoDataSender1.videoPath = videoPath;
    }

    public static String getVideoCategoryId() {
        return videoCategoryId;
    }

    public static void setVideoCategoryId(String videoCategoryId) {
        VideoDataSender1.videoCategoryId = videoCategoryId;
    }

    public static String getVideoDesc() {
        return videoDesc;
    }

    public static void setVideoDesc(String videoDesc) {
        VideoDataSender1.videoDesc = videoDesc;
    }

    public static int getVideoHeight() {
        return videoHeight;
    }

    public static void setVideoHeight(int videoHeight) {
        VideoDataSender1.videoHeight = videoHeight;
    }

    public static int getVideoWidth() {
        return videoWidth;
    }

    public static void setVideoWidth(int videoWidth) {
        VideoDataSender1.videoWidth = videoWidth;
    }
}
