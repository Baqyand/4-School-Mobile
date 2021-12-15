package com.application.a4_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.a4_school.Auth.Login;

public class SplashScreen extends AppCompatActivity {
    private int delayscreen = 1500;
    private TextView txt_splash;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txt_splash = findViewById(R.id.splashschool);
        logo = findViewById(R.id.app_logo_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txt_splash.setVisibility(View.VISIBLE);
                txt_splash.animate().alpha(1.0f).setDuration(200);
            }
        },500);

        new Handler().postDelayed(new  Runnable(){
            @Override
            public void run() {
                checksession();
            }
        },delayscreen);
    }

    private void checksession(){
        SharedPreferences getSession = getSharedPreferences("session", 0);
        String token = getSession.getString("token", "");
        if (token != ""){
            Intent toHome = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(toHome);
            finish();
        }else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, logo, ViewCompat.getTransitionName(logo));
            startActivity(intent, options.toBundle());
            finish();
        }
    }
}