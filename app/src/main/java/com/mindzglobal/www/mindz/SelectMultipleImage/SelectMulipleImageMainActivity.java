package com.mindzglobal.www.mindz.SelectMultipleImage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mindzglobal.www.mindz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectMulipleImageMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static Button openCustomGallery;
    private static GridView selectedImageGridView;
    ListView list_item;
    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    ArrayList<String> filepthlist;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    public static final String CustomGalleryIntentKey = "ImageArray";//Set Intent Key Value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_activity_main);
        initViews();
        setListeners();
        getSharedImages();
    }
    //Init all views
    private void initViews() {
        openCustomGallery = (Button) findViewById(R.id.openCustomGallery);
        selectedImageGridView = (GridView) findViewById(R.id.selectedImagesGridView);
        list_item = (ListView) findViewById(R.id.list_item);
        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(SelectMulipleImageMainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
    }

    //set Listeners
    private void setListeners() {
        openCustomGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openCustomGallery:
//                Start Custom Gallery Activity by passing intent id
                startActivityForResult(new Intent(SelectMulipleImageMainActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);
                break;
        }
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case CustomGallerySelectId:
                if (resultcode == RESULT_OK) {
                    String imagesArray = imagereturnintent.getStringExtra(CustomGalleryIntentKey);//get Intent data
                    //Convert string array into List by splitting by ',' and substring after '[' and before ']'
                    List<String> selectedImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));
                    loadGridView(new ArrayList<String>(selectedImages));
                     filepthlist = new ArrayList<String>(selectedImages);
                    //call load gridview method by passing converted list into arrayList
                }
                break;
        }
    }

    //Load GridView
    private void loadGridView(ArrayList<String> imagesArray) {
//        GridView_Adapter adapter = new GridView_Adapter(SelectMulipleImageMainActivity.this, imagesArray, false);
//        selectedImageGridView.setAdapter(adapter);
        Log.e("imagesArray",""+imagesArray.toString());

        HorizontalAdapter adapters = new HorizontalAdapter(SelectMulipleImageMainActivity.this, imagesArray, false);
        horizontal_recycler_view.setAdapter(adapters);
    }

    //Read Shared Images
    private void getSharedImages() {
        //If Intent Action equals then proceed
        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
                && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);//get Parcelabe list
            ArrayList<String> selectedImages = new ArrayList<>();

            //Loop to all parcelable list
            for (Parcelable parcel : list) {
                Uri uri = (Uri) parcel;//get URI
                String sourcepath = getPath(uri);//Get Path of URI
                selectedImages.add(sourcepath);//add images to arraylist
            }
            Log.e("selectedImages",selectedImages.toString());
            loadGridView(selectedImages);//call load gridview
        }
    }

    //get actual path of uri
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

//        public HorizontalAdapter(List<Data> horizontalList, Context context) {
//            this.horizontalList = horizontalList;
//            this.context = context;
//        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView txtview;
            public MyViewHolder(View view) {
                super(view);
                imageView=(ImageView) view.findViewById(R.id.galleryImageView);
                txtview=(TextView) view.findViewById(R.id.file_name);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

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

//    private void uploadMultiFile() {
//        progressDialog.show();
//
//        ArrayList<String> filePaths = new ArrayList<>();
//        filePaths.add("storage/emulated/0/DCIM/Camera/IMG_20170802_111432.jpg");
//        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/587c4178e4b0060e66732576_294204376.jpg");
//        filePaths.add("storage/emulated/0/Pictures/WeLoveChat/594a2ea4e4b0d6df9153028d_265511791.jpg");
//
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//
//        builder.addFormDataPart("user_name", "Robert");
//        builder.addFormDataPart("email", "mobile.apps.pro.vn@gmail.com");
//
//        // Map is used to multipart the file using okhttp3.RequestBody
//        // Multiple Images
//        for (int i = 0; i < filepthlist.size(); i++) {
//            File file = new File(filepthlist.get(i));
//            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
//        }
//
//        File file = new File("");
//        MultipartBody requestBody = builder.build();
//        Call<ResponseBody> call = uploadService.uploadMultiFile(requestBody);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                Toast.makeText(SelectMulipleImageMainActivity.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
//                Toast.makeText(SelectMulipleImageMainActivity.this, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();
//
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.d(TAG, "Error " + t.getMessage());
//            }
//        });
//
//
//    }


    // use @Body uploadService.uploadMultiFile(requestBody);


//    //@Multipart
//    //@FormUrlEncoded
//    //@POST("/upload_multi_files/MultiUpload.php")
//    @POST("/upload_multi_files/MultiPartUpload.php")
//    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);

}




















