package com.mindzglobal.www.mindz.Follow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.FriendRequest.RequestFragment;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Profile.OtherProfileActivity;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ashu on 3/17/2018.
 */

public class FollowCustomAdpater extends RecyclerView.Adapter<FollowCustomAdpater.MyViewHolder> {

    private ArrayList<GetRecomUser> dataSet;

    private Dialog rankDialog;
    String token;
    View root;
    SessionManager manager;
    String user_id,f_status;
    RetrofitClient retrofitClient;


    String followCheck;
    private  int lastPosition=-1;
    Activity mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView user_name;
        TextView category;
        TextView area;
        TextView userid;
        CircleImageView imageView;
        LinearLayout otherprofile;
//        TextView following_recru;
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
            this.otherprofile=itemView.findViewById(R.id.otherprofile);

//            this.following_recru = (TextView) itemView.findViewById(R.id.followed_recru);
            this.follow_recru = itemView.findViewById(R.id.follow_recru);


        }
    }

    public FollowCustomAdpater(ArrayList<GetRecomUser> data, Activity mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    @Override
    public FollowCustomAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_follow_list, parent, false);


        view.setOnClickListener(RequestFragment.myOnClickListener);

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
        LinearLayout otherprofile=holder.otherprofile;


//        TextView following_recru = holder.following_recru;
         final TextView follow_recru = holder.follow_recru;

        name.setText(dataSet.get(listPosition).getName());
        user_name.setText(dataSet.get(listPosition).getUserName());
        category.setText(dataSet.get(listPosition).getProfileCategory());
        area.setText(dataSet.get(listPosition).getArea());
        userid.setText(dataSet.get(listPosition).getUserId());

        String picUrl = dataSet.get(listPosition).getProfilePic();

        String followstatus = dataSet.get(listPosition).getFollowStatus();

        Log.e("followstatusid",followstatus);

        Glide.with(mContext)
                .load(Config.BASE_URL_MEDIA + picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        if(followstatus.contains("0")){
            holder.follow_recru.setText("Follow");
            holder.follow_recru.setTextColor(Color.parseColor("#FFFFFF")); // Not following
        }
        else if(followstatus.contains("1")){
            holder.follow_recru.setText("Following");
            holder.follow_recru.setTextColor(Color.parseColor("#FFFFFF")); // Already Following
        }

        holder.follow_recru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followCheck = holder.follow_recru.getText().toString();

                switch (followCheck){
                    case"Follow":
                        holder.follow_recru.setText("Following");
                        holder.follow_recru.setTextColor(Color.parseColor("#FFFFFF"));
                        followStatus(userid.getText().toString());
                        break;
                    case"Following":
                        holder.follow_recru.setText("Follow");
                        holder.follow_recru.setTextColor(Color.parseColor("#FFFFFF"));
                        followStatus(userid.getText().toString());
                        break;
                }
            }
        });

        holder.otherprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_id = String.valueOf(dataSet.get(listPosition).getUserId());
                followCheck = holder.follow_recru.getText().toString();
                Intent des = new Intent(v.getContext(), OtherProfileActivity.class);
//
                des.putExtra("user_id",user_id);
                des.putExtra("follow_status",followCheck);

                Log.e("check",followCheck);

                v.getContext().startActivity(des);

            }
        });

        setAnimation(holder.itemView,listPosition);

    }

    public void following(String s,final FollowCustomAdpater.MyViewHolder holder){
//        rankDialog = new Dialog(, R.style.AppTheme);
        rankDialog = new Dialog(root.getContext(),R.style.AppTheme);
        rankDialog.setContentView(R.layout.following_dialog);
        rankDialog.setCancelable(true);
        TextView textView = (TextView) rankDialog.findViewById(R.id.rank_dialog_name);
        Button cancel = (Button) rankDialog.findViewById(R.id.rank_dialog_cancel);
        Button unfollow = (Button) rankDialog.findViewById(R.id.rank_dialog_unfollow);
        textView.setText("Unfollow :"+s);
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
//                holder.following_recru.setVisibility(View.GONE);
//                holder.follow_recru.setVisibility(View.VISIBLE);
//                i = Integer.parseInt(holder.follow_count.getText().toString());
//                i = i - 1;
//                holder.follow_count.setText(""+i);
//                followStatus(token,holder.userid.getText().toString(),"UnFollow");
                rankDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
        rankDialog.show();

    }

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