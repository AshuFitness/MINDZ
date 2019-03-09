package com.mindzglobal.www.mindz.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.MyViewHolder> {
    private ArrayList<GetFeedx> dataSet;
    Activity mContext;
    SessionManager manager;
    String token;

    public SearchFriendsAdapter(ArrayList<GetFeedx> dataSet, Activity mContext) {
        this.dataSet = dataSet;
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public SearchFriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friends_layout, parent, false);

        manager = new SessionManager();
        token = manager.getPreferences(mContext, Constants.USER_TOKEN);


        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchFriendsAdapter.MyViewHolder holder, int position) {

        TextView name = holder.name;
        final  ImageView add_tag = holder.add_tag;
        final  ImageView added_tag = holder.added_tag;
        CircleImageView authorImageView = holder.circularImageView;


        holder.add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                added_tag.setVisibility(View.VISIBLE);
                add_tag.setVisibility(View.GONE);
            }
        });


        name.setText(dataSet.get(position).getName());

        String picUrl = dataSet.get(position).getProPic();

        Glide.with(mContext)
                .load(Config.BASE_URL_MEDIA + picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(authorImageView);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circularImageView;
        ImageView add_tag,added_tag;
        TextView name;


        public MyViewHolder(View itemView) {
            super(itemView);

            circularImageView=itemView.findViewById(R.id.tag_frnd_img);
            name=itemView.findViewById(R.id.friends_list_names);
            add_tag=itemView.findViewById(R.id.add_tagg);
            added_tag=itemView.findViewById(R.id.added_tagg);


        }
    }
}
