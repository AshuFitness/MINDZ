package com.mindzglobal.www.mindz.fragments;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;




public class MyCustomPagerAdapter1 extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> pathlist=null;
    private OnItemClickListener mOnItemClickListener;
    ImageView edit;
    public interface OnItemClickListener {
        void onItemClick(View view, String imagePath, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public MyCustomPagerAdapter1(Context context, ArrayList<String> pathlist, ImageView edit) {
        this.context = context;
        this.edit = edit;
        this.pathlist = pathlist;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return pathlist.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item1, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        /*Glide.with(context)
                .load(pathlist.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(imageView);*/
        Glide.with(context).load(pathlist.get(position)).fitCenter().into(imageView);
        container.addView(itemView);
       /* edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int pos=getItemPosition(imageView);
                    mOnItemClickListener.onItemClick(v,pathlist.get(position).toString(),position);
                }
            }
        });*/
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
