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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllImageFragment extends Fragment {

    String token;
    SessionManager manager;
    RetrofitClient retrofitClient;

    private  static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public AllImageFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_image, container, false);

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

}
