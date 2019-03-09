package com.mindzglobal.www.mindz;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mindzglobal.www.mindz.ChangePassword.ChangePasswordActivity;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.CropMain.CropMainActivity;
import com.mindzglobal.www.mindz.Feeds.BaseActivity;
import com.mindzglobal.www.mindz.FilterSearch.FilterSearchActivity;
import com.mindzglobal.www.mindz.Follow.FollowActivity;
import com.mindzglobal.www.mindz.Follow.Followers.AllFollowersListActivity;
import com.mindzglobal.www.mindz.Follow.Following.AllFollowingListActivity;
import com.mindzglobal.www.mindz.FriendRequest.RequestFragment;
import com.mindzglobal.www.mindz.FriendRequest.global.GlobalFragment;
import com.mindzglobal.www.mindz.FriendRequest.global.SettingsFragment;
import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.NotificationCenter.NotificationActivity;
import com.mindzglobal.www.mindz.fragments.GuidePageActivity2;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    int[] tabIcons = {  R.drawable.house_homepage,
                        R.drawable.request_homepage,
                        R.drawable.drawer_homepage };
    TabLayout tabLayout;
    SessionManager manager;

    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    boolean doubleBackToExitPressedOnce = false;

    String FireBaseToken,token;

    Dialog dialog;
    View root;
    Activity context;

    @Bind(R.id.content)
    CoordinatorLayout clContent;
    private FloatingActionButton floatingActionButton;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.layout_fab1) LinearLayout fab1;
    @Bind(R.id.layout_fab3) LinearLayout fab3;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private static final int CustomGallerySelectId = 1;//Set Intent Id
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301,VIDEO_CODE = 401,CAPTURE_VIDEO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new SessionManager();
        ButterKnife.bind(this);

        token = manager.getPreferences(MainActivity.this, Constants.USER_TOKEN);
        FireBaseToken = FirebaseInstanceId.getInstance().getToken();

        tokenmainn();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab3.setOnClickListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }
        checkAndRequestPermissions();
    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int storagewritePermission = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (storagewritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


        public void tokenmainn() {

        RetrofitClient.getWithClient().updatetoken(FireBaseToken, "application/json", "Bearer "+token)
                .enqueue(new GlobalCallback<String>(root) {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        String takeRes = response.body().toString();

                    }
                });
    }



            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public void showFloatButtonRelatedSnackBar(int messageId) {
        showSnackBar(floatingActionButton, messageId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menulogout:
              logoutdialog();
                break;


            case R.id.menunotify:
                Intent nss = new Intent(this, NotificationActivity.class);
                startActivity(nss);
                break;

            case R.id.menuchangepass:
                Intent pss = new Intent(this, ChangePasswordActivity.class);
                startActivity(pss);
                break;

            case R.id.menusearch:
                Intent psss = new Intent(this, FilterSearchActivity.class);
                startActivity(psss);
                break;

            case R.id.menufl:
                Intent pfl = new Intent(this, AllFollowingListActivity.class);
                startActivity(pfl);
                break;

            case R.id.menufollower:
                Intent pflr = new Intent(this, AllFollowersListActivity.class);
                startActivity(pflr);
                break;

            case R.id.menufind:
                Intent pfnd = new Intent(this, FollowActivity.class);
                startActivity(pfnd);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fab:
                animateFAB();
                fab1.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.VISIBLE);
                break;

            case R.id.layout_fab1:
                selectVideoOption();
                break;

            case R.id.layout_fab3:
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

//                startActivityForResult(new Intent(MainActivity.this, CustomGallery_Activity.class), CustomGallerySelectId);
                break;

        }

    }


    private void selectVideoOption() {
        final CharSequence[] items = {"Record Video","Choose Video", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo/Video");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Record Video")) {
                    Intent intent = new Intent( MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(Intent.createChooser(intent, "Select a Video "), CAPTURE_VIDEO);
                }
                else if (items[item].equals("Choose Video")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent, VIDEO_CODE);
                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void animateFAB(){

        if(isFabOpen){
            fab.startAnimation(rotate_backward);
            fab1.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
            fab1.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab1.setVisibility(View.GONE);
            fab3.setVisibility(View.GONE);
            fab1.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
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
                    return new HomeFragment();
                case 1:
                    return new RequestFragment();
                case 2:
                    return new GlobalFragment();
            }
            return null;
        }
    }


//    public void logout()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setIcon(R.drawable.test)
//                .setTitle("Log out")
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        manager.setPreferences(MainActivity.this, Constants.LOGIN_STATUS,"0");
//                        manager.setPreferences(MainActivity.this, Constants.CAMERA_STATUS,"0");
//
//                        Intent intent = new Intent(MainActivity.this, GuidePageActivity2.class);
//                        startActivity(intent);
//                        MainActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }



    public void logoutdialog(){

        dialog = new Dialog(MainActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.logout_dialog_layout);

        Button cancel_logout=dialog.findViewById(R.id.cancel_logout);
        Button logout_dialog =dialog.findViewById(R.id.logout_dialog);

        logout_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setPreferences(MainActivity.this, Constants.LOGIN_STATUS, "0");
                manager.setPreferences(MainActivity.this, Constants.CAMERA_STATUS, "0");

                Intent intent = new Intent(MainActivity.this, GuidePageActivity2.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
               MainActivity.this.finish();

                dialog.dismiss();

            }
        });

        cancel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), "Please Click back again to exit", Snackbar.LENGTH_LONG);
        snackbar.show();

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#BD071D"));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }

}