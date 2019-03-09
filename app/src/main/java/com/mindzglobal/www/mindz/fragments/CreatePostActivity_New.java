package com.mindzglobal.www.mindz.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.CrimeActivity;
import com.mindzglobal.www.mindz.LocationAccessUtils.PermissionUtils;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyTextVieww;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;


public class CreatePostActivity_New extends AppCompatActivity implements GalleryClickedPosition, ChatScreenCallback, CameraInterface,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback,CategoryFragment.OnCallbackReceived {

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

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    double latitude;
    double longitude;
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;

    @Bind(R.id.at_location)
    MyTextVieww mAt_location;

    @Bind(R.id.check_in_btn)
    MyTextVieww mCheck_in_btn;

    @Bind(R.id.post_btn)
    MyTextVieww mPostBtn;

    @Bind(R.id.add_img)
    MyTextVieww mAddImage;

    @Bind(R.id.add_video)
    MyTextVieww mAddVideo;

    @Bind(R.id.tag_friends)
    MyTextVieww tag_friends;

    @Bind(R.id.choose_cat)
    MyTextVieww mChoose_cat;

    @Bind(R.id.status_post_text)
    EditText mStatus_post_text;

    @Bind(R.id.category)
    MyTextVieww mCategory;


    FrameLayout gallery;
    LinearLayout bottom;
    RelativeLayout relative;
    ArrayAdapter galleryAdapter;
    RecyclerView imageList;
    ImageView send, editImage;
    LinearLayoutManager mLayoutManager;
    SelectedGalaryImageAdapter mAdapter;
//    public PostInBuzz postImage = null;
    List<String> selectedPath;
    List<String> videoimagePath = null;
    private static final int REQUEST_PREVIEW_CODE = 1001;

    public static int sharetoselected=0;
    RelativeLayout sharebtn;
    String filePath = "", actual_path = "";
    private VideoView videoView;
    public int vdHeight = 0, vdWidth = 0, vduration = 0;
//    CreatePostJsonInputModel json = new CreatePostJsonInputModel();
    private VideoDataSender dataSedataSender=new VideoDataSender();

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
//    private Uri fileUri;
    int imageHeight=0,imageWidth=0;
    private boolean isVisible = false;
//    ArrayList<String> getMediaBase;

    String selectedPathstr;

    /*CrossRemove mRemoveCross;

    public interface CrossRemove {
        void mRemovex();

    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_keypad);
        ButterKnife.bind(this);

        imageList = findViewById(R.id.selected_imge);
        manager = new SessionManager();
        token = manager.getPreferences(CreatePostActivity_New.this, Constants.USER_TOKEN);
        user_pic = manager.getPreferences(CreatePostActivity_New.this, Constants.USER_PROFILE_PIC);
        retrofitClient = new RetrofitClient(token);
        mProfile_pic_img = findViewById(R.id.profile_pic_img);

        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(CreatePostActivity_New.this).load(Config.BASE_URL_MEDIA + user_pic)
                                .centerCrop()
                                .into(mProfile_pic_img);
                    }
                });
            }
        }).start();


        mStatus_post_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery.setVisibility(View.GONE);
            }
        });

        permissionUtils = new PermissionUtils(CreatePostActivity_New.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        jsonObject = new JSONObject();

//        getMediaBase = new ArrayList<>();

//        selectedPath = new ArrayList<>();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        LayoutInflater mInflater = LayoutInflater.from(this);

        gallery = findViewById(R.id.gallery);
        bottom = findViewById(R.id.bottom);
        relative = findViewById(R.id.relative);
        imageList = findViewById(R.id.selected_imge);


        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                bottom.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.GONE);
            }
        });

        videoView = findViewById(R.id.videoView);
        SharedPreferenceSingleton.getInstance().init(CreatePostActivity_New.this);

        imageList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageList.setLayoutManager(mLayoutManager);

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheView(0);
                hideSoftKeyboard();
            }
        });


        mChoose_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheView(1);
                hideSoftKeyboard();
            }
        });

        mAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheView(2);
                hideSoftKeyboard();
            }
        });


        tag_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheView(3);
                hideSoftKeyboard();
            }
        });


        mCheck_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

                }
            }
        });


        filePath = getIntent().getStringExtra(VideoTrimmerActivity.TRIM_VIDEO_PATH);
        actual_path = getIntent().getStringExtra("actual_path");
        if (filePath != null && actual_path != null) {
            imageList.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            vdWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            vdHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            vduration = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();

            dataSedataSender.setVideoHeight(vdHeight);
            dataSedataSender.setVideoWidth(vdWidth);
            dataSedataSender.setVideoPath(filePath);
            dataSedataSender.setVideo_duration(vduration);
            MediaController myMediaController = new MediaController(CreatePostActivity_New.this);
            videoView.setMediaController(myMediaController);

            videoView.setVideoURI(Uri.parse(filePath));
            videoView.start();
        }


        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.getVisibility() == View.VISIBLE) {
                    Log.e("images",selectedPath.toString());
                    if (selectedPath != null) {

                            uploadMultiFile();

                    }
                } else if (videoView.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(CreatePostActivity_New.this, CrimeActivity.class);
                    intent.putExtra("fragmentload", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    CreatePostActivity_New.this.finish();
                }
            }
        });


    }


  /*   for (int i = 0; i < selectedPath.size(); i++)

    {
        File file = new File(selectedPath.get(i));
        photo = decodeFile(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        selectedPath.add(encodedImage);
    }*/


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


