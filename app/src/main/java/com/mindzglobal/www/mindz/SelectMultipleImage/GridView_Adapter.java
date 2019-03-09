package com.mindzglobal.www.mindz.SelectMultipleImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.fragments.GalleryFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class GridView_Adapter extends BaseAdapter {
    private GalleryFragment context;
    private ArrayList<String> imageUrls;
    private SparseBooleanArray mSparseBooleanArray;//Variable to store selected Images
    private DisplayImageOptions options;
    private boolean isCustomGalleryActivity;//Variable to check if gridview is to setup for Custom Gallery or not
    private int a = 0;

    public GridView_Adapter(GalleryFragment context, ArrayList<String> imageUrls, boolean isCustomGalleryActivity) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.isCustomGalleryActivity = isCustomGalleryActivity;
        mSparseBooleanArray = new SparseBooleanArray();


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public GridView_Adapter(CustomGallery_Activity customGallery_activity, ArrayList<String> galleryImageUrls, boolean isCustomGalleryActivity) {
    }

    //Method to return selected Images
    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for (int i = 0; i < imageUrls.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(imageUrls.get(i));
            }
        }

        return mTempArry;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imageUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.customgridview_item, viewGroup, false);//Inflate list_single

        final CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.selectCheckBox);

        final ImageView imageView = (ImageView) view.findViewById(R.id.galleryImageView);
        final TextView filename = (TextView) view.findViewById(R.id.file_name);

        //If Context is SelectMulipleImageMainActivity then hide checkbox
        if (!isCustomGalleryActivity)
            mCheckBox.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position), imageView, options);//Load Images over ImageView
        filename.setText(imageUrls.get(position));
        filename.setVisibility(View.GONE);
        mCheckBox.setTag(position);//Set Tag for CheckBox
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mCheckBox

                if(!mCheckBox.isChecked()){
                    mCheckBox.setChecked(true);
                }
               else if(mCheckBox.isChecked()){
                    mCheckBox.setChecked(false);
                }
            }
        });

        return view;
    }

    final CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                a++;
                if (a >= 5) {
                    Toast.makeText(context.getActivity(), "Do not exceed", Toast.LENGTH_SHORT).show();
                } else {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);//Insert selected checkbox value inside boolean array
                    (context).showSelectButton();
                }
            } else {
                a--;
                if (a > 5) {
                    Toast.makeText(context.getActivity(), "Do not exceed", Toast.LENGTH_SHORT).show();
                } else {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);//Insert selected checkbox value inside boolean array
                    (context).showSelectButton();
                }
            }
            //call custom gallery activity method
        }
    };

}
