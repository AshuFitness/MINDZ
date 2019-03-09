package com.mindzglobal.www.mindz.Posting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.mindzglobal.www.mindz.CropMain.CropingOption;
import com.mindzglobal.www.mindz.CropMain.CropingOptionAdapter;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.LocationAccessUtils.PermissionUtils;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.SelectMultipleImage.CustomGallery_Activity;
import com.mindzglobal.www.mindz.fragments.CategoryFragment;
import com.mindzglobal.www.mindz.fragments.GalleryFragment;
import com.mindzglobal.www.mindz.fragments.SelectedGalaryImageAdapter;
import com.mindzglobal.www.mindz.fragments.VideoFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import tyrantgit.explosionfield.ExplosionField;

//import com.mindzglobal.www.mindz.VideoFragment;


public class StatusPostActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    private ExplosionField mExplosionField;
    RecyclerView imageList;
    private View hiddenPanel;

    @Bind(R.id.status_post_text)
    EditText mStatus_post_text;

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

    @Bind(R.id.choose_cat)
    MyTextVieww mChoose_cat;


    @Bind(R.id.category)
    MyTextVieww mCategory;


    /*@Bind(R.id.cancel_img_vid)
    ImageView mCancel_img_vid;*/



    @Bind(R.id.videoView)
    VideoView mVideo;


    @Bind(R.id.main_screen)
    RelativeLayout mMain_screen;

    @Bind(R.id.gallery)
    FrameLayout gallery;

    @Bind(R.id.bottom)
   LinearLayout bottom;

    CircleImageView mProfile_pic_img;

    private Uri mImageCaptureUri;
    private File outPutFile = null;
    Bitmap photo;

    private JSONObject jsonObject;

    private final static int REQUEST_PERMISSION_REQ_CODE = 34;

    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301,VIDEO_CODE = 401,CAPTURE_VIDEO = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private String selectedPath;

    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    ArrayList<String> filepthlist;
    ArrayList<String> getMediaBase;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value
    String[] suggestions = new String[]{"Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Mexico", "Jalapeno"};

    SessionManager manager;
    String token,user_pic;

    RetrofitClient retrofitClient;

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
//    List<String> selectedPath = null;
    List<String> videoimagePath = null;
    double latitude;
    double longitude;
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    SelectedGalaryImageAdapter mAdapter;
    boolean isPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_post_layout);
        ButterKnife.bind(this);
        imageList =  findViewById(R.id.selected_imge);
        manager = new SessionManager();
        token = manager.getPreferences(StatusPostActivity.this, Constants.USER_TOKEN);
        user_pic = manager.getPreferences(StatusPostActivity.this, Constants.USER_PROFILE_PIC);
        retrofitClient = new RetrofitClient(token);
        mProfile_pic_img = findViewById(R.id.profile_pic_img);

        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(StatusPostActivity.this).load(Config.BASE_URL_MEDIA+user_pic)
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

        permissionUtils=new PermissionUtils(StatusPostActivity.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);

        mExplosionField = ExplosionField.attach2Window(this);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        jsonObject = new JSONObject();

        getMediaBase =  new ArrayList<>();

        mAddImage.setOnClickListener(this);
        mAddVideo.setOnClickListener(this);
        mChoose_cat.setOnClickListener(this);
        mPostBtn.setOnClickListener(this);
        mCheck_in_btn.setOnClickListener(this);


        initViews();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.getVisibility() == View.VISIBLE) {
                    if (selectedPath != null) {
                        uploadMultiFile();
                    }
                } else if (mVideo.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(StatusPostActivity.this, HomeFragment.class);
                    intent.putExtra("fragmentload", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    StatusPostActivity.this.finish();
                }
            }
        });


      //  autoComplete();
    }

    private void initViews() {
//        openCustomGallery = (Button) findViewById(R.id.openCustomGallery);
//        selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);
//        list_item = (ListView) findViewById(R.id.list_item);
        horizontal_recycler_view=  findViewById(R.id.horizontal_recycler_view);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(StatusPostActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
    }




    public void getTheView(int i) {

        if (i == 0) {
            hideSoftKeyboard();
            changeFragment(new GalleryFragment());

        } else if (i == 1) {
            hideSoftKeyboard();
            changeFragment(new CategoryFragment());
        } else if (i == 2) {
            hideSoftKeyboard();
            changeFragment(new VideoFragment());
        } else {
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


/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                Intent it =new Intent(this,ActivityGallery.class );
                SharedPreferenceSingleton.getInstance().init(this);
                it.putExtra("camera","camera");
                SharedPreferenceSingleton.getInstance().writeStringPreference(ApplicationConstants.CAPTURED_IMAGE_PATH,mImageCaptureUri.getPath());
                startActivityForResult(it, 4);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }  if (requestCode == VIDEO_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    // video successfully recorded
                    // preview the recorded video
                    //previewVideo();
                    break;
                case RESULT_CANCELED:
                    // user cancelled recording
                    Toast.makeText(getApplicationContext(),
                            "User cancelled video recording", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    // failed to record video
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
        if (resultCode == 4) {
            getSupportActionBar().show();
            ArrayList<String> imagePath = new ArrayList<String>();
            imagePath = data.getStringArrayListExtra("ImagePath");
            //selectedPath=imagePath.get(0);
            selectedPath = imagePath;
            mAdapter = new SelectedGalaryImageAdapter(this, imagePath);
            imageList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            madapter_item_click();
        }
        if (resultCode == 5) {
            ArrayList<String> imagePath = data.getStringArrayListExtra("selectedImage");
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
*/
/*
    private void madapter_item_click() {
        mAdapter.setOnItemClickListener(new SelectedGalaryImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ArrayList<String> imagePath, int clickedPosition) {
                //selectedPath = imagePath;
                Intent intent = new Intent(StatusPostActivity.this, ActivityGallery_Viewpager.class);
                intent.putStringArrayListExtra(ApplicationConstants.FILTERED_IMAGE_PATH, imagePath);
                intent.putExtra("position_id", clickedPosition);
//                 SharedPreferenceSingleton.getInstance().init(StatusPostActivity.this);
//                SharedPreferenceSingleton.getInstance().writeStringPreference(ApplicationConstants.FILTERED_IMAGE_PATH,selectedPath);
                startActivityForResult(intent, 4);
            }
        });
    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI : "+mImageCaptureUri);
            CropingIMG();

        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            System.out.println("Camera Image URI : "+mImageCaptureUri);
            CropingIMG();
//                 CropingCameraIMG();
        }

        else if (requestCode == VIDEO_CODE && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera Image URI : "+mImageCaptureUri);
            String path = data.getData().toString();

//            setVideo(path);

        }
        else if (requestCode == CAPTURE_VIDEO && resultCode == Activity.RESULT_OK) {
            System.out.println("SELECT_VIDEO");
            Uri selectedImageUri = data.getData();
            selectedPath = getPath(selectedImageUri);
//            setVideo(selectedPath);

//            MyProfileVideoManager u = new MyProfileVideoManager();
//            String msg = u.uploadVideo(selectedPath);
//            getMediaBase.add(msg);


//            int partCounter = 1;
//            File f = new File(selectedPath);
//            List<File> result = new ArrayList<>();
//            int sizeOfFiles = 1024 * 1024;// 1MB
//            byte[] buffer = new byte[sizeOfFiles]; // create a buffer of bytes sized as the one chunk size
//
//            BufferedInputStream bis = null;
//            try {
//                bis = new BufferedInputStream(new FileInputStream(f));
//                String name = f.getName();
//
//                int tmp = 0;
//                while ((tmp = bis.read(buffer)) > 0) {
//                    File newFile = new File(f.getParent(), name + "." + String.format("%03d", partCounter++)); // naming files as <inputFileName>.001, <inputFileName>.002, ...
//                    FileOutputStream out = new FileOutputStream(newFile);
//                    out.write(buffer, 0, tmp);//tmp is chunk size. Need it for the last chunk, which could be less then 1 mb.
//                    result.add(newFile);
//                }
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }

        else if(requestCode == CustomGallerySelectId && resultCode == Activity.RESULT_OK ){
            String imagesArray = data.getStringExtra(CustomGalleryIntentKey);//get Intent data
            //Convert string array into List by splitting by ',' and substring after '[' and before ']'
            List<String> selectedImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));
            loadGridView(new ArrayList<String>(selectedImages));
            filepthlist = new ArrayList<String>(selectedImages);

            for (int i = 0; i < filepthlist.size(); i++) {
                File file = new File(filepthlist.get(i));
                photo = decodeFile(file);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                getMediaBase.add(encodedImage);

            }

        }

        else if(requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK ){
            getLocation();
        }
        else if (requestCode == CROPING_CODE) {
            try {
                if(outPutFile.exists()){

//                    mVideo.setVisibility(View.GONE);
//                    imageView.setVisibility(View.VISIBLE);
//                    mCancel_img_vid.setVisibility(View.VISIBLE);
                    photo = decodeFile(outPutFile);
//                    imagess = getStringImage(photo);
//                    imageView.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Load GridView
    private void loadGridView(ArrayList<String> imagesArray) {
//        GridView_Adapter adapter = new GridView_Adapter(SelectMulipleImageMainActivity.this, imagesArray, false);
//        selectedImageGridView.setAdapter(adapter);
        Log.e("imagesArray",""+imagesArray.toString());

        HorizontalAdapter adapters = new HorizontalAdapter(StatusPostActivity.this, imagesArray, false);
        horizontal_recycler_view.setAdapter(adapters);
    }



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

 /*   private void setVideo(final String path){
        try
        {
            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(mVideo);

//            imageView.setVisibility(View.GONE);
//            mCancel_img_vid.setVisibility(View.GONE);
            mVideo.setVisibility(View.VISIBLE);
            mVideo.setMediaController(mediaController);
            mVideo.setVideoPath(path);
            mVideo.requestFocus();
            mVideo.stopPlayback();
            mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
//                    VideoprogressBar.setVisibility(View.GONE);
                    mp.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                       int arg2) {
                            // TODO Auto-generated method stub
//                            VideoprogressBar.setVisibility(View.GONE);
                            mp.pause();
                        }
                    });
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    private void CropingIMG(){
        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title  = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROPING_CODE);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

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


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.add_img:
                getTheView(0);
                hideSoftKeyboard();

//                reset(imageView);
//                selectImageOption();
//                startActivityForResult(new Intent(StatusPostActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);
                break;
            case R.id.add_video:
                getTheView(2);
                hideSoftKeyboard();
//                selectVideoOption();
                break;


            case R.id.choose_cat:
//                mChoose_cat_rel.setVisibility(View.VISIBLE);
//                reset(findViewById(R.id.root));
                getTheView(1);
                mExplosionField.clear();
                break;

            case R.id.cancel_img_vid:
//                mExplosionField.explode(imageView);
//                imageView.setImageDrawable(null);
//                mCancel_img_vid.setVisibility(View.GONE);
                break;

            case R.id.openCustomGallery:
//                Start Custom Gallery Activity by passing intent id
                startActivityForResult(new Intent(StatusPostActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);
                break;

            case R.id.post_btn:
                uploadMultiFile();
                break;

            case R.id.check_in_btn:
                getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {

//                    if(btnProceed.isEnabled())
//                        btnProceed.setEnabled(false);
//
//                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }
                break;

        }
    }



    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }


    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        }
        else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {

        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
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


    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

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

    private void uploadMultiFile() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("Category", mCategory.getText().toString());
        builder.addFormDataPart("StatusText", mStatus_post_text.getText().toString());
        builder.addFormDataPart("CheckIn", mAt_location.getText().toString());
        builder.addFormDataPart("TagFriend", "Bangalore");
//        builder.addFormDataPart("fragmentload","");

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (int i = 0; i < getMediaBase.size(); i++) {
            builder.addFormDataPart("HOMmedia[]", getMediaBase.get(i));
        }

        MultipartBody requestBody = builder.build();
        RetrofitClient.getClient().uploadMultiFile(requestBody,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(mAddImage) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("PostRes",""+response.toString());
                Log.e("CheckPostRes",""+response.body().toString());
                Toast.makeText(StatusPostActivity.this, ""+response.body().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

            if (!TextUtils.isEmpty(city)) {
                currentLocation = area + " , " + city;
            }


            mAt_location.setText(currentLocation);

        }

//        }

    }

    /**
     * Creating google api client object
     * */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

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
                            status.startResolutionForResult(StatusPostActivity.this, REQUEST_CHECK_SETTINGS);

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


}