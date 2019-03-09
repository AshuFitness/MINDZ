package com.mindzglobal.www.mindz.Follow;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.FilterSearch.FilterSearchActivity;
import com.mindzglobal.www.mindz.Model.FollowListPojo.Followpojo;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.UserNamepojo.UserName;
import com.mindzglobal.www.mindz.Model.UserNamepojo.UserNameListPojo;
import com.mindzglobal.www.mindz.Profile.OtherProfileActivity;
import com.mindzglobal.www.mindz.R;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import custom_font.MyTextView;
import retrofit2.Call;
import retrofit2.Response;

import static com.mindzglobal.www.mindz.Model.RetrofitClient.token;

/**
 * Created by Ashu on 3/19/2018.
 */

public class FollowActivity extends AppCompatActivity implements View.OnClickListener {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;
    SessionManager manager;
    RetrofitClient retrofitClient;


    @Bind(R.id.autocomplete)
    PlacesAutocompleteTextView mAutocomplete;

    @Bind(R.id.state)
    TextView mState;

    @Bind(R.id.city)
    TextView mCity;

    @Bind(R.id.area)
    TextView mArea;

    @Bind(R.id.country)
    TextView mCountry;

    @Bind(R.id.choose_cat)
    MyTextView mchoose_cat;

    @Bind(R.id.address_lay)
    LinearLayout maddress_lay;

    @Bind(R.id.auto_userName)
    AutoCompleteTextView mAuto_userName;

    @Bind(R.id.submit)
    Button mSubmit;

    @Bind(R.id.recycler_filter)
    RelativeLayout mRecycler_filter;

    @Bind(R.id.filter_view)
    LinearLayout mFilter_view;

    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;

    private List<UserName> userNameList;




    RelativeLayout filtersearch;

    String userName,CounTry,StaTe,CiTy,AreA,CateGory;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressBar bar;

    Dialog dialog;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list);
        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(FollowActivity.this, Constants.USER_TOKEN);

        userName = manager.getPreferences(FollowActivity.this,Constants.USER_NAME);
        CounTry = manager.getPreferences(FollowActivity.this,Constants.USER_COUNTRY);
        StaTe = manager.getPreferences(FollowActivity.this,Constants.USER_STATE);
        CiTy = manager.getPreferences(FollowActivity.this,Constants.USER_CITY);
        AreA = manager.getPreferences(FollowActivity.this,Constants.USER_AREA);
        CateGory = manager.getPreferences(FollowActivity.this,Constants.USER_PRO_CAT);

        filtersearch = findViewById(R.id.toolbar);
        filtersearch.setOnClickListener(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new FollowActivity.MyOnClickListener(FollowActivity.this);
        layoutManager = new LinearLayoutManager(FollowActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bar = findViewById(R.id.progressBar);

        Follows();

        mSwipeRefreshLayout = findViewById(R.id.swipeContainerfollow);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Follows();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    public void Follows() {
        bar.setVisibility(View.VISIBLE);


        RetrofitClient.getWithClient().allFollowList("",CounTry,StaTe,CiTy,AreA,CateGory,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<Followpojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<Followpojo> call, Response<Followpojo> response) {

				    Log.e("FollowListResponse", response.body().toString());

                        bar.setVisibility(View.GONE);
                        List<GetRecomUser> FollowList = response.body().getGetRecomUser();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(FollowList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
                        }

                        else {
                            adapter = new FollowCustomAdpater((ArrayList<GetRecomUser>) FollowList,FollowActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.toolbar:

                Intent filter = new Intent(FollowActivity.this, FilterSearchActivity.class);
                startActivity(filter);
        }

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