    private void uploadMultiFile() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("Category", mCategory.getText().toString());
        builder.addFormDataPart("StatusText", mStatus_post_text.getText().toString());
        builder.addFormDataPart("CheckIn", mAt_location.getText().toString());
        builder.addFormDataPart("TagFriend", "Bangalore");

        for (int i = 0; i < selectedPath.size(); i++) {
            builder.addFormDataPart("HOMmedia[]", selectedPath.get(i));
        }

        MultipartBody requestBody = builder.build();
        RetrofitClient.getClient().uploadMultiFile(requestBody,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(imageList) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("PostRes",""+response.toString());
                Log.e("CheckPostRes",""+response.body().toString());
                Toast.makeText(CreatePostActivity_New.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



 private void getLocation() {

        if (isPermissionGranted) {

            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

        }

    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void getAddress() {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();
            String area = locationAddress.getSubLocality();

            String currentLocation = "";

//            if(!TextUtils.isEmpty(address))
//            {
//                currentLocation=address;

            if (!TextUtils.isEmpty(city)) {
                currentLocation = area + " , " + city;
            }


            mAt_location.setText(currentLocation);

        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        @SuppressLint("RestrictedApi")
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(CreatePostActivity_New.this, REQUEST_CHECK_SETTINGS);

                        }
                        catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }


    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    private void getIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;

    }



    @Override
    public void clicked(List<String> selectedImages, int requestNumber) {
        imageList.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);
        if (requestNumber == 0) {
            if (selectedImages.size() > 0) {
                selectedPath = selectedImages;
                mAdapter = new SelectedGalaryImageAdapter(this, selectedImages);
                imageList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                madapter_item_click();
            } else {
                selectedPath = null;
                mAdapter = new SelectedGalaryImageAdapter(this, selectedImages);
                imageList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                madapter_item_click();
            }
        } else {

            Intent it = new Intent(CreatePostActivity_New.this, BrowsePicture2.class);
            startActivityForResult(it, 5);
        }

    }



