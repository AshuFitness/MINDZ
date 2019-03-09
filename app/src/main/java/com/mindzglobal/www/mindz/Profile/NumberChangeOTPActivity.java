package com.mindzglobal.www.mindz.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Wave on 1/4/2018.
 */

public class NumberChangeOTPActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.mobile_num)
    TextView mMobile_num;

    @Bind(R.id.otp)
    EditText otp;

    @Bind(R.id.submit)
    Button submit;

    @Bind(R.id. resend)
    TextView mResend;


SessionManager manager;
    String name,email,mobile,token;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.otp);
        ButterKnife.bind(this);
        manager = new SessionManager();

         token = manager.getPreferences(NumberChangeOTPActivity.this, Constants.USER_TOKEN);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        mobile = bundle.getString("mobile");

        mMobile_num.setText(mobile);

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
            otpCheck();
    }

    public void otpCheck(){
        RetrofitClient.getWithClient().vaildOTP(mMobile_num.getText().toString(),otp.getText().toString()).enqueue(new GlobalCallback<String>(submit) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("otpCheck",response.body().toString());
                String otpres = response.body().toString();
                if(otpres.contains("1")){
//                    Toast.makeText(NumberChangeOTPActivity.this, "Valid Otp", Toast.LENGTH_SHORT).show();
                    postChangeNumber();
                }
                else if(otpres.contains("2")){
                    Toast.makeText(NumberChangeOTPActivity.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NumberChangeOTPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NumberChangeOTPActivity.this, "Try Again!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void postChangeNumber(){
        RetrofitClient.getWithClient().postmobChange(mMobile_num.getText().toString(),
                "application/json","Bearer "+token).enqueue(new GlobalCallback<String>(submit) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("postdatares",response.toString());

                String postdatares = response.body().toString();

                if(postdatares.contains("1")){
                    Toast.makeText(NumberChangeOTPActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    manager.setPreferences(NumberChangeOTPActivity.this,Constants.USER_NUM,mMobile_num.getText().toString());
                    Toast.makeText(NumberChangeOTPActivity.this, manager.getPreferences(NumberChangeOTPActivity.this,Constants.USER_NUM), Toast.LENGTH_SHORT).show();
                }
                else if(postdatares.contains("2")){
                    Toast.makeText(NumberChangeOTPActivity.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

 }