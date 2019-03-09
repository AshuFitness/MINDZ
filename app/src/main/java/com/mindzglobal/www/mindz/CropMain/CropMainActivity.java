package com.mindzglobal.www.mindz.CropMain;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CropMainActivity extends AppCompatActivity {

    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
        private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;

        private Button btn_select_image,btn_upload;
        private ImageView imageView;
        private Uri mImageCaptureUri;
        private File outPutFile = null;
         Bitmap photo;
        String token;
        SessionManager manager;
        String imagess;
        Context context;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.crop_activity_main);
            manager = new SessionManager();
            token = manager.getPreferences(CropMainActivity.this, Constants.USER_TOKEN);

            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

            btn_select_image =  findViewById(R.id.btn_select_image);
            btn_upload =  findViewById(R.id.btn_upload);
            imageView =  findViewById(R.id.img_photo);

            btn_select_image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    selectImageOption();
                }
            });


            btn_upload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage(imagess);
//
                }
            });
        }

        private void selectImageOption() {
            final CharSequence[] items = { "Capture Photo", "Choose from Gallery", "Cancel" };

            AlertDialog.Builder builder = new AlertDialog.Builder(CropMainActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {


                        if (items[item].equals("Capture Photo")) {


//                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
//                        mImageCaptureUri = Uri.fromFile(f);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                            mImageCaptureUri = FileProvider.getUriForFile(CropMainActivity.this, authorities, outPutFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(intent, CAMERA_CODE);

                        }
                    else if (items[item].equals("Choose from Gallery")) {

                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY_CODE);

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(CropMainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

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
            } else if (requestCode == CROPING_CODE) {

                try {
                    if(outPutFile.exists()){
                         photo = decodeFile(outPutFile);
                         imagess = getStringImage(photo);
                        imageView.setImageBitmap(photo);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void CropingIMG() {
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
                    ResolveInfo res =  list.get(0);

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
                    } );

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
            } catch (FileNotFoundException e) {
            }
            return null;
        }

    private void uploadImage(final String image) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading Image...","Please Wait...",false,false);


        RetrofitClient.getClient().postProfilepic(image,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(btn_select_image) {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                loading.dismiss();
                Log.e("Checkpic",response.toString());
                String picres = response.body().toString();


                if(picres.contains("1")){
                    finish();
                }
                else  if(picres.contains("2"))
                {
                    Toast.makeText(CropMainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    }



/*
    if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( context, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_PERMISSION_REQ_CODE);*/
