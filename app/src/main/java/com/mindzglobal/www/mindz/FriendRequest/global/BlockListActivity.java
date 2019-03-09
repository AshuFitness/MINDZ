package com.mindzglobal.www.mindz.FriendRequest.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.FollowActivity;
import com.mindzglobal.www.mindz.Follow.FollowCustomAdpater;
import com.mindzglobal.www.mindz.FriendRequest.global.global_adapters.Blocklistadapter;
import com.mindzglobal.www.mindz.Model.FollowListPojo.Followpojo;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.blocklist.BlocklistPojo;
import com.mindzglobal.www.mindz.Model.blocklist.UBlockList;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.mindzglobal.www.mindz.Model.RetrofitClient.token;

public class BlockListActivity extends AppCompatActivity implements View.OnClickListener {


    private static RecyclerView.Adapter adapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;
    SessionManager manager;
    RetrofitClient retrofitClient;
    Context context;
    RelativeLayout filtersearch;

    String userid;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressBar bar;
    String token;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_list_layout);
        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(BlockListActivity.this, Constants.USER_TOKEN);

//        filtersearch = findViewById(R.id.toolbar);
//        filtersearch.setOnClickListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new MyOnClickListener(BlockListActivity.this);
        layoutManager = new GridLayoutManager(BlockListActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bar = findViewById(R.id.progressBar);

//        Bundle bundle = getIntent().getExtras();
//        userid  = bundle.getString("user_id");

//        Follows();

        mSwipeRefreshLayout = findViewById(R.id.swipeContainerfollow);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               blocklist();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        blocklist();
    }



    public void blocklist() {
//        bar.setVisibility(View.VISIBLE);

        String url = "https://www.homiezin.com/api/blockuser-list/" ;

        RetrofitClient.getWithClient().allblockList(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<BlocklistPojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<BlocklistPojo> call, Response<BlocklistPojo> response) {

                        Log.e("BlockResponseAll", response.body().toString());

                        bar.setVisibility(View.GONE);
                        List<UBlockList> FollowList = response.body().getUBlockList();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(FollowList.isEmpty()){

//					emptyElement.setVisibility(View.VISIBLE);
                        }


                        else {

                            adapter = new Blocklistadapter((ArrayList<UBlockList>) FollowList,BlockListActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }





    @Override
    public void onClick(View v) {

    }


    public static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        public MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "clicked to follow list", Toast.LENGTH_SHORT).show();
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
