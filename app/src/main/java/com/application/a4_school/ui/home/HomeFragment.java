package com.application.a4_school.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.MainActivity;
import com.application.a4_school.Models.Home;
import com.application.a4_school.Models.HomeData;
import com.application.a4_school.Models.Schedule;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.adapter.HomeListAdapter;
import com.application.a4_school.ui.maps.MapsFragment;
import com.application.a4_school.ui.schedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.application.a4_school.R.id.WebView1;
import static com.application.a4_school.R.id.content;
import static com.application.a4_school.R.id.rv_mapel;
import static com.application.a4_school.R.id.time;

public class HomeFragment extends Fragment {
    private HomeViewModel HomeViewModel;
    private String role;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RecyclerView rv;
    HomeListAdapter adapter;
    TextView shName, shGreeting;
    private ArrayList<Home> homeList = new ArrayList<>();

    public HomeFragment(String role) {
        this.role = role;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        shName = root.findViewById(R.id.nameHome);
        shGreeting = root.findViewById(R.id.greeting_text);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            shGreeting.setText("Good Morning,");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            shGreeting.setText("Good Afternoon,");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            shGreeting.setText("Good Evening,");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            shGreeting.setText("Good Night,");
        }


        String name = getActivity().getSharedPreferences("userInfo", 0).getString("name", "Hmm something wen't wrong i cant see your name):");
        shName.setText(name);
        rv = root.findViewById(rv_mapel);
        rv.setHasFixedSize(true);
        showList();

        return root;

    }

    public void showList() {
        homeList.addAll(HomeData.getlisthome());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeListAdapter(role, homeList, getActivity());
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        adapter.setOnItemClickCallback(new HomeListAdapter.OnItemClickCallbackHome() {
            @Override
            public void onItemClicked(Home homeList) {
                ChipNavigationBar navigationView = getActivity().findViewById(R.id.nav_view);
                Fragment fragment = null;
                switch (homeList.getJudul()) {
                    case "JOBS":
                        fragment = new ScheduleFragment(role);
                        navigationView.setItemSelected(R.id.nav_jobs, true);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                        break;

                    case "MAPS":
                        fragment = new MapsFragment();
                        navigationView.setItemSelected(R.id.nav_maps, true);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheckPermission();
    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(getActivity(), getActivity().getClass()));
                                getActivity().overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }
}