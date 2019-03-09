package com.mindzglobal.www.mindz.FriendRequest.global;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.FriendRequest.RequestFragment;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import static com.mindzglobal.www.mindz.Model.RetrofitClient.token;

public class GalleryOtherActivity extends AppCompatActivity {



//    private static RecyclerView.Adapter adapter;
//    private static RecyclerView recyclerView;
//    public static View.OnClickListener myOnClickListener;
//    private RecyclerView.LayoutManager layoutManager;
//    SessionManager manager;
//    RetrofitClient retrofitClient;
//
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar bar;

    TabLayout tabLayout;
    ViewPager viewPager;


    int[] tabIcons = {
            R.drawable.gallry_icon_global,
            R.drawable.video_icon_global,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_other);

        viewPager =  findViewById(R.id.viewpager_othergallery);
        GalleryOtherActivity.PagerAdapter pagerAdapter = new GalleryOtherActivity.PagerAdapter(getSupportFragmentManager(), GalleryOtherActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout =  findViewById(R.id.tabs_othergallery);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        }

    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"Tab One", "Tab Two"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new OtherImageFragment();
                case 1:
                    return new OtherVideoFragment();
            }
            return null;
        }
    }

}



//        retrofitClient = new RetrofitClient(token);
//        manager = new SessionManager();
//        token = manager.getPreferences(GalleryOtherActivity.this, Constants.USER_TOKEN);
//
//        recyclerView = findViewById(R.id.my_recycler_view);
//        recyclerView.setHasFixedSize(true);
////        myOnClickListener = new FollowActivity.MyOnClickListener(FollowActivity.this);
//        layoutManager = new LinearLayoutManager(GalleryOtherActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        bar = findViewById(R.id.progressBar);
//
////        Follows();
//
//        mSwipeRefreshLayout = findViewById(R.id.swipeContainerfollow);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                Follows();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });