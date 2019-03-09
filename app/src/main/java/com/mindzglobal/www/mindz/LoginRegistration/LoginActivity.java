package com.mindzglobal.www.mindz.LoginRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.ForgotPassword.ForgotPasswordActivity;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.login.LoginPojo;
import com.mindzglobal.www.mindz.Model.login.UserLogin;
import com.mindzglobal.www.mindz.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import custom_font.MyEditText;
import custom_font.MyTextView;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.signIn)
    Button mSignIn;

    @Bind(R.id.user_id)
    MyEditText mUser_id;

    @Bind(R.id.password)
    MyEditText mPassword;

    @Bind(R.id.forgot_pass)
    TextView mForgot_pass;

    @Bind(R.id.signUp)
    MyTextView mSignUp;

    SessionManager manager;
    String checkStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);
        ButterKnife.bind(this);
        manager =  new SessionManager();
        checkStatus = manager.getPreferences(LoginActivity.this,Constants.LOGIN_STATUS);

//        if(checkStatus.contains("1")){
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
        mSignIn.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        mForgot_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.signUp:
                Intent  i  = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
                break;
            case R.id.signIn:
                checkValidation();
                break;
            case R.id.user_id:
                break;
            case R.id.password:
                break;
            case R.id.forgot_pass:
                Intent fpass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(fpass);
                break;
        }
    }


    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts
        String getUserId = mUser_id.getText().toString();
        String getPass = mPassword.getText().toString();

        // Check if all strings are null or not
        if (getUserId.equals("") || getUserId.length() == 0
                || getPass.equals("") || getPass.length() == 0) {
            Toast.makeText(this,  R.string.all_fields_req, Toast.LENGTH_SHORT).show();
        }

        else
        {
            login();
        }

    }

    public void login(){
        String getUserId = mUser_id.getText().toString();
        String getPass = mPassword.getText().toString();
        RetrofitClient.getWithClient().login(getUserId,getPass).enqueue(new GlobalCallback<LoginPojo>(mUser_id) {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                UserLogin  userLogin = response.body().getUserLogin();
                String loginres = userLogin.getResponse();
                String l_name = userLogin.getName();
                String l_email = userLogin.getEmail();
                String l_num = userLogin.getNumber();
                String l_proCat = userLogin.getProfileCategory();
                String l_country = userLogin.getCountry();
                String l_state= userLogin.getState();
                String l_city = userLogin.getCity();
                String l_area = userLogin.getArea();
                String l_token = userLogin.getToken();
                String l_username = userLogin.getUserName();
                String l_gender = userLogin.getGender();
                String l_ProfilePic = userLogin.getProfilePic();
                String uAccType = userLogin.getAccountType();

                if(loginres.contains("1")){
                    // valid user
                    manager.setPreferences(LoginActivity.this, Constants.USER_ACC_TYPE, uAccType);
                    manager.setPreferences(LoginActivity.this, Constants.USER_PROFILE_PIC, l_ProfilePic);
                    manager.setPreferences(LoginActivity.this, Constants.USER_GENDER, l_gender);
                    manager.setPreferences(LoginActivity.this, Constants.USER_UN_NAME, l_username);
                    manager.setPreferences(LoginActivity.this, Constants.USER_NAME, l_name);
                    manager.setPreferences(LoginActivity.this, Constants.USER_EMAIL, l_email);
                    manager.setPreferences(LoginActivity.this, Constants.USER_NUM, l_num);
                    manager.setPreferences(LoginActivity.this, Constants.USER_PRO_CAT, l_proCat);
                    manager.setPreferences(LoginActivity.this, Constants.USER_COUNTRY, l_country);
                    manager.setPreferences(LoginActivity.this, Constants.USER_STATE, l_state);
                    manager.setPreferences(LoginActivity.this, Constants.USER_CITY, l_city);
                    manager.setPreferences(LoginActivity.this, Constants.USER_AREA, l_area);
                    manager.setPreferences(LoginActivity.this, Constants.USER_TOKEN, l_token);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    manager.setPreferences(LoginActivity.this, Constants.LOGIN_STATUS, "1");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    LoginActivity.this.finish();
                }
                else {
                    //Invalid user
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}