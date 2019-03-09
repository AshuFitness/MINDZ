package com.mindzglobal.www.mindz.Follow.Followers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.FilterSearch.FilterSearchActivity;
import com.mindzglobal.www.mindz.Follow.AllFollowingListCustomAdapter;
import com.mindzglobal.www.mindz.Model.AllFollowListPojo.Allfollowlistpojo;
import com.mindzglobal.www.mindz.Model.AllFollowListPojo.FollowList;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.mindzglobal.www.mindz.Model.RetrofitClient.token;

/**
 * Created by Ashu on 3/22/2018.
 */

public class AllFollowersListActivity extends AppCompatActivity implements View.OnClickListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;
    SessionManager manager;
    RetrofitClient retrofitClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressBar bar;

    RelativeLayout filtersearch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followerlist_layout);


        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(AllFollowersListActivity.this, Constants.USER_TOKEN);

        filtersearch = findViewById(R.id.toolbar);
        filtersearch.setOnClickListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new AllFollowersListActivity.MyOnClickListener(AllFollowersListActivity.this);
        layoutManager = new LinearLayoutManager(AllFollowersListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bar = findViewById(R.id.progressBar);

        FollowList();

        mSwipeRefreshLayout = findViewById(R.id.swipeContainerfollow);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FollowList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }




    public void FollowList() {
        bar.setVisibility(View.VISIBLE);
        String url = "https://www.homiezin.com/api/follower-list/";

        RetrofitClient.getWithClient().AllMemberFollowList(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<Allfollowlistpojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<Allfollowlistpojo> call, Response<Allfollowlistpojo> response) {
                        Log.e("MemberServerResponse", response.toString());
                        Log.e("MemberFollowerResponse", response.body().toString());

                        bar.setVisibility(View.GONE);
                        List<FollowList> AllMemberFollowList = response.body().getFollowList();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(AllMemberFollowList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
                        }

                        else {
                            adapter = new AllFollowingListCustomAdapter((ArrayList<FollowList>) AllMemberFollowList,AllFollowersListActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.toolbar:
                Intent filter = new Intent(AllFollowersListActivity.this, FilterSearchActivity.class);
                startActivity(filter);
        }
    }


    public  class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
			Toast.makeText(context, "Follower Clicked!!", Toast.LENGTH_SHORT).show();
//			int selectedItemPosition = recyclerView.getChildPosition(v);
//			RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//			TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);
//			TextView companyName = (TextView) viewHolder.itemView.findViewById(R.id.company_name);
//			TextView experience = (TextView) viewHolder.itemView.findViewById(R.id.experience);
//			TextView location = (TextView) viewHolder.itemView.findViewById(R.id.location);
//			TextView no_position = (TextView) viewHolder.itemView.findViewById(R.id.no_position);


//			Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();
//			Intent des = new Intent(JobsAllActivity.this,JobDiscription.class);
//			checkJobView(jobId.getText().toString(),emp);
//			des.putExtra("jobTitle",jobtitle.getText().toString());
//			des.putExtra("companyName",companyName.getText().toString());
//			des.putExtra("experience",experience.getText().toString());
//			des.putExtra("location",location.getText().toString());
//			startActivity(des);
        }
    }

}
