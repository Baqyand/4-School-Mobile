package com.application.a4_school;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.application.a4_school.ui.help.HelpFragment;
import com.application.a4_school.ui.home.HomeFragment;
import com.application.a4_school.ui.maps.MapsFragment;
import com.application.a4_school.ui.profile.ProfileFragment;
import com.application.a4_school.ui.schedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String role = getSharedPreferences("session", 0).getString("role", "");

        //nampilin awal fragment pertama kali
        getFragmentPage(new HomeFragment(role));
        ChipNavigationBar navigationView = findViewById(R.id.nav_view);
        navigationView.setItemSelected(R.id.nav_home, true);
        navigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.nav_home:
                        fragment = new HomeFragment(role);
                        break;
                    case R.id.nav_maps:
                        fragment = new MapsFragment();
                        break;
                    case R.id.nav_jobs:
                        fragment = new ScheduleFragment(role);
                        break;
                    case R.id.nav_help:
                        fragment = new HelpFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment(role);
                        break;
                }

                getFragmentPage(fragment);
            }
        });

    }
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        return  true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}