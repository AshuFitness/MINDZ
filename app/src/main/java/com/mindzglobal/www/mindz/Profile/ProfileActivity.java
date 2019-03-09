package com.mindzglobal.www.mindz.Profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.CropMain.CropImageHorizntalActivity;
import com.mindzglobal.www.mindz.CropMain.CropMainActivity;

import com.mindzglobal.www.mindz.Follow.Followers.AllFollowersListActivity;
import com.mindzglobal.www.mindz.Follow.Following.AllFollowingListActivity;
import com.mindzglobal.www.mindz.LocationAccessUtils.PermissionUtils;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.login.LoginPojo;
import com.mindzglobal.www.mindz.Model.login.UserLogin;

import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.Utils;
import com.mindzglobal.www.mindz.fragments.CreatePostActivity_New;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyTextVieww;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Wave on 1/4/2018.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    @Bind(R.id.edit_mobile)
    TextView edit_mobile;

    @Bind(R.id.edit_email)
    TextView edit_email;


    @Bind(R.id.mobile_fetch)
    TextView mobile_fetch;

    @Bind(R.id.email_fetch)
    TextView email_fetch;

    @Bind(R.id.dob_fetch)
    TextView dob_fetch;

    @Bind(R.id.pan_fetch)
    TextView pan_fetch;

    @Bind(R.id.adhar_fetch)
    TextView adhar_fetch;

    @Bind(R.id.name_view)
    MyTextVieww name_view;

    @Bind(R.id.location_view)
    MyTextVieww location_view;

    @Bind(R.id.iam_view)
    TextView iam_view;

    @Bind(R.id.edit_profile)
    TextView edit_profile;

    @Bind(R.id.edit_profile_pic)
    TextView edit_profile_pic;

    @Bind(R.id.edit_cover_pic)
    TextView edit_cover_pic;

    @Bind(R.id.profile_pic_img)
    ImageView profile_pic_img;

    @Bind(R.id.cover_image)
    KenBurnsView cover_image;

    SessionManager manager;
    String token,name,email,pic;

    RetrofitClient retrofitClient;

    @Bind(R.id.followers_prof)
    LinearLayout followers_prof;


    @Bind(R.id.following_prof)
    LinearLayout following_prof;

    Dialog dialog,dialog_email;


    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    private File outPutFile = null;

    final  String picurl = Config.BASE_URL_MEDIA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ButterKnife.bind(this);

        manager = new SessionManager();
        token = manager.getPreferences(ProfileActivity.this, Constants.USER_TOKEN);
        name = manager.getPreferences(ProfileActivity.this, Constants.USER_NAME);
        email = manager.getPreferences(ProfileActivity.this, Constants.USER_EMAIL);
        pic = manager.getPreferences(ProfileActivity.this, Constants.USER_PROFILE_PIC);
        retrofitClient = new RetrofitClient(token);
        permissionUtils = new PermissionUtils(ProfileActivity.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");


        edit_mobile.setOnClickListener(this);
        edit_email.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        edit_profile_pic.setOnClickListener(this);
        edit_cover_pic.setOnClickListener(this);

        followers_prof.setOnClickListener(this);

        following_prof.setOnClickListener(this);

        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(ProfileActivity.this).load(picurl+pic)
                                .centerCrop()
                                .into(profile_pic_img);
                    }
                });
            }
        }).start();



        new Thread(new Runnable() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getCustData();
                    }
                });
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_mobile:
//                mobDailog();
                mobiledialog();
                break;

            case R.id.edit_email:
                emailDailog1();
                break;

            case R.id.edit_profile:
                Intent i = new Intent(ProfileActivity.this,ProfileEditActivity.class);
                i.putExtra("dob",dob_fetch.getText().toString());
                i.putExtra("panCard",pan_fetch.getText().toString());
                i.putExtra("adharCard",adhar_fetch.getText().toString());
                startActivity(i);
                break;

            case R.id.edit_profile_pic:
                Intent crop = new Intent(ProfileActivity.this, CropMainActivity.class);
                startActivity(crop);

                break;
            case R.id.edit_cover_pic:
                Intent crops = new Intent(ProfileActivity.this,CropImageHorizntalActivity.class);
                startActivity(crops);
                break;

            case R.id.followers_prof:
                Intent follower_intent = new Intent(ProfileActivity.this,AllFollowersListActivity.class);
                startActivity(follower_intent);
                break;

            case R.id.following_prof:
                Intent following_intent = new Intent(ProfileActivity.this,AllFollowingListActivity.class);
                startActivity(following_intent);
                break;

        }
    }



    public void mobiledialog(){

        dialog = new Dialog(ProfileActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.mobile_dialog_layout);


        final EditText input=dialog.findViewById(R.id.rank_dialog_name);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);

        String mobile=input.getText().toString();
        mobile_fetch.setText(mobile);

        Button cancel_dialog=dialog.findViewById(R.id.cancel_dialog);
        Button submit_dialog =dialog.findViewById(R.id.submit_dialog);

        submit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.equals("") || input.length() == 0 ){
//                            new CustomToast().Show_Toast(getActivity(), rootView,
//                                    "Please Enter Mobile Number.");

                    Toast.makeText(ProfileActivity.this, "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();
                }

                else   if (!input.getText().toString().matches("^[789]\\d{9}$")) {
                    Toast.makeText(ProfileActivity.this, R.string.invalid_mobile, Toast.LENGTH_SHORT).show();
                }

                else{

                    checkMobNumber(input.getText().toString());
                    //  Toast.makeText(ProfileActivity.this, "Sunny", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();

            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }



    public void  emailDailog1(){

        dialog_email = new Dialog(ProfileActivity.this);
        dialog_email.setCanceledOnTouchOutside(false);
        dialog_email.setContentView(R.layout.email_dialog_layout);


        final EditText input_email=dialog.findViewById(R.id.email_dialog_name);
        input_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);

        String email=input_email.getText().toString();
        email_fetch.setText(email);

        Button cancel_dialog=dialog.findViewById(R.id.cancel_email_dialog);
        Button submit_dialog =dialog.findViewById(R.id.submit_email_dialog);

        submit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern p = Pattern.compile(Utils.regEx);
                Matcher m = p.matcher(input_email.getText().toString());
                if(input_email.equals("") || input_email.length() == 0 ){

                    Toast.makeText(ProfileActivity.this, "Please Enter Valid Email IDr.", Toast.LENGTH_SHORT).show();
                }

                else if (!m.find()) {
                    Toast.makeText(ProfileActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                }

                else{
                    changeEmail(input_email.getText().toString());
                }
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_email.dismiss();
            }
        });


        dialog_email.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog_email.show();

    }



    public void checkMobNumber(final String mob){

        RetrofitClient.getWithClient().mobChange(mob,email,name).enqueue(new GlobalCallback<String>(edit_email) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("Change pass",response.body().toString());
             //   bar.setVisibility(View.GONE);
                String res = response.body();
                if(res.contains("1")){

                    Toast.makeText(ProfileActivity.this, "Number Already Exsists", Toast.LENGTH_SHORT).show();
//                    finish();
                }
                else if(res.contains("2")){
//                    Toast.makeText(ProfileActivity.this, "Jump to another Activity", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ProfileActivity.this,NumberChangeOTPActivity.class);
                    i.putExtra("name",name);
                    i.putExtra("email",email);
                    i.putExtra("mobile",mob);
                    startActivity(i);

                }

            }
        });
    }





     public void changeEmail(final String emails){

         RetrofitClient.getWithClient().changeEID(emails,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(edit_email) {
             @Override
             public void onResponse(Call<String> call, Response<String> response) {
                 String emailChangeres = response.body().toString();
                 if(emailChangeres.contains("1")){
                     Toast.makeText(ProfileActivity.this, "Email ID Already Exists", Toast.LENGTH_SHORT).show();
                 }
                 else if(emailChangeres.contains("2")){
                     Toast.makeText(ProfileActivity.this, "Email ID Changed Successfully", Toast.LENGTH_SHORT).show();
                     manager.setPreferences(ProfileActivity.this,Constants.USER_EMAIL,emails);

                 }
             }
         });
     }


     public void getCustData(){

         String url = "https://www.homiezin.com/api/get-user/";

         Log.e("token",token);
         RetrofitClient.getWithClient().getuserData(url,"application/json","Bearer "+token).enqueue(new GlobalCallback<LoginPojo>(edit_email) {
             @Override
             public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
//                 String custdata = response.body().toString();
                 Log.e("custdata",response.toString());
                try{
                 UserLogin userLogin = response.body().getUserLogin();
                 String loginres = userLogin.getResponse();
                 String l_name = userLogin.getName();
                 String l_email = userLogin.getEmail();
                 String l_num = userLogin.getNumber();
                 String l_proCat = userLogin.getProfileCategory();
                 String l_country = userLogin.getCountry();
                 String l_state= userLogin.getState();
                 String l_city = userLogin.getCity();
                 String l_area = userLogin.getArea();
                 String l_dob = userLogin.getDob();
                 String l_panCard = userLogin.getPanCard();
                 String l_UserName = userLogin.getUserName();
                 String l_Gender = userLogin.getGender();
                 String uAccType = userLogin.getAccountType();
                 String l_adhaardCard = userLogin.getAdhaardCard();
                 final String profilePic = userLogin.getProfilePic();
                 final String coverPic = userLogin.getCoverPic();
                 manager.setPreferences(ProfileActivity.this, Constants.USER_GENDER, l_Gender);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_PROFILE_PIC, profilePic);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_ACC_TYPE, uAccType);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_NAME, l_name);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_EMAIL, l_email);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_NUM, l_num);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_PRO_CAT, l_proCat);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_COUNTRY, l_country);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_STATE, l_state);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_CITY, l_city);
                 manager.setPreferences(ProfileActivity.this, Constants.USER_AREA, l_area);


