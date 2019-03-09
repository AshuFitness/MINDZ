package com.mindzglobal.www.mindz.ForgotPassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.registered_emailid) EditText email_mobile;
    @Bind(R.id.forgot_button) Button forgot_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);
        ButterKnife.bind(this);
        forgot_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_button:
                checkValidation();
                break;
        }
    }




    private void checkValidation() {
        // Get all edittext texts
        String getuserinfo = email_mobile.getText().toString();

        // Check if all strings are null or not
        if (getuserinfo.equals("") || getuserinfo.length() == 0) {
            Toast.makeText(this,"Invalid Credential", Toast.LENGTH_SHORT).show();
        }
        else
        {
            getPassword();
        }

    }



    public void getPassword(){
        String url = "https://www.homiezin.com/api/forgot-password/"+email_mobile.getText().toString()+"/";
        RetrofitClient.getWithClient().fpassword(url).enqueue(new GlobalCallback<String>(email_mobile) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("fres",response.toString());

                String fres = response.body().toString();
                Log.e("fres",fres);
                if(fres.contains("1")){
                    // successful
                    Toast.makeText(ForgotPasswordActivity.this, "New Password is Send!!", Toast.LENGTH_SHORT).show();
                }

                else if(fres.contains("2")){
                    //Account Not fOUND
                    Toast.makeText(ForgotPasswordActivity.this, "Account not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
