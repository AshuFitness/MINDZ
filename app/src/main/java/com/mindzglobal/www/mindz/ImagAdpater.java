package com.mindzglobal.www.mindz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.SectionListDataAdapter;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.Image;
import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class ImagAdpater extends RecyclerView.Adapter<ImagAdpater.MyViewHolder> {

    private ArrayList<GetFeedx> dataSet;
    Activity mContext;
    View root;
    SessionManager manager;
    String token;
    String post_id;
    List<Image> feed_pic;
    Animation activeLikeAnim,LikeAnim;

    private ExplosionField mExplosionField;
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView postImageView;
        RecyclerView media_view;

        CardView card_view;
        ImageView likesImageView, activelikesImageView;

        RelativeLayout on_click_desc;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.postImageView = itemView.findViewById(R.id.itemImage1);
            this.card_view =  itemView.findViewById(R.id.cards);
        }
    }
    public ImagAdpater(ArrayList<GetFeedx> data, Activity mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    @Override
    public ImagAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_item_box1, parent, false);

        manager = new SessionManager();
        token = manager.getPreferences(mContext, Constants.USER_TOKEN);

        view.setOnClickListener(ImageActivity.myOnClickListener);

        ImagAdpater.MyViewHolder myViewHolder = new ImagAdpater.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ImagAdpater.MyViewHolder holder, final int listPosition) {

        ImageView postImage = holder.postImageView;
        RecyclerView media_view = holder.media_view;

        CardView card_view = holder.card_view;
       feed_pic = dataSet.get(listPosition).getImage();


       /*
        Bundle b=getIntent().getExtras();

         postImageView=b.getString("imageid");

        Glide.with(mContext).load(postImageView).into(postImageView);*/
        Context contextfeedimage = holder.postImageView.getContext();


        if (feed_pic.isEmpty()){
            holder.postImageView.setVisibility(View.GONE);
            holder.media_view.setVisibility(View.GONE);
        }
        else{
            holder.media_view.setVisibility(View.VISIBLE);
            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext,feed_pic);
            holder.media_view.setHasFixedSize(true);
            holder.media_view.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
            holder.media_view.setAdapter(itemListDataAdapter);
        }

        }


    @Override
    public int getItemCount()  {
        return dataSet.size();
    }

}
