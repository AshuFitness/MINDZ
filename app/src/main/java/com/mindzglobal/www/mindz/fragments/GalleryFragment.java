package com.mindzglobal.www.mindz.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Posting.StatusPostActivity;
import com.mindzglobal.www.mindz.SelectMultipleImage.GridView_Adapter;
import com.mindzglobal.www.mindz.SelectMultipleImage.SelectMulipleImageMainActivity;

import com.mindzglobal.www.mindz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment  implements View.OnClickListener {
    private static Button selectImages;
    private static GridView galleryImagesGridView;
    private static ArrayList<String> galleryImageUrls;
    private static GridView_Adapter imagesAdapter;
    ArrayList<String> getMediaBase;
    public static final String CustomGalleryIntentKey = "ImageArray";
    SessionManager manager;
    String token,user_pic;
    private static final int CustomGallerySelectId = 1;

    RetrofitClient retrofitClient;
    ArrayList<String> filepthlist;
    RecyclerView horizontal_recycler_view;
    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.customgallery_activity, container, false);

        selectImages = view.findViewById(R.id.selectImagesBtn);
        galleryImagesGridView =  view.findViewById(R.id.selectedImagesGridView);

        setListeners();
        fetchGalleryImages();
        setUpGridView();
        getSharedImages();
        showSelectButton();
//        uploadMultiFile();
        return view;
    }


 /*   private void uploadMultiFile() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

               // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (int i = 0; i < getMediaBase.size(); i++) {
            builder.addFormDataPart("HOMmedia[]", getMediaBase.get(i));
        }

        MultipartBody requestBody = builder.build();
        RetrofitClient.getClient().uploadMultiFile(requestBody,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(galleryImagesGridView) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("PostRes",""+response.toString());
                Log.e("CheckPostRes",""+response.body().toString());
                Toast.makeText(getActivity(), ""+response.body().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/


    //fetch all images from gallery
    private void fetchGalleryImages() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};//get all columns of type images
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;//order data by date
        Cursor imagecursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order

        galleryImageUrls = new ArrayList<String>();//Init array

        //Loop to cursor count
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index
            galleryImageUrls.add(imagecursor.getString(dataColumnIndex));//get Image from column index
            System.out.println("Array path" + galleryImageUrls.get(i));
        }
    }

    //Set Up GridView method
    private void setUpGridView() {
        imagesAdapter = new GridView_Adapter(GalleryFragment.this, galleryImageUrls, true);
        galleryImagesGridView.setAdapter(imagesAdapter);
    }


    //Set Listeners method
    private void setListeners() {
        selectImages.setOnClickListener(this);
    }


    //Show hide select button if images are selected or deselected
    public void showSelectButton() {
        ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();
        if (selectedItems.size() > 0) {
            selectImages.setText(selectedItems.size() + " - Images Selected");
            selectImages.setVisibility(View.VISIBLE);
        }
        else
            selectImages.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectImagesBtn:

                ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();
                ArrayList<String> imagesAdapters = imagesAdapter.getCheckedItems();

                Intent intent = new Intent(getActivity(),StatusPostActivity.class);
                intent.putExtra(SelectMulipleImageMainActivity.CustomGalleryIntentKey, selectedItems.toString());//Convert Array into string to pass data
                getActivity().setResult(RESULT_OK, intent);//Set result OK
                getActivity().finish();//finish activity
                break;
        }
    }

    public void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
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

        HorizontalAdapter adapters = new HorizontalAdapter(getActivity(), imagesArray, false);
        horizontal_recycler_view.setAdapter(adapters);
    }

    //Read Shared Images
    private void getSharedImages() {
        //If Intent Action equals then proceed
        if (Intent.ACTION_SEND_MULTIPLE.equals(getActivity().getIntent().getAction())
                && getActivity().getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getActivity().getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);//get Parcelabe list
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
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
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
//            TextView txtview;
            public MyViewHolder(View view) {
                super(view);
                imageView=(ImageView) view.findViewById(R.id.galleryImageView);
//                txtview=(TextView) view.findViewById(R.id.file_name);
            }
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card_layout, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, final int position) {

//            holder.imageView.setImageResource(imageUrls.get(position));

            ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position), holder.imageView, options);//Load Images over ImageView

//            holder.txtview.setText(imageUrls.get(position));

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


}
