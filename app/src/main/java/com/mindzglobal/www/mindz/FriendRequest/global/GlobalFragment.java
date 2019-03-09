package com.mindzglobal.www.mindz.FriendRequest.global;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.Followers.AllFollowersListActivity;
import com.mindzglobal.www.mindz.Follow.Following.AllFollowingListActivity;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class GlobalFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    CircleImageView post_prof_img;
    String token, user_pic, usertext, usertitle;
    SessionManager manager;
    RetrofitClient retrofitClient;
    TextView NameTextView;
    TextView titleusername;
    LinearLayout profile_linear;
    LinearLayout followers_layout, following_layout;


    int[] tabIcons = {
            R.drawable.gallry_icon_global,
            R.drawable.video_icon_global,
            R.drawable.view_global
    };

    public GlobalFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dummy_global_frag, container, false);

        profile_linear = view.findViewById(R.id.profile_linear);

        post_prof_img = view.findViewById(R.id.post_profile);
        NameTextView = view.findViewById(R.id.titleTextView);
        titleusername = view.findViewById(R.id.titleusername);

        followers_layout = view.findViewById(R.id.followers_layout);

        following_layout = view.findViewById(R.id.following_layout);

        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);
        user_pic = manager.getPreferences(getActivity(), Constants.USER_PROFILE_PIC);

        usertext = manager.getPreferences(getActivity(), Constants.USER_NAME);
        NameTextView.setText(usertext);

        usertitle = manager.getPreferences(getActivity(), Constants.USER_UN_NAME);
        titleusername.setText(usertitle);


        followers_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_followers = new Intent(getActivity(), AllFollowersListActivity.class);
                startActivity(intent_followers);

            }
        });


        following_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_following = new Intent(getActivity(), AllFollowingListActivity.class);
                startActivity(intent_following);

            }
        });


        new Thread(new Runnable() {
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(Config.BASE_URL_MEDIA + user_pic)
                                .centerCrop()
                                .into(post_prof_img);
                    }
                });
            }
        }).start();

        viewPager = view.findViewById(R.id.viewpager_global);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(pagerAdapter);


        tabLayout = view.findViewById(R.id.tabs_global);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }


        return view;
    }


    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"Tab One", "Tab Two", "Tab Three"};
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
                    return new AllImageFragment();
                case 1:
                    return new AllVideoFragment();
                case 2:
                    return new SettingsFragment();
            }
            return null;
        }
    }


}
