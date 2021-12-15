package com.application.a4_school.UserInteraction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.FilesUpload;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.DownloadFromUrl;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.adapter.ClassFilesAdapter;
import com.application.a4_school.ui.classroom.DetailClassRoomActivity;
import com.application.a4_school.ui.schedule.ScheduleFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetFile extends BottomSheetDialogFragment {
    Button btnAssign, btnAttach, btnPoint;
    LinearLayout container_point;
    EditText edtPoint;
    RecyclerView rv_files;
    ClassFilesAdapter adapter;
    String id_taskclass;
    TextView shStatus;
    int id_user;
    List<FilesUpload> listFiles = new ArrayList<>();

    public BottomSheetFile(String id_taskclass, int id_user) {
        this.id_taskclass = id_taskclass;
        this.id_user = id_user;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        final View mView = View.inflate(getContext(), R.layout.bottom_sheet_assignment, null);
        dialog.setContentView(mView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) mView.getParent());
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        btnAssign = mView.findViewById(R.id.btn_assign);
        btnAttach = mView.findViewById(R.id.btn_attach);
        rv_files = mView.findViewById(R.id.rv_files_assignment);
        shStatus = mView.findViewById(R.id.status_assign);
        container_point = mView.findViewById(R.id.container_point);
        btnPoint = mView.findViewById(R.id.btn_setpoint);
        edtPoint = mView.findViewById(R.id.edt_setpoint);
        btnAssign.setVisibility(View.GONE);
        btnAttach.setVisibility(View.GONE);
        getItemFilesiswa();
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    ScheduleFragment.getInstance().showRecyclerGrid();
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        return dialog;
    }

    private void getItemFilesiswa() {
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> loadFile = api.getListFiles(id_taskclass, id_user, "file_siswa");
        loadFile.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getFilesDetail() != null) {
                        Log.d("DetailLoadfilesiswa", "success");
                        listFiles.addAll(response.body().getFilesDetail());
                        adapter = new ClassFilesAdapter(listFiles, getActivity(), "detail");
                        adapter.notifyDataSetChanged();
                        rv_files.setAdapter(adapter);
                        if (listFiles.get(0).getPoints() == 0){
                            shStatus.setText("Point not set");
                            container_point.setVisibility(View.VISIBLE);
                            btnPoint.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<String> id_assignment = new ArrayList<>();
                                    for (int i=0; i<listFiles.size(); i++){
                                        id_assignment.add(listFiles.get(i).getId());
                                    }
                                    int point = Integer.parseInt(edtPoint.getText().toString());
                                    postPoint(point, id_assignment);
                                }
                            });
                        }else{
                            shStatus.setText("Point: "+listFiles.get(0).getPoints());
                        }
                        adapter.setOnItemClickCallback(new ClassFilesAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(FilesUpload filesUpload, int index) {
                                addListFiles(filesUpload.getFile_url(), filesUpload.getNamefile(), filesUpload.getTypefile(), index);
                            }
                        });
                        Log.d("arrayvalue", "value: " + listFiles);
                    }
                } else {
                    Log.d("DetailLoadfilesiswa", "not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("DetailLoadfilesiswa", "failure: " + t.getMessage());
            }
        });
    }

    private void postPoint(int point, ArrayList<String> id_assignment) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String token = getActivity().getSharedPreferences("session", 0).getString("token", "");
        APIService api = APIClient.getClient().create(APIService.class);
        if (point > 100){
            point = 100;
        }
        Call<ResponseBody> postPoint = api.uploadPoint("Bearer "+token, id_assignment, point);
        final int finalPoint = point;
        postPoint.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    btnPoint.setVisibility(View.GONE);
                    edtPoint.setVisibility(View.GONE);
                    shStatus.setText("Point: " + finalPoint);
                }else{
                    try {
                        String json = response.body().string();
                        Log.d("postPoint", ""+json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("postPoint", ""+t.getMessage());
            }
        });
    }

    private void addListFiles(String url, String filename, String extension, int position) {
        Log.d("downloadfile", "" + filename);
        File fileDownloaded = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + "." + extension);
        Uri openPath = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", fileDownloaded);
        ContentResolver cR = getActivity().getContentResolver();
        String type = cR.getType(openPath);
        Log.d("downloadfile", "mime: " + type);
        Log.d("downloadfile", "" + openPath);
        if (fileDownloaded.exists()) {
            Intent openFile = new Intent(Intent.ACTION_VIEW);
            openFile.setDataAndType(openPath, type);
            openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.d("realmime", "" + type);
            startActivity(openFile);
        } else {
            Log.d("downloadfile", "" + url);
            new DownloadFromUrl(getActivity(), url, filename, extension, type).downloadFile();
        }
    }
}