    private void madapter_item_click() {
        mAdapter.setOnItemClickListener(new SelectedGalaryImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,  List<String> selectedImages, int clickedPosition) {
                //selectedPath = imagePath;
                Intent intent = new Intent(CreatePostActivity_New.this, ActivityGallery_Viewpager.class);

                intent.putStringArrayListExtra(ApplicationConstants.FILTERED_IMAGE_PATH, (ArrayList<String>) selectedImages);
                intent.putExtra("position_id", clickedPosition);
//                 SharedPreferenceSingleton.getInstance().init(CreatePostActivity_New.this);
//                SharedPreferenceSingleton.getInstance().writeStringPreference(ApplicationConstants.FILTERED_IMAGE_PATH, String.valueOf(selectedPath));
                startActivityForResult(intent, 4);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {

            Uri selectedImageUri = data.getData();
            selectedPathstr = getPath(selectedImageUri);
            Intent it = new Intent(this, ActivityGallery.class);
//                mImageCaptureUri = data.getData();

            SharedPreferenceSingleton.getInstance().init(this);
            it.putExtra("camera", "camera");
            SharedPreferenceSingleton.getInstance().writeStringPreference(ApplicationConstants.CAPTURED_IMAGE_PATH, mImageCaptureUri.getPath());
            startActivityForResult(it, 4);

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();

        }  if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    // video successfully recorded
                    // preview the recorded video
                    //previewVideo();
                    break;
                case RESULT_CANCELED:
                    // user cancelled recording
                    Toast.makeText(getApplicationContext(), "User cancelled video recording", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // failed to record video
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (resultCode == 4) {
            getSupportActionBar().show();
            List<String> imagePath = new ArrayList<String>();
            imagePath = data.getStringArrayListExtra("ImagePath");
            //selectedPath=imagePath.get(0);
            selectedPath = imagePath;
            mAdapter = new SelectedGalaryImageAdapter(this, imagePath);
            imageList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            madapter_item_click();
        }
        if (resultCode == 5) {
            List<String> imagePath = data.getStringArrayListExtra("selectedImage");
            if (imagePath.size() > 0) {
                //selectedPath = imagePath.get(0);
                selectedPath = imagePath;
                mAdapter = new SelectedGalaryImageAdapter(this, imagePath);
                imageList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                madapter_item_click();
            } else {
                selectedPath = null;
                mAdapter = new SelectedGalaryImageAdapter(this, imagePath);
                imageList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                madapter_item_click();

            }


        }
    }


/*    //Load GridView
    private void loadGridView(ArrayList<String> imagesArray) {
//        GridView_Adapter adapter = new GridView_Adapter(SelectMulipleImageMainActivity.this, imagesArray, false);
//        selectedImageGridView.setAdapter(adapter);
        Log.e("imagesArray",""+imagesArray.toString());

        SelectedGalaryImageAdapter adapters = new SelectedGalaryImageAdapter(CreatePostActivity_New.this, imagesArray);
        imageList.setAdapter(adapters);
    }
    */



    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        }
        catch (FileNotFoundException e) {

        }
        return null;
    }




