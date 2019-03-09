package com.mindzglobal.www.mindz.LoginRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.Model.CustDataPojo.CustomerDataPojo;
import com.mindzglobal.www.mindz.Model.CustDataPojo.UserRegister;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.sms.SmsListener;
import com.mindzglobal.www.mindz.sms.SmsReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Wave on 12/28/2017.
 */

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {



    @Bind(R.id.mobile_num)
    TextView mMobile_num;

    @Bind(R.id.otp)
    EditText otp;

    @Bind(R.id.submit)
    Button submit;

    @Bind(R.id. resend)
    TextView mResend;

    SessionManager manager;

    String name,email,mobile,area,city,state,country,password,am,gender,username,AccountType,FirebaseTKN;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.otp);
        ButterKnife.bind(this);
        manager = new SessionManager();

        Bundle bundle = getIntent().getExtras();

        name = bundle.getString("name");
        email = bundle.getString("email");
        mobile = bundle.getString("mobile");
        area = bundle.getString("area");
        city = bundle.getString("city");
        state = bundle.getString("state");
        country = bundle.getString("country");
        password = bundle.getString("password");
        am = bundle.getString("am");
        username = bundle.getString("username");
        gender = bundle.getString("gender");
        AccountType = bundle.getString("AccountType");
        FirebaseTKN = bundle.getString("FirebaseTKN");

        mMobile_num.setText(mobile);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                //    Log.d("Text",messageText);
                otp.setText(messageText);
                otpCheck(messageText);
            }

            @Override
            public void differentMob(String messageText) {

            }
        });


        submit.setOnClickListener(this);
        mResend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                checkValidation();
                break;

            case R.id.resend:
                getresendOTP();
                break;
        }
    }

    public void checkValidation(){
        String getotp = otp.getText().toString();
        if (getotp.equals("") || getotp.length() == 0) {
            Toast.makeText(this, "Enter OTP!!", Toast.LENGTH_SHORT).show();
        }
        else
            otpCheck(otp.getText().toString());
    }

    public void otpCheck(String otpcheck){
        RetrofitClient.getWithClient().vaildOTP(mMobile_num.getText().toString(),otpcheck).enqueue(new GlobalCallback<String>(submit) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("otpCheck",response.body().toString());
                String otpres = response.body().toString();
                if(otpres.contains("1")){
                    //Toast.makeText(OtpActivity.this, "Valid Otp", Toast.LENGTH_SHORT).show();
                    postCustData();
                }
                else if(otpres.contains("2")){
                    Toast.makeText(OtpActivity.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                }

            }
        });
     }


     public void getresendOTP(){
        RetrofitClient.getWithClient().resndOtp(name,mMobile_num.getText().toString(),email).enqueue(new GlobalCallback<String>(submit) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resendotpres = response.body().toString();

                if(resendotpres.contains("1")){
                    Toast.makeText(OtpActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(OtpActivity.this, "Try Again!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
     }



     public void postCustData(){
         RetrofitClient.getWithClient().custData(FirebaseTKN,AccountType,gender,username,name,email,mobile,password,am,country,state,city,area).enqueue(new GlobalCallback<CustomerDataPojo>(submit) {
             @Override
             public void onResponse(Call<CustomerDataPojo> call, Response<CustomerDataPojo> response) {

                 Log.e("CustData",response.body().toString());

                 UserRegister userRegister = response.body().getUserRegister();
                 String res,uName,uEmail,uNum,uProCat,uCountry,uState,uCity,uArea,uUserame,uToken,uGender,uProfilePic,uAccType;

                  res = userRegister.getResponse();
                  uName = userRegister.getName();
                  uEmail = userRegister.getEmail();
                  uNum = userRegister.getNumber();
                  uProCat = userRegister.getProfileCategory();
                  uCountry = userRegister.getCountry();
                  uState = userRegister.getState();
                  uCity = userRegister.getCity();
                  uArea = userRegister.getArea();
                  uToken = userRegister.getToken();
                  uUserame = userRegister.getUserName();
                  uGender = userRegister.getGender();
                 uProfilePic = userRegister.getProfilePic();
                 uAccType = userRegister.getAccountType();


                 Log.e("resCust",res);

                 if(res.contains("1")){
                //email exists
                 }
                 else if(res.contains("2")){
                //mobile Number
                 }
                 else if(res.contains("3")){
                     //User Name exsists
                 }
                 else if(res.contains("4")){
                     manager.setPreferences(OtpActivity.this, Constants.USER_ACC_TYPE, uAccType);
                     manager.setPreferences(OtpActivity.this, Constants.USER_PROFILE_PIC, uProfilePic);
                     manager.setPreferences(OtpActivity.this, Constants.USER_GENDER, uGender);
                     manager.setPreferences(OtpActivity.this, Constants.USER_UN_NAME, uUserame);
                     manager.setPreferences(OtpActivity.this, Constants.USER_NAME, uName);
                     manager.setPreferences(OtpActivity.this, Constants.USER_EMAIL, uEmail);
                     manager.setPreferences(OtpActivity.this, Constants.USER_NUM, uNum);
                     manager.setPreferences(OtpActivity.this, Constants.USER_PRO_CAT, uProCat);
                     manager.setPreferences(OtpActivity.this, Constants.USER_COUNTRY, uCountry);
                     manager.setPreferences(OtpActivity.this, Constants.USER_STATE, uState);
                     manager.setPreferences(OtpActivity.this, Constants.USER_CITY, uCity);
                     manager.setPreferences(OtpActivity.this, Constants.USER_AREA, uArea);
                     manager.setPreferences(OtpActivity.this, Constants.USER_TOKEN, uToken);
                     Intent i = new Intent(OtpActivity.this, MainActivity.class);

//                     i.putExtra("utoken",FirebaseTKN);

                     startActivity(i);
                 }

             }
         });
     }



}
