package com.mindzglobal.www.mindz.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.Followers.AllFollowersListActivity;
import com.mindzglobal.www.mindz.FriendRequest.global.GalleryOtherActivity;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.OtherProfile.GetSingleProfile;
import com.mindzglobal.www.mindz.Model.OtherProfile.OtherProfilePojo;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import custom_font.MyTextVieww;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener {


    SessionManager manager;
    RetrofitClient retrofitClient;

    LinearLayout poke, about, photos, follower_count, follow_following, block,gallery_other;
    TextView mobile_fetch, email_fetch, dob_fetch;

    String userid, token, follow_status;
    final String picurl = Config.BASE_URL_MEDIA;
    //    follow_user
    ImageView cover_image;
    CircleImageView profile_pic_img;
    Dialog dialog;
    MyTextVieww name_view, location_view, iam_view, poked, blocked, follow_user, poke_other, block_other;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);

        retrofitClient = new RetrofitClient(token);
        manager = new SessionManager();
        token = manager.getPreferences(OtherProfileActivity.this, Constants.USER_TOKEN);
//        userid = manager.getPreferences(OtherProfileActivity.this, Constants.USER_ID);

        cover_image = findViewById(R.id.cover_image);
        profile_pic_img = findViewById(R.id.profile_pic_img);

//        poke_other = findViewById(R.id.poke_other);
        block_other = findViewById(R.id.block_other);

        follow_user = findViewById(R.id.follow_user);

        //LinearLayout
        follow_following = findViewById(R.id.follow_following);
//        poke = findViewById(R.id.poke);
        about = findViewById(R.id.about);
        gallery_other = findViewById(R.id.gallery_other);
        follower_count = findViewById(R.id.follower_count);
        block = findViewById(R.id.block);
//        poked=findViewById(R.id.poked);
        blocked = findViewById(R.id.blocked);

        //Textview
        mobile_fetch = findViewById(R.id.mobile_fetch);
        email_fetch = findViewById(R.id.email_fetch);
        dob_fetch = findViewById(R.id.dob_fetch);

        //MyTextVieww
        name_view = findViewById(R.id.name_view);
        location_view = findViewById(R.id.location_view);
        iam_view = findViewById(R.id.iam_view);


        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("user_id");

        follow_status = bundle.getString("follow_status");
        follow_user.setText(follow_status);

        follow_following.setOnClickListener(this);
        gallery_other.setOnClickListener(this);
        block.setOnClickListener(this);
        otherprofile();
    }



    public void otherprofile() {
        String url = "https://www.homiezin.com/api/single-user-profile/" + userid;
        RetrofitClient.getWithClient().getotherprofile(url, "application/json", "Bearer " + token).enqueue(new GlobalCallback<OtherProfilePojo>(mobile_fetch) {
            @Override
            public void onResponse(Call<OtherProfilePojo> call, Response<OtherProfilePojo> response) {

                try {
                    GetSingleProfile singleProfile = response.body().getGetSingleProfile();

                    final String coverpic = singleProfile.getCoverPic();
                    final String profilepic = singleProfile.getProfilePic();

                    String othername = singleProfile.getName();
//                String hashother=singleProfile.getUserName();
                    String category = singleProfile.getProfileCategory();
                    String email = singleProfile.getEmail();
                    String dob = singleProfile.getDob();
                    String number = singleProfile.getNumber();
                    String city = singleProfile.getCity();

                    name_view.setText(othername);
//                name_view.setText(hashother);
                    location_view.setText(city);
                    iam_view.setText(category);
                    email_fetch.setText(email);
                    dob_fetch.setText(dob);
                    mobile_fetch.setText(number);


                    new Thread(new Runnable() {
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(picurl + profilepic)
                                            .centerCrop()
                                            .into(profile_pic_img);
                                }
                            });
                        }
                    }).start();

                    new Thread(new Runnable() {
                        public void run() {

                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(picurl + coverpic)
                                            .centerCrop()
                                            .into(cover_image);
                                }
                            });
                        }
                    }).start();

                } catch (Exception e) {

                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.follow_following:
                followotheruser(userid);
                break;

            case R.id.block:
                blockdialog();
                break;

            case R.id.follower_count:
                Intent pflr = new Intent(OtherProfileActivity.this, AllFollowersListActivity.class);
                startActivity(pflr);
                break;


            case R.id.about:
                Intent about_intent = new Intent(OtherProfileActivity.this, AboutActivity.class);
                startActivity(about_intent);
                break;

            case R.id.gallery_other:
                Intent photo_intent = new Intent(OtherProfileActivity.this, GalleryOtherActivity.class);
                startActivity(photo_intent);
                break;


        }

//            case R.id.poke:
////                Intent poke_intent=new Intent(OtherProfileActivity.this,PokeActivity.class);
////                poke_intent.putExtra("poke")
////                startActivity(poke_intent);
//                break;
    }

    private void followotheruser(final String followId) {

        RetrofitClient.getWithClient().folloStatus(followId, "application/json", "Bearer " + token).enqueue(new GlobalCallback<String>(follow_user) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String followStatus = response.body().toString();


                if (followStatus.contains("0")) {
                    follow_user.setText("Follow");
                    follow_user.setTextColor(Color.parseColor("#000000")); // Not following

                } else if (followStatus.contains("1")) {
                    follow_user.setText("Following");
                    follow_user.setTextColor(Color.parseColor("#000000")); // Already Following
                }

            }
        });
    }


    public void blockdialog(){

        dialog = new Dialog(OtherProfileActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.block_dialog_layout);

        Button cancel_block=dialog.findViewById(R.id.cancel_block);
        Button block_dialog =dialog.findViewById(R.id.block_dialog);

        block_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockotheruser(userid);
                dialog.dismiss();

            }
        });

        cancel_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }



    private void blockotheruser(final String followId_block) {

        RetrofitClient.getWithClient().blockother(followId_block,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(block_other) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String blockStatus = response.body().toString();

                if(blockStatus.contains("0")){
                    block_other.setText("Block");
                    block_other.setTextColor(Color.parseColor("#000000")); // Not following

                }
                else if(blockStatus.contains("1")){
                    block_other.setText("Blocked");
                    block_other.setTextColor(Color.parseColor("#000000")); // Already Following
                }

            }
        });

    }
}




//    private void pokeotheruser(final String followId_poke) {
//
//        RetrofitClient.getWithClient().pokeother(followId_poke,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(poke_other) {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                String pokeStatus = response.body().toString();
//
////                Log.e("followstatus", followStatus);
//
//                if(pokeStatus.contains("0")){
//                    poke_other.setText("Poke");
//                    poke_other.setTextColor(Color.parseColor("#000000")); // Not following
//
//                }
//                else if(pokeStatus.contains("1")){
//                    poke_other.setText("Poked");
//                    poke_other.setTextColor(Color.parseColor("#000000")); // Already Following
//                }
//
//            }
//        });
//
//    }
