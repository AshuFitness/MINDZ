package com.mindzglobal.www.mindz.ChangePassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
 * Created by Wave on 8/8/2017.
 */

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.editText7)EditText oldpass;
    @Bind(R.id.editText8)EditText newpass;
    @Bind(R.id.button11)Button change;
    @Bind(R.id.shake)LinearLayout shake;
    @Bind(R.id.progressBar)ProgressBar bar;

    SessionManager manager;


    private Animation shakeAnimation;
    RetrofitClient retrofitClient;
    String token;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);
        manager = new SessionManager();
       // shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
         token = manager.getPreferences(ChangePasswordActivity.this, Constants.USER_TOKEN);
//        retrofitClient = new RetrofitClient(token);
        Log.e("token",token);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button11:
                validate();
                break;
        }
    }


    public void validate(){
        if (oldpass.equals("") || oldpass.length() == 0
                || newpass.equals("") || newpass.length() == 0) {
         //   shake.startAnimation(shakeAnimation);

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ChangePass();
        }
    }


    public void ChangePass(){

        RetrofitClient.getWithClient().resetpass(oldpass.getText().toString(),newpass.getText().toString(),"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(change) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Change pass",response.body().toString());
                bar.setVisibility(View.GONE);
                String res = response.body();
                if(res.contains("1")){

                    Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(res.contains("2")){
                    Toast.makeText(ChangePasswordActivity.this, "Invalid Old Password", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

}
