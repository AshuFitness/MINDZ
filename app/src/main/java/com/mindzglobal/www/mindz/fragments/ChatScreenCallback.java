package com.mindzglobal.www.mindz.fragments;



public interface ChatScreenCallback {

    void isPasswordMatched(boolean isPasswordMatched);

    void gallerySelectedImage(boolean send);

    void cameraClickedImage(String imagePath);

    void audioMusicFile(String imagePath);

    void cameraClose(boolean isClicked);

}