 public void getTheView(int i) {

     if (i == 0) {
         hideSoftKeyboard();
         changeFragment(new GalleryLoad());

     } else if (i == 1) {
         hideSoftKeyboard();
         changeFragment(new CategoryFragment());
     } else if (i == 2) {
         hideSoftKeyboard();
         changeFragment(new VideoFragment());
     }else if (i==3){
         hideSoftKeyboard();
         changeFragment(new TagFragment());
     }else {
         gallery.setVisibility(View.GONE);
         showSoftKeyboard(bottom);
     }

 }


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            gallery.setVisibility(View.VISIBLE);
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        assert inputMethodManager != null;
        inputMethodManager.showSoftInput(view, 0);
    }

    public void changeFragment(Fragment targetFragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.gallery, targetFragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();

    }

    @Override
    public void isPasswordMatched(boolean isPasswordMatched) {
    }

    @Override
    public void gallerySelectedImage(boolean send) {
    }

    @Override
    public void cameraClickedImage(String imagePath) {
    }

    @Override
    public void audioMusicFile(String imagePath) {
    }

    @Override
    public void cameraClose(boolean isClicked) {
    }

    @Override
    public void clickedPick(String Image, boolean isClosed) {

    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    public void cUpdate() {
        mCategory.setText("Crime");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void aUpdate() {
        mCategory.setText("Area Problem");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void fUpdate() {
        mCategory.setText("Fun Activity");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void oppUpdate() {
        mCategory.setText("Opportunity");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void pUpdate() {
        mCategory.setText("Political");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void sUpdate() {
        mCategory.setText("Sports");
        gallery.setVisibility(View.GONE);
    }

    @Override
    public void othUpdate() {
        mCategory.setText("Other");
        gallery.setVisibility(View.GONE);
    }
}



   /* public class HorizontalAdapter extends RecyclerView.Adapter<CreatePostActivity_New.HorizontalAdapter.MyViewHolder> {

        private Context context;
        private ArrayList<String> imageUrls;
        private SparseBooleanArray mSparseBooleanArray;//Variable to store selected Images
        private DisplayImageOptions options;
        private boolean isCustomGalleryActivity;//Variable to check if gridview is to setup for Custom Gallery or not

        public HorizontalAdapter(Context context, ArrayList<String> imageUrls, boolean isCustomGalleryActivity) {
            this.context = context;
            this.imageUrls = imageUrls;
            this.isCustomGalleryActivity = isCustomGalleryActivity;
            mSparseBooleanArray = new SparseBooleanArray();

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

//        public HorizontalAdapter(List<Data> horizontalList, Context context) {
//            this.horizontalList = horizontalList;
//            this.context = context;
//        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView txtview;
            public MyViewHolder(View view) {
                super(view);
                imageView= view.findViewById(R.id.galleryImageView);
                txtview= view.findViewById(R.id.file_name);
            }
        }

        @NonNull
        @Override
        public CreatePostActivity_New.HorizontalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card_layout, parent, false);

            return new CreatePostActivity_New.HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final CreatePostActivity_New.HorizontalAdapter.MyViewHolder holder, final int position) {

//            holder.imageView.setImageResource(imageUrls.get(position));

            ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position), holder.imageView, options);//Load Images over ImageView

            holder.txtview.setText(imageUrls.get(position));

//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//
//                public void onClick(View v) {
//                    String list = horizontalList.get(position).txt.toString();
//                    Toast.makeText(SelectMulipleImageMainActivity.this, list, Toast.LENGTH_SHORT).show();
//                }
//
//            });

        }

        @Override
        public int getItemCount()
        {
            return imageUrls.size();
        }
    }
*/



/*    send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.getVisibility() == View.VISIBLE) {
                    if (selectedPath != null || mEditEmojicon.getText().toString().length() > 0) {
                        callapiPost(selectedPath, mEditEmojicon.getText().toString());
                    }
                } else if (videoView.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(CreatePostActivity_New.this, MainDashBoardActivity.class);
                    intent.putExtra("fragmentload", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dataSedataSender.setVideoDesc(mEditEmojicon.getText().toString());
                    dataSedataSender.setVideoView_to(String.valueOf(sharetoselected));
                    dataSedataSender.setIsCall(true);
                    // new YouthtubeNupLoad1(CreatePostActivity_New.this);
                } else {
                    CreatePostActivity_New.this.finish();
                }
            }
        });*/

//        getTheView(0);
//        sharebtn.setOnClickListener(new View.OnClickListener()



    /* private void callapiPost(List<String> selectpath, final String textstring) {

         json = new CreatePostJsonInputModel();
         String userid = SharedPreferenceSingleton.getInstance().getStringPreferenceNull(RegistrationConstants.USER_ID);
         String token = SharedPreferenceSingleton.getInstance().getStringPreferenceNull(RegistrationConstants.TOKEN);
         json.setPostCreatorId(userid);
         String textval = textstring == null ? "" : textstring;
         json.setContent(textval);
         json.setType(1);
         json.setCategorie_id(1);
         if (sharetoselected==0) {
             sharetoselected = 1;
             json.setView_to(sharetoselected);
         } else {
             json.setView_to(sharetoselected);
         }


         List<CreatePostJsonInputModel.ImageDataAllToSend> imagedataall = new ArrayList<>();
         if (selectpath != null) {
             for (int i = 0; i < selectpath.size(); i++) {
                 CreatePostJsonInputModel.ImageDataAllToSend imagedata = new CreatePostJsonInputModel.ImageDataAllToSend();
                 File file_for_selectedPath = new File(selectpath.get(i));
                 Uri selected_image_uri=Uri.fromFile(file_for_selectedPath);
                 getIMGSize(selected_image_uri);
                 String path_from_aws = AWSConfiguration.uploadProfileImage(file_for_selectedPath, CreatePostActivity_New.this, RegistrationConstants.POST_IMAGE, userid);
                 imagedata.setUrl(path_from_aws);
                 imagedata.setHeight(String.valueOf(imageHeight));
                 imagedata.setWidth(String.valueOf(imageWidth));
                 imagedataall.add(imagedata);
             }
             json.setImage(imagedataall);
         }


         Apimethods methods = API_Call_Retrofit.getretrofit(CreatePostActivity_New.this).create(Apimethods.class);
         Call<CreatePostJsonResponseModel> call = methods.setpost(json, token);
         String url = call.request().url().toString();
         Log.d("url", "url=" + url);
         call.enqueue(new Callback<CreatePostJsonResponseModel>() {
             @Override
             public void onResponse(Call<CreatePostJsonResponseModel> call, Response<CreatePostJsonResponseModel> response) {
                 if (response.code() == 200) {

                     *//*Intent returnIntent = CreatePostActivity_New.this.getIntent();
                    returnIntent.putStringArrayListExtra("postImage", (ArrayList<String>) selectedPath);
                    returnIntent.putExtra("post_text", textstring);
                    CreatePostActivity_New.this.setResult(2, returnIntent);*//*
                    Toast.makeText(CreatePostActivity_New.this, "Posted successfully", Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent i = new Intent(CreatePostActivity_New.this, MainDashBoardActivity.class);
                    i.putExtra("fragmentload","");
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (response.code() == 401) {

                }
            }

            @Override
            public void onFailure(Call<CreatePostJsonResponseModel> call, Throwable t) {
                Toast.makeText(CreatePostActivity_New.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
