package com.mindzglobal.www.mindz.fragments;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.mindzglobal.www.mindz.R;


public class VideoPostActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    long totalSize = 0;


    private ImageView imageback, imagerUplaod;
    private VideoView videoView;
    private Spinner videoCategory;
    private EditText edtDesc;
    private float lastAngle = 0;
    boolean is_animated = false;
//    private ArrayList<CategoryBean> categoryList;
    String filePath = "", actual_path = "";
    public int vdHeight = 0, vdWidth = 0;
    private VideoDataSender1 dataSedataSender;
//    private CategorySpinnerAdapter spinnerAdapter;
    private FrameLayout framCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_post_activity);
        dataSedataSender = new VideoDataSender1();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        imageback = (ImageView) findViewById(R.id.imageback);
        imagerUplaod = (ImageView) findViewById(R.id.imagerUplaod);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoCategory = (Spinner) findViewById(R.id.spinnerCategory);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        framCategory = (FrameLayout) findViewById(R.id.framCategory);

//        categoryList = new ArrayList<>();


        imagerUplaod.setOnClickListener(this);
        imageback.setOnClickListener(this);


        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            filePath = extraIntent.getStringExtra(VideoTrimmerActivity.TRIM_VIDEO_PATH);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            vdWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            vdHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            int duration = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();
            dataSedataSender.setVideoHeight(vdHeight);
            dataSedataSender.setVideoWidth(vdWidth);
            dataSedataSender.setVideoPath(filePath);
            dataSedataSender.setVideo_duration(duration);
        }

        if (extraIntent != null) {
            // this is only for when we want to goback, than need to send path for Trimview
            actual_path = extraIntent.getStringExtra("actual_path");
        }

        MediaController myMediaController = new MediaController(VideoPostActivity.this);
        videoView.setMediaController(myMediaController);

        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();


        edtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 10) {
                    lastAngle = -90f;
                    is_animated = true;
                    fulfillRequirment(lastAngle);
                } else if (lastAngle == -90f) {
                    lastAngle = 0f;
                    fulfillRequirment(lastAngle);
                    is_animated = false;
                }
            }
        });

        videoCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i % 2 == 0) {
                    framCategory.setBackgroundColor(getResources().getColor(R.color.light_gray));
                } else {
                    framCategory.setBackgroundColor(getResources().getColor(R.color.white_12));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        getAllCategories();
    }

  /*  private void getAllCategories() {
        AsyncWorker mWorker = new AsyncWorker(this);
        mWorker.delegate = this;
        mWorker.delegate = VideoPostActivity.this;
        JSONObject BroadcastObject = new JSONObject();
        mWorker.execute(ServerConnector.REQUEST_GET_ALL_CATEGORIES, BroadcastObject.toString(), RequestConstants.GET_REQUEST, RequestConstants.HEADER_YES, RequestConstants.REQUEST_GET_CATEGORIES);
    }

    private void addYouthTubePost() {
        AsyncWorker mWorker = new AsyncWorker(this);
        mWorker.delegate = this;
        mWorker.delegate = VideoPostActivity.this;
        JSONObject BroadcastObject = new JSONObject();
        JSONObject imagejson = new JSONObject();

        //imagejson.put("url",)

        //mWorker.execute(ServerConnector.REQUEST_ADD_POST, BroadcastObject.toString(), RequestConstants.POST_REQUEST, RequestConstants.HEADER_YES, RequestConstants.REQUEST_GET_CATEGORIES);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageback:
                goback();
                break;
          /*  case R.id.imagerUplaod:
                String desc = edtDesc.getText().toString();
                int pos = videoCategory.getSelectedItemPosition();
                int category_id = categoryList.get(pos).get_id();
                if (validation(category_id + "", desc)) {
                    dataSedataSender.setVideoCategoryId(category_id + "");
                    dataSedataSender.setIsCall(true);
                    dataSedataSender.setVideoDesc(desc);
                    finish();
                }
                break;*/
        }
    }

    private boolean validation(String category_id, String desc) {
        boolean flag = false;
        String message="";
        if (category_id == null | category_id.equalsIgnoreCase("")) {
            message = "Please select category";
            flag = false;
        } else {
            flag = true;
            /*if (!category_id.equalsIgnoreCase("") && !desc.equalsIgnoreCase("")) {
                flag = true;
            }*/
        }
       /* if (desc == null || desc.equalsIgnoreCase("")) {
            message = "Please enter desc";
            flag = false;
        }*/
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        return flag;
    }

    private void goback() {
        Intent goback = new Intent(this, VideoTrimmerActivity.class);
        goback.putExtra(VideoGallery.EXTRA_VIDEO_PATH, actual_path);
        goback.putExtra("Video_pick","0");
        startActivity(goback);
        finish();
    }

    private void fulfillRequirment(float angle) {
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(imagerUplaod).rotation(angle).withLayer().setDuration(600).setInterpolator(interpolator).start();
    }



    @Override
    public void onBackPressed() {
        goback();
    }
}
