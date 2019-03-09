package com.mindzglobal.www.mindz.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.R;

import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageView.OnPictureSavedListener;
import static android.graphics.BitmapFactory.decodeResource;
import static com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize;

public class ActivityGallery extends Activity implements OnClickListener, OnPictureSavedListener {
    private static final int REQUEST_PICK_IMAGE = 1;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private GPUImageView mGPUImageView;
    LinearLayoutManager layoutManager;
    public List<ModelClass> mfilters = new ArrayList<>();
    RecyclerView mRecyclerView ;
    //RecyclerView recList;
    Bitmap mMainBitmap,mBitmap;
    TextView SendImage;
    Uri imageUri;
    ExFilterAdapter exFilterAdapter;
    ImageView imageView;
    String path;
    private ProgressDialog progress;
    FilterAdapter mAdapter;
    Bitmap mThumbNailBitmap;
    private static final String TAG = "SampleActivity";
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "homiez";
    private ImageView cropImage;
    boolean isfilterShowing;

    private GPUImageFilter selectedFilter;
    private int percent;
    private String path_camera=" ";
    ArrayList<String> ori_list=new ArrayList<>();
    ArrayList<String> newlist=new ArrayList<>();
   // CropImage cropImageview;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        //recList =(RecyclerView) findViewById(R.id.exDrawable);
        final ImageView filterButton =(ImageView) findViewById(R.id.filterButton);
        SendImage =findViewById(R.id.ImageOperationDoneButton);
        mRecyclerView =  findViewById(R.id.recyclerView);
        mGPUImageView =  findViewById(R.id.gpuimage);
        //cropImageview = ) findViewById(R.id.cropImageView);
        cropImage     =  findViewById(R.id.crop_image);
        path_camera=getIntent().getStringExtra("camera");
        SharedPreferenceSingleton.getInstance().init(this);
        if(path_camera.equalsIgnoreCase("camera")){
            path = SharedPreferenceSingleton.getInstance().getStringPreference(ApplicationConstants.CAPTURED_IMAGE_PATH);
        }else {
            path = SharedPreferenceSingleton.getInstance().getStringPreference("image_path");
            ori_list=getIntent().getStringArrayListExtra("ori_list");
        }
        handleImage(path,0);
        //recList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
       // recList.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        exFilterAdapter = new ExFilterAdapter(this, new ExColorDrawable() {
            @Override
            public void drawableList(int Position) {}
        });


        SendImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveImage();
            }
        });
        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if(isfilterShowing){
                  mRecyclerView.setVisibility(View.GONE);
                  isfilterShowing=false;
                  filterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter));
              }else{
                  mRecyclerView.setVisibility(View.VISIBLE);
                  filterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_selection));
                  isfilterShowing=true;
              }
            }
        });


        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBitmap = decodeResource(this.getResources(), R.drawable.pro2);

         mAdapter = new FilterAdapter(this, mfilters, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
            @Override
            public void onGpuImageFilterChosenListener(GPUImageFilter filter,int per) {
                mFilter=null;
                    switchFilterTo(filter,per);
                    selectedFilter=filter;
                    percent=per;
                    mGPUImageView.requestRender();
                    mGPUImageView.getGPUImage();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        isfilterShowing=true;
       // filterButton.setBackground(getResources().getDrawable(R.drawable.di_grayround));
        filterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_selection));
       //cropImage.setBackground(getResources().getDrawable(R.drawable.di_grayround));
       cropImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_crop_rotation));
        cropImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri selectedImage = Uri.fromFile(new File(path));
                Uri destinationUri = Uri.fromFile(new File(ActivityGallery.this.getCacheDir(), "IMG_" + System.currentTimeMillis()+".jpg"));

                CropImage.activity(selectedImage)
                        .start(ActivityGallery.this);
            }
        });
        setmDataset();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }


  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
      if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
          CropImage.ActivityResult result = CropImage.getActivityResult(data);
          if (result == null) {
              //selectedFunction(file, type, 0);
          }
          if (resultCode == RESULT_OK) {
              Uri resultUri = result.getUri();
              File file = new File(resultUri.getPath());
              String st =file.getPath();
              handleImage(st,1);
          } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
              Exception error = result.getError();
          }
      }
  }

    private void switchFilterTo(final GPUImageFilter filter,int per) {

        if (mFilter == null || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            mGPUImageView.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            mFilterAdjuster.adjust(per);

        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.b_upload:
                break;

            default:
                break;
        }

    }

    @Override
    public void onPictureSaved(final Uri uri) {
        progress.hide();
        Toast.makeText(this, "Saved: " + uri.toString(), Toast.LENGTH_SHORT).show();
        String path1=getPath(uri);
        if(path_camera.equalsIgnoreCase("camera")){
            newlist.clear();
            newlist.add(path1);
            Intent intent = new Intent();
            intent.putStringArrayListExtra("ImagePath",newlist);
            setResult(4, intent);
            finish();
        }else {
            for (int i = 0; i < ori_list.size(); i++) {
                if (ori_list.get(i).contains(path)) {
                    newlist.add(ori_list.get(i).replace(path, path1));
                    //someList.set(i, someList.get(i).replace(someString, otherString));
                } else {

                    // If it not contains `someString`, add it as it is to newList
                    newlist.add(ori_list.get(i));
                }

            }

            Intent intent = new Intent();
            intent.putStringArrayListExtra("filterlistimage", newlist);
            // intent.putExtra("ImagePath1",path1);
            //intent.putExtra("originalimage",path);
            setResult(6, intent);
            finish();
        }
    }

    private void  saveImage() {
        String fileName = System.currentTimeMillis()+".jpg";
        mGPUImageView.saveToPictures("Homiez", fileName, this);
        progress = new ProgressDialog(this);
        progress.setMessage("saving...");
        progress.show();
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    private void switchFilterTo(final GPUImageFilter filter, GPUImageView ImageView) {

        if (mFilter == null || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            ImageView.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            findViewById(R.id.seekBar).setVisibility(mFilterAdjuster.canAdjust() ? View.VISIBLE : View.GONE);
        }
    }


    private void handleImage(String Url, int isCropImage) {
        SharedPreferenceSingleton.getInstance().init(this);
        String isfromBackCamera = SharedPreferenceSingleton.getInstance().getStringPreference(RegistrationConstants.cameraReotation);
       if(isfromBackCamera.equals(RegistrationConstants.ChatFront)) {
           Bitmap bitmap;
           bitmap = flip(rotate(Url ,-90));
           //Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 810, 1080, false);
           mThumbNailBitmap = Bitmap.createScaledBitmap(bitmap, 330, 440, false);
           mGPUImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
           mGPUImageView.setImage(bitmap);
       }else{
           BitmapFactory.Options options = new BitmapFactory.Options();
           options.inJustDecodeBounds = true;
           BitmapFactory.decodeFile(new File(Url).getAbsolutePath(), options);
           int imageHeight = options.outHeight;
           int imageWidth = options.outWidth;

           Bitmap decodedBitmap = decodeSampledBitmapFromResource(Url, imageWidth, imageHeight);
          // Bitmap decodedBitmap = decodeSampledBitmapFromResource(Url, 810, 1080);
           Bitmap decodedtnBitmap = decodeSampledBitmapFromResource(Url, 330, 400);
           mThumbNailBitmap = Bitmap.createScaledBitmap(setBitMap(decodedtnBitmap, Url), 330, 440, false);
           mGPUImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
           mGPUImageView.setImage(setBitMap(decodedBitmap, Url));
       }
    }

    public void setmDataset() {
        mfilters.add(new ModelClass("Normal", GPUImageFilterTools.FilterType.PIXELATION, updateImage(GPUImageFilterTools.FilterType.PIXELATION,0,0),0, false,0));
        mfilters.add(new ModelClass("Hue",GPUImageFilterTools.FilterType.HUE, updateImage(GPUImageFilterTools.FilterType.HUE,0,23), 23, false,0));
        mfilters.add(new ModelClass("Gamma",GPUImageFilterTools.FilterType.GAMMA, updateImage(GPUImageFilterTools.FilterType.GAMMA,0,13), 13, false,0));
        mfilters.add(new ModelClass("Sepia",GPUImageFilterTools.FilterType.SEPIA, updateImage(GPUImageFilterTools.FilterType.SEPIA,0,50), 50, false,0));
        mfilters.add(new ModelClass("Grayscale",GPUImageFilterTools.FilterType.GRAYSCALE, updateImage(GPUImageFilterTools.FilterType.GRAYSCALE,0,0), 30, false,0));
        mfilters.add(new ModelClass("Sharpness",GPUImageFilterTools.FilterType.SHARPEN, updateImage(GPUImageFilterTools.FilterType.SHARPEN,0,62), 62, false,0));

        buildFilterButton task = new buildFilterButton();
        task.execute(new String[] { "Pixelation" });

    }


    public void startColor(int percent) {
        GPUImageFilter filter = new GPUImagePosterizeFilter();
        switchFilterTo(filter, mGPUImageView);
        mGPUImageView.requestRender();
        GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
        mFilterAdjuster.adjust(percent);
        imageView.setVisibility(View.GONE);
    }

    public Bitmap updateImage(GPUImageFilterTools.FilterType type,int drawable,int percent) {
        Bitmap filterImageBitmap;
        filterImageBitmap = FilterImage(this, mThumbNailBitmap, GPUImageFilterTools.createFilterForType(this, type,drawable), percent);
        return filterImageBitmap;
    }

    public  Bitmap FilterImage(Context context, Bitmap mBitmap, GPUImageFilter filter, int percent) {
        GPUImage gpuImage = new GPUImage(context);
        GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
        mFilterAdjuster.adjust(percent);
        gpuImage.setImage(mBitmap);
        gpuImage.setFilter(filter);
        gpuImage.requestRender();
        Bitmap bitmapAfterFilter = gpuImage.getBitmapWithFilterApplied();
        gpuImage.deleteImage();
        return bitmapAfterFilter;

    }



    private class buildFilterButton extends AsyncTask<String, String, String>{
    @Override
    protected String doInBackground(String... params) {
        mfilters.add(new ModelClass("Emboss",GPUImageFilterTools.FilterType.EMBOSS, updateImage(GPUImageFilterTools.FilterType.EMBOSS,0,20), 20, false,0));
        mfilters.add(new ModelClass("Posterize",GPUImageFilterTools.FilterType.POSTERIZE, updateImage(GPUImageFilterTools.FilterType.POSTERIZE,0,36), 36, false,0));
        mfilters.add(new ModelClass("White Balance",GPUImageFilterTools.FilterType.WHITE_BALANCE, updateImage(GPUImageFilterTools.FilterType.WHITE_BALANCE,0,28), 28, false,0));
        mfilters.add(new ModelClass("ToneCurve",GPUImageFilterTools.FilterType.TONE_CURVE, updateImage(GPUImageFilterTools.FilterType.TONE_CURVE,0,0), 30, false,0));
        mfilters.add(new ModelClass("Lookup (Amatorka)",GPUImageFilterTools.FilterType.LOOKUP_AMATORKA, updateImage(GPUImageFilterTools.FilterType.LOOKUP_AMATORKA,0,0), 30, false,0));
        mfilters.add(new ModelClass("Pixelation", GPUImageFilterTools.FilterType.PIXELATION, updateImage(GPUImageFilterTools.FilterType.PIXELATION,0,8), 30, false,0));
        mfilters.add(new ModelClass("Toon",GPUImageFilterTools.FilterType.TOON, updateImage(GPUImageFilterTools.FilterType.TOON,0,0), 30, false,0));
        mfilters.add(new ModelClass("Bulge Distortion",GPUImageFilterTools.FilterType.BULGE_DISTORTION, updateImage(GPUImageFilterTools.FilterType.BULGE_DISTORTION,0,0), 30, false,0));
        /*FILTER1*/
        mfilters.add(new ModelClass("12aBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50amber,0), 30, false,R.drawable.a50amber));
        mfilters.add(new ModelClass("20aBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50amber,0), 30, false,R.drawable.a50amber));
        /*FILTER1*/
        mfilters.add(new ModelClass("3bBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50blue,0), 30, false,R.drawable.a50blue));
       /*FILTER1*/
        mfilters.add(new ModelClass("3cBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50cyan,0), 30, false,R.drawable.a50cyan));
        /*FILTER1*/
        mfilters.add(new ModelClass("12dBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50deep_orange,0), 30, false,R.drawable.a50deep_orange));
        mfilters.add(new ModelClass("20dBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50deep_orange,0), 30, false,R.drawable.a50deep_orange));
        /*FILTER1*/
         mfilters.add(new ModelClass("3eBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50deep_purple,0), 30, false,R.drawable.a50deep_purple));
        /*FILTER1*/
        mfilters.add(new ModelClass("20fBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50green,0), 30, false,R.drawable.a50green));
        /*FILTER1*/
        mfilters.add(new ModelClass("3gBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50indigo,0), 30, false,R.drawable.a50indigo));
         /*FILTER1*/
        mfilters.add(new ModelClass("12hBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50light_blue,0), 30, false,R.drawable.a50light_blue));
      /*FILTER1*/
        mfilters.add(new ModelClass("3iBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50light_green,0), 30, false,R.drawable.a50light_green));
       /*FILTER1*/
        mfilters.add(new ModelClass("12jBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50lime,0), 30, false,R.drawable.a50lime));
        /*FILTER1*/
        mfilters.add(new ModelClass("20kBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50orange,0), 30, false,R.drawable.a50orange));
        /*FILTER1*/
        mfilters.add(new ModelClass("3lBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50pink,0), 30, false,R.drawable.a50pink));
        mfilters.add(new ModelClass("12lBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50pink,0), 30, false,R.drawable.a50pink));
        /*FILTER1*/
        mfilters.add(new ModelClass("3mBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50purple,0), 30, false,R.drawable.a50purple));
        mfilters.add(new ModelClass("20mBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50purple,0), 30, false,R.drawable.a50purple));
        /*FILTER1*/
        mfilters.add(new ModelClass("20nBlend (Linear Burn)",GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_LINEAR_BURN,R.drawable.a50red,0), 30, false,R.drawable.a50red));
        /*FILTER1*/
        mfilters.add(new ModelClass("3oBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50teal,0), 30, false,R.drawable.a50teal));
        mfilters.add(new ModelClass("12oBlend (Multiply)",GPUImageFilterTools.FilterType.BLEND_MULTIPLY, updateImage(GPUImageFilterTools.FilterType.BLEND_MULTIPLY,R.drawable.a50teal,0), 30, false,R.drawable.a50teal));
        /*yellow*/
        mfilters.add(new ModelClass("3pBlend (Color Burn)",GPUImageFilterTools.FilterType.BLEND_COLOR_BURN, updateImage(GPUImageFilterTools.FilterType.BLEND_COLOR_BURN,R.drawable.a50yellow,0), 30, false,R.drawable.a50yellow));
         return null;
    }

    @Override
    protected void onPostExecute(String result) {
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}

    public static Bitmap rotate(String url , int degree) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(url, opts);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static Bitmap flip(Bitmap src) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            File file = new File(resultUri.getPath());
            String st =file.getPath();
            handleImage(st,1);

        } else {
            Toast.makeText(this,"toast_cannot_retrieve_selected_image", Toast.LENGTH_SHORT).show();
        }
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

    public Bitmap  setBitMap(Bitmap decodedBitmap,String previewFilePath){
        Bitmap resultBitmap = rotateBitmap(decodedBitmap, getExifOrientation(previewFilePath));
     return resultBitmap;
    }
    private Bitmap decodeSampledBitmapFromResource(String url,
                                                   int requestedWidth, int requestedHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);

        options.inSampleSize = calculateInSampleSize(options, requestedWidth, requestedHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(url, options);
    }
    private int getExifOrientation(String previewFilePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(previewFilePath);
        } catch (IOException ignore) {
        }
        return exif == null ? ExifInterface.ORIENTATION_UNDEFINED :
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
    }
    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError ignore) {
            return null;
        }
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap=null;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
           return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

