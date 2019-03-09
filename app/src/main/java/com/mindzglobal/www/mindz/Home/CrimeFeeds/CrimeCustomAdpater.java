package com.mindzglobal.www.mindz.Home.CrimeFeeds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Feeds.PostDetailsActivity;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.Image;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;
import tyrantgit.explosionfield.ExplosionField;


public class CrimeCustomAdpater extends RecyclerView.Adapter<CrimeCustomAdpater.MyViewHolder> {

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

        TextView detailsTextView;
        TextView NameTextView;
        TextView dateTextView;
        ImageView postImageView;
        RecyclerView media_view;
        TextView titleusername;
        CircleImageView authorImageView;
        CardView card_view;
        ImageView likesImageView, activelikesImageView;
        TextView posted_id;
        TextView view_count;
        TextView like_count;
        TextView comment_count;
        RelativeLayout on_click_desc;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.detailsTextView = itemView.findViewById(R.id.detailsTextView);
            this.NameTextView = itemView.findViewById(R.id.titleTextView);
            this.titleusername = itemView.findViewById(R.id.titleusername);
            this.dateTextView = itemView.findViewById(R.id.dateTextView);
            this.postImageView = itemView.findViewById(R.id.postImageView);
            this.media_view = itemView.findViewById(R.id.media_view);
            this.authorImageView = itemView.findViewById(R.id.authorImageView);
            this.card_view =  itemView.findViewById(R.id.card_view);
            this.on_click_desc = itemView.findViewById(R.id.on_click_desc);

            this.likesImageView =  itemView.findViewById(R.id.likesImageView);
            this.activelikesImageView =  itemView.findViewById(R.id.activelikesImageView);

            this.posted_id =  itemView.findViewById(R.id.posted_id);
            this.view_count =  itemView.findViewById(R.id.view_count);
            this.like_count =  itemView.findViewById(R.id.like_count);
            this.comment_count =  itemView.findViewById(R.id.comment_count);

        }
    }

    public CrimeCustomAdpater(ArrayList<GetFeedx> data, Activity mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CrimeCustomAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_post_deatil, parent, false);

        manager = new SessionManager();
        token = manager.getPreferences(mContext, Constants.USER_TOKEN);

        view.setOnClickListener(HomeFragment.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CrimeCustomAdpater.MyViewHolder holder, @SuppressLint("RecyclerView") final int listPosition) {
//         ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

        TextView detailsTextView = holder.detailsTextView;
        TextView titleTextView = holder.NameTextView;
        TextView userTextView = holder.titleusername;
        TextView dateTextView = holder.dateTextView;
        ImageView postImageView = holder.postImageView;
        RecyclerView media_view = holder.media_view;

        CardView card_view = holder.card_view;
        RelativeLayout on_click_desc = holder.on_click_desc;


        final ImageView likesImageView = holder.likesImageView;
        final ImageView activelikesImageView = holder.activelikesImageView;

        TextView view_count = holder.view_count;
        TextView like_count = holder.like_count;
        TextView comment_count = holder.comment_count;
        TextView posted_id = holder.posted_id;
//        ImageView postImageView = holder.postImageView;
        CircleImageView authorImageView = holder.authorImageView;

       feed_pic = dataSet.get(listPosition).getImage();

        detailsTextView.setText(dataSet.get(listPosition).getStatus());
        titleTextView.setText(dataSet.get(listPosition).getName());
        userTextView.setText(dataSet.get(listPosition).getUsrNam());
        dateTextView.setText(dataSet.get(listPosition).getPdate());
        posted_id.setText(dataSet.get(listPosition).getPostId());
        view_count.setText(String.valueOf(dataSet.get(listPosition).getViewCount()));
        like_count.setText(String.valueOf(dataSet.get(listPosition).getLikeCount()));
        comment_count.setText(String.valueOf(dataSet.get(listPosition).getCommentCount()));
//        media_view.setAdapter(dataSet.get(listPosition).getImg());
//
//
        Context contextfeedimage = holder.postImageView.getContext();

        String picUrl = dataSet.get(listPosition).getProPic();

        Glide.with(mContext)
                .load(Config.BASE_URL_MEDIA + picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(authorImageView);


        if (feed_pic.isEmpty()){
            holder.postImageView.setVisibility(View.GONE);
            holder.media_view.setVisibility(View.GONE);
        }
        else{
            holder.media_view.setVisibility(View.VISIBLE);
            SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext,feed_pic);
            holder.media_view.setHasFixedSize(true);
            holder.media_view.setLayoutManager(new LinearLayoutManager(mContext,0,false));
            holder.media_view.setAdapter(itemListDataAdapter);
        }

//        activeLikeAnim = AnimationUtils.loadAnimation(mContext, R.anim.android_rotate_animation);
//        LikeAnim = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_done);

        mExplosionField = ExplosionField.attach2Window(mContext);

        holder.likesImageView.setOnClickListener(new View.OnClickListener() {  //grey
            @Override
            public void onClick(View view) {
                postView(holder.posted_id.getText().toString(),"LikePost");
                likesImageView.setVisibility(View.GONE);
                mExplosionField.explode(view);
                activelikesImageView.setVisibility(View.VISIBLE);

                int i ;
                    i = Integer.parseInt(holder.like_count.getText().toString());
                    i = i + 1;
                    holder.like_count.setText(""+i);

                reset(activelikesImageView);

            }
        });

        holder.activelikesImageView.setOnClickListener(new View.OnClickListener() { //red
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                postView(holder.posted_id.getText().toString(),"UnLikePost");
                mExplosionField.explode(view);
                activelikesImageView.setVisibility(View.GONE);
                likesImageView.setVisibility(View.VISIBLE);

                int i ;
                    i = Integer.parseInt(holder.like_count.getText().toString());
                    i = i - 1;
                    holder.like_count.setText(""+i);
                reset(likesImageView);
            }
        });



        if(String.valueOf(dataSet.get(listPosition).getMyLike()).contains("1")){
            activelikesImageView.setVisibility(View.VISIBLE);
            likesImageView.setVisibility(View.GONE);


        }

        else if(String.valueOf(dataSet.get(listPosition).getMyLike()).contains("0")){
//            btnCustomCheckBoxLike.setChecked(false);
            activelikesImageView.setVisibility(View.GONE);
            likesImageView.setVisibility(View.VISIBLE);

        }



        holder.on_click_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postView(holder.posted_id.getText().toString(),"ViewPost");

                post_id = String.valueOf(dataSet.get(listPosition).getPostId());
                String post_owner_name = dataSet.get(listPosition).getName();
                String post_owner_username = dataSet.get(listPosition).getUsrNam();
                String post_date = dataSet.get(listPosition).getPdate();
                String post_contents = dataSet.get(listPosition).getStatus();

                List<Image> post_img = dataSet.get(listPosition).getImage();
                String user_profile_img = dataSet.get(listPosition).getProPic();

                Intent i = new Intent (v.getContext(), PostDetailsActivity.class);

                i.putExtra("post_id",post_id);
                i.putExtra("post_owner_name",post_owner_name);
                i.putExtra("post_owner_username",post_owner_username);
                i.putExtra("post_date",post_date);
                i.putExtra("post_contents",post_contents);
                i.putExtra("user_profile_img",user_profile_img);

                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount()  {
        return dataSet.size();
    }

    public void postView(String postid,String Category){
        RetrofitClient.getWithClient().viewLikeComment(postid,Category,"","application/json","Bearer "+token).enqueue(new GlobalCallback<String>(root) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }
        });
    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        }
        else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

}