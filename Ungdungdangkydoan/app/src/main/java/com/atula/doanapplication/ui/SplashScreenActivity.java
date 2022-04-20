package com.atula.doanapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.atula.doanapplication.R;
import com.atula.doanapplication.config.Config;
import com.atula.doanapplication.ui.ad.activity.AdmindActivity;
import com.atula.doanapplication.ui.user.activity.MainActivity;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> map = Config.getInstance().getSaveLogin(SplashScreenActivity.this);
                String isSave = map.get("ISSAVE");
                if(Boolean.parseBoolean(isSave)){
                    boolean isSv = Boolean.parseBoolean(map.get("ISSV"));
                    if(isSv){
                        String mssv = map.get("MSSV");
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        intent.putExtra("MSSV",mssv);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashScreenActivity.this, AdmindActivity.class);
                        startActivity(intent);
                    }

                }else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 2000);
    }
}