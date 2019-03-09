package com.mindzglobal.www.mindz.NotificationCenter;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.Notification.NotificationList;
import com.mindzglobal.www.mindz.Model.Notification.NotifyPojo;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Wave on 11/10/2017.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeContainer;
    SessionManager manager;
    String empId;
    ListView listView;
    ImageView emptyElement;

    ProgressBar bar;

    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        manager = new SessionManager();
//        empId = manager.getPreferences(NotificationActivity.this, Constants.EmpID);
        token = manager.getPreferences(this, Constants.USER_TOKEN);


        listView =  findViewById(R.id.list_item);
        bar =  findViewById(R.id.progressBar);
        emptyElement =  findViewById(R.id.emptyElement);
        swipeContainer =  findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NotificationsCenter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);

                    }
                }, 2500);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark);

        bar.setVisibility(View.VISIBLE);
        NotificationsCenter();
    }

    @Override
    public void onClick(View view) {

    }

    public void NotificationsCenter() {
        String url = "https://www.homiezin.com/api/notification/" ;
        RetrofitClient.getWithClient().getnotification(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<NotifyPojo>(listView) {
            @Override
            public void onResponse(Call<NotifyPojo> call, Response<NotifyPojo> response) {

//                Log.e("NotificationResp", response.body().toString());

                List<NotificationList> allnotifylist = response.body().getNotificationList();

                if (allnotifylist.isEmpty()){
                    bar.setVisibility(View.GONE);
                    emptyElement.setVisibility(View.VISIBLE);
                }else {
                    emptyElement.setVisibility(View.GONE);
                    NotificationCustomListAdapter notificationList = new NotificationCustomListAdapter(NotificationActivity.this, allnotifylist);
                    listView.setAdapter(notificationList);
                    listView.setEmptyView(findViewById(R.id.emptyElement));
                    bar.setVisibility(View.GONE);
                }

            }
        });
    }

}