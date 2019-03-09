package com.mindzglobal.www.mindz.Home.Others;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Feeds.BaseActivity;
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

public class OthersActivity extends BaseActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    ProgressBar bar;

    String token;
    SessionManager manager;
    RetrofitClient retrofitClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_layout);

        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(OthersActivity.this, Constants.USER_TOKEN);

        recyclerView = findViewById(R.id.recycler_crime);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new OthersActivity.MyOnClickListener(OthersActivity.this);
        layoutManager = new LinearLayoutManager(OthersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bar = findViewById(R.id.progressBar);

        CrimeFeeds();

        mSwipeRefreshLayout = findViewById(R.id.swipeContainercrime);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CrimeFeeds();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void CrimeFeeds() {
        bar.setVisibility(View.VISIBLE);
        String url = "https://www.homiezin.com/api/feed/Other/";

        RetrofitClient.getWithClient().allFeeds(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<feedscrimepojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<feedscrimepojo> call, Response<feedscrimepojo> response) {

//				Log.e("FeedsResponse", response.body().toString());

                        bar.setVisibility(View.GONE);
                        List<GetFeedx> FeedCrimeList = response.body().getGetFeedx();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(FeedCrimeList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
                        }

                        else {
                            adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList,OthersActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }


    public  class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
//			Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
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
