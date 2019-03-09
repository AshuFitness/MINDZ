package com.mindzglobal.www.mindz.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.CropMain.CropMainActivity;
import com.mindzglobal.www.mindz.LocationAccessUtils.PermissionUtils;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyTextVieww;
import de.hdodenhof.circleimageview.CircleImageView;

public class SingleImgPostActivity extends AppCompatActivity {


    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;

    CircleImageView mProfile_pic_img;

    Uri mImageCaptureUri;
    private File outPutFile = null;
    Bitmap photo;

    JSONObject jsonObject;

    SessionManager manager;
    String token,user_pic;

    RetrofitClient retrofitClient;

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    double latitude;
    double longitude;
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;


    @Bind(R.id.post_btn)
    MyTextVieww mPostBtn;

    @Bind(R.id.add_img)
    MyTextVieww mAddImage;

    @Bind(R.id.postImageView)
    ImageView postImageView;


    @Bind(R.id.status_post_text)
    EditText mStatus_post_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_img_post);

        ButterKnife.bind(this);

        manager = new SessionManager();
        token = manager.getPreferences(SingleImgPostActivity.this, Constants.USER_TOKEN);
        user_pic = manager.getPreferences(SingleImgPostActivity.this, Constants.USER_PROFILE_PIC);
        retrofitClient = new RetrofitClient(token);
        mProfile_pic_img = findViewById(R.id.profile_pic_img);

        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(SingleImgPostActivity.this).load(Config.BASE_URL_MEDIA + user_pic)
                                .centerCrop()
                                .into(mProfile_pic_img);
                    }
                });
            }
        }).start();



    }
}
