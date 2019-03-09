package com.mindzglobal.www.mindz.FriendRequest.global;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OtherImageAdapter extends RecyclerView.Adapter<OtherImageAdapter.MyViewHolder>{
    private ArrayList<String> dataSet;
    Activity mContext;


    public OtherImageAdapter(ArrayList<String> dataSet, Activity mContext) {
        this.dataSet = dataSet;
        this.mContext = mContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.postImageView=itemView.findViewById(R.id.img1);


        }
    }

    @Override
    public OtherImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_image_box_layout, parent, false);

        view.setOnClickListener(OtherImageFragment.myOnClickListener);
        OtherImageAdapter.MyViewHolder myViewHolder = new OtherImageAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final OtherImageAdapter.MyViewHolder holder, final int listPosition) {

        final ImageView postImageView = holder.postImageView;


//        final String picUrl = dataSet.get(listPosition).getFilePath();

        new Thread(new Runnable() {
            public void run() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Picasso.with(mContext).load(Config.BASE_URL_MEDIA+picUrl)
//                                .centerCrop()
//                                .resize(500,500)
//                                .into(postImageView);
                    }
                });
            }
        }).start();


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
