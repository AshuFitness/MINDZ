package com.mindzglobal.www.mindz.FriendRequest.global.global_adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.FollowCustomAdpater;
import com.mindzglobal.www.mindz.FriendRequest.RequestFragment;
import com.mindzglobal.www.mindz.FriendRequest.global.BlockListActivity;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.blocklist.UBlockList;
import com.mindzglobal.www.mindz.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Response;

public class Blocklistadapter extends RecyclerView.Adapter<Blocklistadapter.MyViewHolder> {


    private ArrayList<UBlockList> dataSet;
    Activity mContext;
//    private Dialog rankDialog;
    String token;
    View root;
    SessionManager manager;
    String user_id,f_status;
    RetrofitClient retrofitClient;

    String followCheck;
//    private  int lastPosition=-1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView blockusername,blockName;
        CircleImageView block_imageView;
        CardView card_block;
        TextView userid;
        FButton unblock;


//        RecyclerView media_view;
//        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.blockusername = itemView.findViewById(R.id.blockusername);
            this.blockName = itemView.findViewById(R.id.blockName);
            this.userid = itemView.findViewById(R.id.userid);
            this.block_imageView = itemView.findViewById(R.id.block_imageView);
            this.card_block=itemView.findViewById(R.id.card_block);
            this.unblock=itemView.findViewById(R.id.unblock);

        }
    }

    public Blocklistadapter(ArrayList<UBlockList> data, Activity mContext) {
        this.dataSet = data;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public Blocklistadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_adapter_layout, parent, false);


        view.setOnClickListener(BlockListActivity.myOnClickListener);

        manager = new SessionManager();
        token = manager.getPreferences(mContext, Constants.USER_TOKEN);
        retrofitClient = new RetrofitClient(token);
        Blocklistadapter.MyViewHolder myViewHolder = new Blocklistadapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Blocklistadapter.MyViewHolder holder, final int listPosition) {


        TextView area = holder.blockusername;
        TextView name = holder.blockName;
        CircleImageView imageView = holder.block_imageView;
        CardView cardView=holder.card_block;
        final TextView userid = holder.userid;

        final FButton fButton_unblock=holder.unblock;

        userid.setText(dataSet.get(listPosition).getUserId());
        String picUrl = dataSet.get(listPosition).getProfilePic();
        name.setText(dataSet.get(listPosition).getName());

        Log.e("name",String.valueOf(name));

        area.setText(dataSet.get(listPosition).getUserName());
        Glide.with(mContext)
                .load(Config.BASE_URL_MEDIA + picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);



        fButton_unblock.setText("Unblock");
        fButton_unblock.setTextColor(Color.parseColor("#FFFFFF"));

//        if(followstatus.contains("0")){
//            holder.follow_recru.setText("Follow");
//            holder.follow_recru.setTextColor(Color.parseColor("#8B0000")); // Not following
//
//        }
//        else if(followstatus.contains("1")){
//            holder.follow_recru.setText("Following");
//            holder.follow_recru.setTextColor(Color.parseColor("#006400")); // Already Following
//        }

        final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.clk);
        fButton_unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.remove(listPosition);
                notifyItemRemoved(listPosition);
                notifyItemRangeChanged(listPosition,dataSet.size());
                mp.start();
                // Show the removed item label
                Toast.makeText(mContext,"Unblocked : "+holder.blockName.getText().toString(),Toast.LENGTH_SHORT).show();
                blockStatus(userid.getText().toString());

            }
        });

//        setAnimation(holder.itemView,listPosition);


    }



    private void blockStatus(final String followId_block) {

        RetrofitClient.getWithClient().blockother(followId_block,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<String>(root) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String blockStatus = response.body().toString();

                Log.e("blockstatus", blockStatus);

//                if(blockStatus.contains("0")){
//                    block_other.setText("Block");
//                    block_other.setTextColor(Color.parseColor("#000000")); // Not following
//
//                }
//                else if(blockStatus.contains("1")){
//                    block_other.setText("Blocked");
//                    block_other.setTextColor(Color.parseColor("#000000")); // Already Following
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
