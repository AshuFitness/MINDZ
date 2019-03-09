package com.mindzglobal.www.mindz.Splash;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.LoginRegistration.LoginActivity;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.fragments.GuidePageActivity2;

/**
 * Created by Ashu on 1/13/2018.
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout l1,l2;
    Button btnsub;
    Animation uptodown,downtoup;
    SessionManager manager;
    String checkStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        manager =  new SessionManager();
        checkStatus = manager.getPreferences(SplashActivity.this, Constants.LOGIN_STATUS);
        if(checkStatus.contains("1")){
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        btnsub = (Button)findViewById(R.id.buttonsub);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);



        btnsub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.buttonsub:
//                Intent intent = new Intent(this, InfoSplash.class);
                Intent intent = new Intent(this, GuidePageActivity2.class);
                this.startActivity(intent);
                break;
        }

    }
}