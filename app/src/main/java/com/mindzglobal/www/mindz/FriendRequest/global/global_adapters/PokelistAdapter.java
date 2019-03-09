package com.mindzglobal.www.mindz.FriendRequest.global.global_adapters;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;


public class PokelistAdapter  extends RecyclerView.Adapter<PokelistAdapter.MyViewHolder> {



    private ArrayList<GetRecomUser> dataSet;
    Activity mContext;
    private Dialog rankDialog;
    String token;
    View root;
    SessionManager manager;
    String user_id,f_status;
    RetrofitClient retrofitClient;


    String followCheck;
    private  int lastPosition=-1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView pokeArea,pokeName;
        CircleImageView poke_imageView;
        CardView poke_card;
//        TextView userid;





//        RecyclerView media_view;
//        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.pokeArea = itemView.findViewById(R.id.pokeArea);
            this.pokeName = itemView.findViewById(R.id.pokeName);
//            this.userid = itemView.findViewById(R.id.userid);
            this.poke_imageView = itemView.findViewById(R.id.poke_imageView);
            this.poke_card=itemView.findViewById(R.id.poke_card);

        }
    }








    @NonNull
    @Override
    public PokelistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PokelistAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