//                 Log.e("profilePic",profilePic);
//                    Log.e("coverPic",coverPic);

                 if(loginres.contains("1")){
                     // valid user
                     mobile_fetch.setText(l_num);
                     email_fetch.setText(l_email);
                     dob_fetch.setText(l_dob);
                     pan_fetch.setText(l_panCard);
                     adhar_fetch.setText(l_adhaardCard);
                     name_view.setText(l_name);
                     location_view.setText(l_city+", "+l_country);
                     iam_view.setText(l_proCat);

                     new Thread(new Runnable() {
                         public void run() {

                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Glide.with(ProfileActivity.this).load(picurl+profilePic)
                                             .centerCrop()
                                             .into(profile_pic_img);
                                 }
                             });
                         }
                     }).start();

                     new Thread(new Runnable() {
                         public void run() {

                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Glide.with(ProfileActivity.this).load(picurl+coverPic)
                                             .centerCrop()
                                             .into(cover_image);
                                 }
                             });
                         }
                     }).start();
                 }
                  }

                catch (Exception e){

                    }
             }
         });
     }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

}


//    public void mobDailog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//        builder.setTitle("Enter Valid Mobile Number");
//
//// Set up the input
//        final EditText input = new EditText(ProfileActivity.this);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
//        builder.setView(input);
//
//// Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(input.equals("") || input.length() == 0 ){
////                            new CustomToast().Show_Toast(getActivity(), rootView,
////                                    "Please Enter Mobile Number.");
//
//                    Toast.makeText(ProfileActivity.this, "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();
//                }
//
//                else   if (!input.getText().toString().matches("^[789]\\d{9}$")) {
//                    Toast.makeText(ProfileActivity.this, R.string.invalid_mobile, Toast.LENGTH_SHORT).show();
//                }
//
//                else{
//
//                    checkMobNumber(input.getText().toString());
//                    //  Toast.makeText(ProfileActivity.this, "Sunny", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//
//    }





//     public void  emailDailog(){
//         AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//         builder.setTitle("Enter Valid Email ID");
//
//         final EditText input = new EditText(ProfileActivity.this);
//         input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
//         builder.setView(input);
//
//         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialog, int which) {
//                 Pattern p = Pattern.compile(Utils.regEx);
//                 Matcher m = p.matcher(input.getText().toString());
//                 if(input.equals("") || input.length() == 0 ){
//
//                     Toast.makeText(ProfileActivity.this, "Please Enter Valid Email IDr.", Toast.LENGTH_SHORT).show();
//                 }
//
//        else if (!m.find()) {
//                     Toast.makeText(ProfileActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
//                 }
//
//                 else{
//                     changeEmail(input.getText().toString());
//                 }
//             }
//         });
//         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//             @Override
//             public void onClick(DialogInterface dialog, int which) {
//                 dialog.cancel();
//             }
//         });
//         builder.show();
//     }