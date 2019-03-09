package com.mindzglobal.www.mindz.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mindzglobal.www.mindz.R;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;


public class VideoTrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnK4LVideoListener {

    private K4LVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    private final int maxVideoLength=5*60;
    public final static String TRIM_VIDEO_PATH="TRIM_VIDEO_PATH";
    String path = "";
    String flag="";
    public GalleryClickedPosition delegate=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);


        Intent extraIntent = getIntent();

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(VideoGallery.EXTRA_VIDEO_PATH);
            flag=extraIntent.getStringExtra("Video_pick");
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        mVideoTrimmer =  findViewById(R.id.timeLine);
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(maxVideoLength);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnK4LVideoListener(this);
            mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
/*
        findViewById(R.id.imageback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goback();

            }
        });*/
    }


    private void goback() {
        if(flag.equalsIgnoreCase("0")) {
            Intent goback = new Intent(VideoTrimmerActivity.this, VideoGallery.class);
            startActivity(goback);
            finish();
        }else{
            Intent goback = new Intent(VideoTrimmerActivity.this, CreatePostActivity_New.class);
            startActivity(goback);
            finish();
        }
    }

    @Override
    public void onTrimStarted() {
        try{
            //mProgressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrimmerActivity.this, "Video saved at"+ uri.getPath(), Toast.LENGTH_SHORT).show();
            }
        });
       /* Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
        finish();*/
        if(flag.equalsIgnoreCase("0")) {
            Intent gotoPostVideo = new Intent(this, VideoPostActivity.class);
            gotoPostVideo.putExtra(TRIM_VIDEO_PATH, uri.getPath());
            gotoPostVideo.putExtra("actual_path", path);
            startActivity(gotoPostVideo);
            finish();
        }else{
            Intent gotoPostVideo = new Intent(VideoTrimmerActivity.this, CreatePostActivity_New.class);
            gotoPostVideo.putExtra(TRIM_VIDEO_PATH, uri.getPath());
            gotoPostVideo.putExtra("actual_path", path);
            startActivity(gotoPostVideo);
            finish();
        }
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        goback();
    }
}
