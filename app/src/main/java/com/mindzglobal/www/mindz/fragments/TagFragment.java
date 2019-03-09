package com.mindzglobal.www.mindz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.CrimeCustomAdpater;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.feedscrimepojo;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment {


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String token,user_pic;
    SearchFriendsAdapter searchFriendsAdapter;
    SessionManager manager;
    RetrofitClient retrofitClient;
    RelativeLayout emptyElement;
    ProgressBar bar;
    public TagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tag, container, false);


        emptyElement = view.findViewById(R.id.empty_element);

        recyclerView=view.findViewById(R.id.recycle_friends);

        bar = view.findViewById(R.id.progressBar);

        retrofitClient = new RetrofitClient(token);

        manager = new SessionManager();

        token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);

        user_pic = manager.getPreferences(getActivity(), Constants.USER_PROFILE_PIC);

        linearLayoutManager=new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setHasFixedSize(true);


        final ImageView imgView= view.findViewById(R.id.imageView);

        Glide.with(this)
                .load(R.drawable.binoculars)
                .asGif()
                .animate(R.drawable.binoculars)
                .crossFade()
                .fitCenter()
                .into(imgView);


        allFeeds();

        return view;
    }




    public void allFeeds() {
        bar.setVisibility(View.VISIBLE);
        String url = "https://www.homiezin.com/api/feed/Crime/";

        RetrofitClient.getWithClient().allFeeds(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<feedscrimepojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<feedscrimepojo> call, Response<feedscrimepojo> response) {


                        bar.setVisibility(View.GONE);
                        List<GetFeedx> FeedCrimeList = response.body().getGetFeedx();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(FeedCrimeList.isEmpty()){
                            emptyElement.setVisibility(View.VISIBLE);
                        }

                        else {
                            emptyElement.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            searchFriendsAdapter = new SearchFriendsAdapter((ArrayList<GetFeedx>) FeedCrimeList,getActivity());
                            recyclerView.setAdapter(searchFriendsAdapter);
                        }
                    }
                });
    }

}
