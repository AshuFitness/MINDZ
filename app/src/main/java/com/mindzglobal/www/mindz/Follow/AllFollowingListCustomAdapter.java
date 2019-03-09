package com.mindzglobal.www.mindz.Follow;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.Model.AllFollowListPojo.FollowList;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ashu on 3/21/2018.
 */

public class AllFollowingListCustomAdapter extends RecyclerView.Adapter<FollowCustomAdpater.MyViewHolder> {

    private ArrayList<FollowList> dataSet;
    Activity mContext;
    private Dialog rankDialog;
    String token;
    View root;
    SessionManager manager;
    RetrofitClient retrofitClient;

    private int lastPosition=-1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView user_name;
        TextView category;
        TextView area;
        TextView userid;
        CircleImageView imageView;

        TextView follow_recru;



//        RecyclerView media_view;
//        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.category = itemView.findViewById(R.id.category);
            this.area = itemView.findViewById(R.id.area);
            this.userid = itemView.findViewById(R.id.userid);
            this.imageView = itemView.findViewById(R.id.imageView);

            this.follow_recru = (TextView) itemView.findViewById(R.id.follow_recru);

        }
    }

    public AllFollowingListCustomAdapter(ArrayList<FollowList> data, Activity mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    @Override
    public FollowCustomAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_follow_list, parent, false);

        view.setOnClickListener(HomeFragment.myOnClickListener);

        manager = new SessionManager();
        token = manager.getPreferences(mContext, Constants.USER_TOKEN);
        retrofitClient = new RetrofitClient(token);
        FollowCustomAdpater.MyViewHolder myViewHolder = new FollowCustomAdpater.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final FollowCustomAdpater.MyViewHolder holder, final int listPosition) {

        TextView name = holder.name;
        TextView user_name = holder.user_name;
        TextView category = holder.category;
        TextView area = holder.area;
        final TextView userid = holder.userid;
        CircleImageView imageView = holder.imageView;

        TextView follow_recru = holder.follow_recru;




        name.setText(dataSet.get(listPosition).getName());
        user_name.setText(dataSet.get(listPosition).getUserName());
        category.setText(dataSet.get(listPosition).getProfileCategory());
        area.setText(dataSet.get(listPosition).getArea());
        userid.setText(dataSet.get(listPosition).getUserId());
        String picUrl = dataSet.get(listPosition).getProfilePic();
//        String followstatus = dataSet.get(listPosition).getFollowStatus();
//        Log.e("followstatusid",followstatus);


        Glide.with(mContext)
                .load(Config.BASE_URL_MEDIA + picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        holder.follow_recru.setText("Following");
        holder.follow_recru.setTextColor(Color.parseColor("#FFFFFF"));

//        if(followstatus.contains("0")){
//            holder.follow_recru.setText("Follow");
//            holder.follow_recru.setTextColor(Color.parseColor("#8B0000")); // Not following
//
//        }
//        else if(followstatus.contains("1")){
//            holder.follow_recru.setText("Following");
//            holder.follow_recru.setTextColor(Color.parseColor("#006400")); // Already Following
//        }


        holder.follow_recru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.remove(listPosition);
                notifyItemRemoved(listPosition);
                notifyItemRangeChanged(listPosition,dataSet.size());

                // Show the removed item label
                Toast.makeText(mContext,"Removed : "+holder.user_name.getText().toString(),Toast.LENGTH_SHORT).show();
                        followStatus(userid.getText().toString());

            }
        });

        setAnimation(holder.itemView,listPosition);

    }

//    public void followStatus(String followId){
//        Log.e("followId",followId);
//        RetrofitClient.getWithClient().folloStatus(followId,"application/json","Bearer"+token)
//                .enqueue(new GlobalCallback<String>(root) {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        Log.e("responsefollow", response.toString());
//                        Log.e("followid", response.body().toString());
//                    }
//                });
//    }

    public void followStatus(String followId){
        Log.e("followId",followId);
        RetrofitClient.getWithClient().folloStatus(followId,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(root) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("responsefollow", response.toString());
                Log.e("followid", response.body().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition )
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}