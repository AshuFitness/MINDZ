package com.mindzglobal.www.mindz.FriendRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.FollowActivity;
import com.mindzglobal.www.mindz.Follow.FollowCustomAdpater;
import com.mindzglobal.www.mindz.Model.FollowListPojo.Followpojo;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Profile.OtherProfileActivity;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.mindzglobal.www.mindz.Model.RetrofitClient.token;

/**
 * Created by Ashu on 3/23/2018.
 */

public class RequestFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    private static RecyclerView.Adapter adapter1;
    private static RecyclerView recyclerView1;
    public static View.OnClickListener myOnClickListener1;
    private RecyclerView.LayoutManager layoutManager1;

    SessionManager manager;
    RetrofitClient retrofitClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    String userName, CounTry, StaTe, CiTy, AreA, CateGory;
//    ProgressBar bar;

    View rootView;
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_request, container, false);
        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);


        userName = manager.getPreferences(getActivity(), Constants.USER_NAME);
        CounTry = manager.getPreferences(getActivity(), Constants.USER_COUNTRY);
        StaTe = manager.getPreferences(getActivity(), Constants.USER_STATE);
        CiTy = manager.getPreferences(getActivity(), Constants.USER_CITY);
        AreA = manager.getPreferences(getActivity(), Constants.USER_AREA);
        CateGory = manager.getPreferences(getActivity(), Constants.USER_PRO_CAT);

//        bar = rootView.findViewById(R.id.progressBar);

        recyclerView = rootView.findViewById(R.id.recycler_suggestion);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new MyOnClickListener(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        ----------------------------------------------------------------------

        recyclerView1 = rootView.findViewById(R.id.recycler_requests);
        recyclerView1.setHasFixedSize(true);
        myOnClickListener1 = new MyOnClickListener1(getActivity());
        layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        Follows();

        FollowsRequest();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeContainerfollow);

//        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeContainerfollow);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Follows();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });

        return rootView;
    }


    public void Follows() {
//        bar.setVisibility(View.VISIBLE);
        RetrofitClient.getWithClient().allFollowList("", CounTry, StaTe, CiTy, AreA, CateGory, "application/json", "Bearer " + token)
                .enqueue(new GlobalCallback<Followpojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<Followpojo> call, Response<Followpojo> response) {
//                          bar.setVisibility(View.GONE);
                        List<GetRecomUser> FollowList = response.body().getGetRecomUser();
//                          adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                          recyclerView.setAdapter(adapter);
                        if (FollowList.isEmpty()) {
//					        emptyElement.setVisibility(View.VISIBLE);
                        } else {
                            adapter = new FollowCustomAdpater((ArrayList<GetRecomUser>) FollowList, getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }


    public void FollowsRequest() {
//        bar.setVisibility(View.VISIBLE);
        RetrofitClient.getWithClient().allFollowList("", CounTry, StaTe, CiTy, AreA, CateGory, "application/json", "Bearer " + token)
                .enqueue(new GlobalCallback<Followpojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<Followpojo> call, Response<Followpojo> response) {
//                          bar.setVisibility(View.GONE);
                        List<GetRecomUser> FollowList = response.body().getGetRecomUser();
//                          adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                          recyclerView.setAdapter(adapter);
                        if (FollowList.isEmpty()) {
//					        emptyElement.setVisibility(View.VISIBLE);
                        } else {
                            adapter1 = new FollowCustomAdpater((ArrayList<GetRecomUser>) FollowList, getActivity());
                            recyclerView1.setAdapter(adapter1);
                        }
                    }
                });
    }

    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Following list clicked!!!!!!!", Toast.LENGTH_SHORT).show();

//         int selectedItemPosition = recyclerView.getChildPosition(v);
//         RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//		   TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);

//		   Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();

//         Intent des = new Intent(getActivity(), OtherProfileActivity.class);

//         des.putExtra("user_id",user_id);

//	       checkJobView(jobId.getText().toString(),emp);
//	       des.putExtra("jobTitle",jobtitle.getText().toString());
//         startActivity(des);

        }
    }

    public class MyOnClickListener1 implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener1(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Friend Request clicked!!!", Toast.LENGTH_SHORT).show();

//          int selectedItemPosition = recyclerView.getChildPosition(v);
//          RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//			TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);

//			Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();

//          Intent des = new Intent(getActivity(), OtherProfileActivity.class);

//          des.putExtra("user_id",user_id);

//			checkJobView(jobId.getText().toString(),emp);
//			des.putExtra("jobTitle",jobtitle.getText().toString());
//          startActivity(des);

        }
    }
}