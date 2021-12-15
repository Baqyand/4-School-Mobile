package com.application.a4_school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.a4_school.Auth.Login;
import com.application.a4_school.Auth.Register;
import com.application.a4_school.Auth.sessionResp.UserInfo;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.ui.profile.ProfileFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    private String nameValue, nisValue, emailValue, classValue, majorsValue, dateValue, id_class;
    private Spinner spinnerClass, spinnerMajors;
    String[] selectOptionClass = {"X", "XI", "XII", "XIII"};
    private String id_kelas;
    UserInfo userinfo = new UserInfo();
    //String[] selectOptionMajors = {"RPL", "TKJ", "MM"};
    private List<UserInfo> listmajors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final TextInputEditText edtName = findViewById(R.id.edtname);
        final TextInputEditText edtNis = findViewById(R.id.edtnis);
//        TextInputEditText edtEmail = findViewById(R.id.edtemail);
//        TextInputEditText edtClass = findViewById(R.id.edtclass);
//        TextInputEditText edtMajors = findViewById(R.id.edtmajors);
        TextInputEditText edtDate = findViewById(R.id.edt_date);
        spinnerClass = findViewById(R.id.list_class);
        spinnerMajors = findViewById(R.id.list_majors);
        spinnerMajors.setEnabled(false);

        Button btnSave = findViewById(R.id.btnsave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateProfile(edtName.getText().toString(), edtNis.getText().toString(), majo;
            }
        });

        ArrayAdapter<String> spinnerAdapterClass = new ArrayAdapter<String>(this, R.layout.spinner_type_style, selectOptionClass);


//        ArrayAdapter<String> spinnerAdapterMajors = new ArrayAdapter<String>(this, R.layout.spinner_type_style, selectOptionMajors);
        spinnerAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerAdapterMajors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(spinnerAdapterClass);
//        spinnerMajors.setAdapter(spinnerAdapterMajors);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadMajors(spinnerClass.getSelectedItem().toString());
                Log.d("Classleveldropdown", "value: " + spinnerClass.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nameValue = getIntent().getStringExtra("EXTRA_NAME");
        edtName.setText(nameValue);

        nisValue = getIntent().getStringExtra("EXTRA_NIS");
        edtNis.setText(nisValue);

//        emailValue = getIntent().getStringExtra("EXTRA_EMAIL");
//        edtEmail.setText(emailValue);
        String role = getSharedPreferences("session", 0).getString("role", "");
        if (role.equals("siswa")) {
            classValue = getIntent().getStringExtra("EXTRA_CLASS");
            if (classValue.equals("X")) {
                spinnerClass.setSelection(0);
            }
            if (classValue.equals("XI")) {
                spinnerClass.setSelection(1);
            }
            if (classValue.equals("XII")) {
                spinnerClass.setSelection(2);
            }
            if (classValue.equals("XIII")) {
                spinnerClass.setSelection(3);
            }
        }

        id_class = getIntent().getStringExtra("EXTRA_ID_CLASS");
        userinfo.setId_class(id_class);
        majorsValue = getIntent().getStringExtra("EXTRA_MAJORS");

        dateValue = getIntent().getStringExtra("EXTRA_DATE");
        edtDate.setText(dateValue);

    }

    private void updateProfile(int id_user, String name, String nip, String nis, String kelas, String profesi, String tanggal_lahir) {
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> updateDataProfile = api.updateProfile(id_user, name, nip, nis, kelas, profesi, tanggal_lahir);
        updateDataProfile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseJSON = response.body().string();
                        Toast.makeText(EditProfile.this, "Update Profile Successfully", Toast.LENGTH_SHORT).show();
                        Intent toProfile = new Intent(EditProfile.this, ProfileFragment.class);
                        startActivity(toProfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void loadMajors(String classlevel) {
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> loadListMajor = api.getMajors(classlevel);
        loadListMajor.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    listmajors.clear();
                    listmajors.addAll(response.body().getListMajors());
                    spinnerMajors.setEnabled(true);
                    List<String> list = new ArrayList<>();
                    list.clear();
                    for (int i = 0; i < response.body().getListMajors().size(); i++) {
                        list.add(response.body().getListMajors().get(i).getMajors());
                    }
                    ArrayAdapter<UserInfo> spinmajorAdapter = new ArrayAdapter<UserInfo>(getApplicationContext(), R.layout.spinner_type_style, listmajors);
                    spinmajorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMajors.setAdapter(spinmajorAdapter);
                    spinnerMajors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("majorvalue", "" + listmajors.get(i).getId_class());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Log.d("loadmajors", "not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("loadmajors", "failure " + t.getMessage());
            }
        });
    }
}