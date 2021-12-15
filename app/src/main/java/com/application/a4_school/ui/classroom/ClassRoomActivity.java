package com.application.a4_school.ui.classroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.application.a4_school.Models.ClassRoom;
import com.application.a4_school.Models.ClassRoomData;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.adapter.ClassListAdapter;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRoomActivity extends AppCompatActivity {
    private RecyclerView rvClassroom;
    private ClassListAdapter adapter;
    private ArrayList<ClassRoom> list = new ArrayList<>();
    private LottieAnimationView loading_classroom;
    private static String[] headerClassContent;
    private TextView titleClass;
    private int id_schedule;
    private String id_class;
    private String role;
    private String token;
    TextView btnInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        initialize();
        id_class = getIntent().getExtras().getString("EXTRA_CLASS");
        String matpel = getIntent().getExtras().getString("EXTRA_MATPEL");
        id_schedule = getIntent().getExtras().getInt("EXTRA_SCHEDULE", 0);
        role = getSharedPreferences("session", 0).getString("role", "");
        token = getSharedPreferences("session", 0).getString("token", "");
        titleClass.setText(matpel);
        Log.d("infoClass", "" + id_schedule);
        getInfoClass();
        switch (role) {
            case "siswa":
                btnInput.setVisibility(View.GONE);
                break;

            case "guru":
                btnInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toForm = new Intent(ClassRoomActivity.this, FormClassRoomActivity.class);
                        toForm.putExtra("EXTRA_ID_SCHEDULE", id_schedule);
                        Log.d("UploadTask", "value: " + id_schedule);
                        startActivityForResult(toForm, 14);
                    }
                });
        }

    }

    public void initialize() {
        rvClassroom = findViewById(R.id.rv_class);
        btnInput = findViewById(R.id.addInputFormClass);
        loading_classroom = findViewById(R.id.loading_classroom);
        titleClass = findViewById(R.id.titleClass);
    }

    public void getInfoClass() {
        final APIService api = APIClient.getClient().create(APIService.class);
        Call<JsonObject> loadClassInformation = api.getClassInformation(id_class);
        loadClassInformation.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body().getAsJsonObject("class_info");
                    headerClassContent = new String[]{
                            object.get("id").toString().replaceAll("\"", ""),
                            object.get("tingkatan").toString().replaceAll("\"", ""),
                            object.get("jurusan").toString().replaceAll("\"", ""),
                            object.get("class_member").toString().replaceAll("\"", "")
                    };
                    getItemClass();

                } else {
                    if (response.body().getAsJsonObject("class_info") != null) {
                        Log.d("classinfo", "" + response.body().getAsJsonObject("class_info"));
                    } else {
                        Log.d("classinfo", "Object null");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("classinfo", "" + t.getMessage());
            }
        });
    }

    public void getItemClass() {
        final APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> loadClassRoomGuru = api.getListClassItemGuru(id_schedule, "Bearer " + token);
        Call<ResponseData> loadClassRoomSiswa = api.getListClassItemSiswa(id_schedule, "Bearer " + token);
        if (role.equals("guru")) {
            loadClassRoomGuru.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        list.addAll(ClassRoomData.getlistClassroom());
                        if (response.body().getIndex_class_guru() != null) {
                            list.addAll(response.body().getIndex_class_guru());
                        }
                        adapter = new ClassListAdapter(list, headerClassContent, id_class, role,ClassRoomActivity.this);
                        adapter.notifyDataSetChanged();
                        rvClassroom.setLayoutManager(new LinearLayoutManager(ClassRoomActivity.this));
                        rvClassroom.setAdapter(adapter);
                        loading_classroom.setVisibility(View.GONE);
                        adapter.setOnItemClickCallback(new ClassListAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(ClassRoom classRoomList) {
                                Intent toDetail = new Intent(ClassRoomActivity.this, DetailClassRoomActivity.class);
                                toDetail.putExtra("EXTRA_PARCEL_CLASS", classRoomList);
                                toDetail.putExtra("EXTRA_CONDITION", "normalDetail");
                                startActivity(toDetail);
                            }
                        });
                        Log.d("getClassData", "success : " + response.body().getIndex_class_guru());
                    } else {
                        Log.d("getClassData", "response not success");
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Log.d("getClassData", "response not success" + t.getMessage());
                }
            });
        } else {
            loadClassRoomSiswa.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        list.addAll(ClassRoomData.getlistClassroom());
                        if (!response.body().getIndex_class_siswa().isEmpty()) {
                            list.addAll(response.body().getIndex_class_siswa());
                        }
                        adapter = new ClassListAdapter(list, headerClassContent, id_class, role,ClassRoomActivity.this);
                        adapter.notifyDataSetChanged();
                        rvClassroom.setLayoutManager(new LinearLayoutManager(ClassRoomActivity.this));
                        rvClassroom.setAdapter(adapter);
                        loading_classroom.setVisibility(View.GONE);
                        adapter.setOnItemClickCallback(new ClassListAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(ClassRoom classRoomList) {
                                Intent toDetail = new Intent(ClassRoomActivity.this, DetailClassRoomActivity.class);
                                toDetail.putExtra("EXTRA_PARCEL_CLASS", classRoomList);
                                toDetail.putExtra("EXTRA_CONDITION", "normalDetail");
                                startActivity(toDetail);
                            }
                        });
                    } else {
                        Log.d("getClassData", "response not success");
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Log.d("getClassData", "response not success" + t.getMessage());
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 14) {
            this.getItemClass();
        }
    }
}