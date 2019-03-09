package com.mindzglobal.www.mindz.FriendRequest.global;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;

import retrofit2.Response;

public  class OtherImageFragment extends Fragment {

    String token;
    SessionManager manager;
    RetrofitClient retrofitClient;

    private  static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public OtherImageFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_image_other, container, false);

        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);


        mSwipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                allFeeds();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        recyclerView = view.findViewById(R.id.recycler_image);
        recyclerView.setHasFixedSize(true);
//        myOnClickListener = new MyOnClickListener(getActivity());
        layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//

        return view;
    }



//    private void prepareImagesData(String strtext) {
//
////        blur_reg1.setVisibility(View.VISIBLE);
//
//        RetrofitClient.getWithClient().InfraImageGallery(strtext,"Infrastructure","Image","application/json","Bearer "+token)
//                .enqueue(new GlobalCallback<StaffPojo>(recyclerView) {
//                    @Override
//                    public void onResponse(Call<StaffPojo> call, Response<StaffPojo> response) {
//
//                        blur_reg1.setVisibility(View.GONE);
//                        List<GalleryList> staffgalleryList = response.body().getGalleryList();
//
//
//                        if(staffgalleryList.isEmpty()){
//                            empty_element.setVisibility(View.VISIBLE);
//                        }
//
//                        else {
//                            adapter = new OtherImageAdapter((ArrayList<String>) staffgalleryList,getActivity());
//                            recyclerView.setAdapter(adapter);
//
//                        }
//                    }
//                });
//
//
//    }



}
