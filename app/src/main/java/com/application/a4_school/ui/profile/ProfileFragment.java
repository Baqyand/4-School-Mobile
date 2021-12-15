package com.application.a4_school.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.application.a4_school.Auth.Login;
import com.application.a4_school.Auth.SessionManager;
import com.application.a4_school.EditProfile;
import com.application.a4_school.ForgotPassword;
import com.application.a4_school.LocalStorage.UserInfoStorage;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.ui.help.HelpViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    FloatingActionButton chooseImage;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int REQUEST_GALLERY = 9544;
    public static final int CAPTURE_REQUEST_CODE = 700;
    private String role;
    private static ProfileFragment instance;
    private UserInfoStorage userInfoStorage;
    private SessionManager sessionManager;
    private ConstraintLayout containerProf, containerClass, containerMajor;
    private TextView shProf;
    private Bitmap bitmap;
    private Toolbar shUsername;
    private Button btnLogout;
    private CircleImageView userImage;
    private String nipOrNis;
    private String email;
    private String birthday;
    private String majors;
    private String classLevel;
    String part_image = "";
    String id_class;
    Context context;
    FloatingActionButton editProfile;
    private TextView shNipOrNis, shEmail, shClass, shMajors, shBirthday;

    public CircleImageView getUserImage() {
        return userImage;
    }

    public ProfileFragment(String role) {
        this.role = role;
    }

    public static ProfileFragment getInstance() {
        return instance;
    }

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initialize(root);
        instance = this;

        nipOrNis = getActivity().getSharedPreferences("userInfo", 0).getString("nip_or_nis", "Loading...");
        email = getActivity().getSharedPreferences("userInfo", 0).getString("email", "loading...");
        birthday = getActivity().getSharedPreferences("userInfo", 0).getString("birthday", "loading...");

        shNipOrNis.setText(nipOrNis);
        shEmail.setText(email);
        shBirthday.setText(birthday);

        getUserInfo();
        ImageButton editProfile = root.findViewById(R.id.edtprofile);

        userInfoStorage = new UserInfoStorage(getActivity().getApplicationContext());
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        final String name = getActivity().getSharedPreferences("userInfo", 0).getString("name", "Hmm something wen't wrong i cant see your name):");
        shUsername.setTitle(name);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEditProfile = new Intent(getActivity(), EditProfile.class);
                toEditProfile.putExtra("EXTRA_NAME", name);
                toEditProfile.putExtra("EXTRA_NIS", nipOrNis);
                toEditProfile.putExtra("EXTRA_EMAIL", email);
                toEditProfile.putExtra("EXTRA_CLASS", classLevel);
                toEditProfile.putExtra("EXTRA_MAJORS", majors);
                toEditProfile.putExtra("EXTRA_DATE", birthday);
                toEditProfile.putExtra("EXTRA_ID_CLASS", id_class);


                startActivity(toEditProfile);
            }
        });

        String prof = null;

        if (role.equals("guru")){
            containerClass.setVisibility(View.GONE);
            containerMajor.setVisibility(View.GONE);
            shProf.setText(prof);
        }else{
            containerProf.setVisibility(View.GONE);
        }
//        final Toolbar toolbar = (Toolbar)root.findViewById(R.id.toolbarpf);
//        toolbar.setBackgroundColor(R.color.BlueishPurple);
////        final Toolbar tb = (Toolbar)root.findViewById(R.id.toolbar);
////
//
        AppCompatActivity app = (AppCompatActivity) getActivity();
        shUsername.setSubtitle("1819117625");
        app.setSupportActionBar(shUsername);
//
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout)root.findViewById(R.id.collaps);
//        collapsingToolbar.setTitle("APP");
//        int bgColor = ContextCompat.getColor(context, R.color.BluePurple);
//        collapsingToolbar.setExpandedTitleColor( ContextCompat.getColor(context, bgColor));


        SharedPreferences getUserInfo = getActivity().getSharedPreferences("userInfo", 0);
        String url_image = getUserInfo.getString("image", "");
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.empty_profile);

        Glide.with(userImage.getContext()).load(url_image).apply(options).into(userImage);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseMenu();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog loading = new ProgressDialog(getContext());
                loading.setMessage("Please wait...");
                loading.setCancelable(false);
                loading.show();
                logout(loading);
            }
        });

        return root;
    }


    private void initialize(View root) {
        chooseImage = root.findViewById(R.id.chooseUserImage);
        userImage = root.findViewById(R.id.userImage);
        shUsername = root.findViewById(R.id.toolbarpf);
        shNipOrNis = root.findViewById(R.id.nip_or_nis);
        shEmail = root.findViewById(R.id.email_profile);
        shClass = root.findViewById(R.id.class_profile);
        shMajors = root.findViewById(R.id.majors_profile);
        shBirthday = root.findViewById(R.id.birthday);
        containerProf = root.findViewById(R.id.container_profession_profile);
        containerClass = root.findViewById(R.id.container_class_profile);
        containerMajor = root.findViewById(R.id.container_major_profile);
        shProf = root.findViewById(R.id.profession);
        btnLogout = root.findViewById(R.id.btn_logout);
    }

    private void getUserInfo() {
        int id_user = getActivity().getSharedPreferences("userInfo", 0).getInt("id", 0);
        final String role = getActivity().getSharedPreferences("session", 0).getString("role", "");
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> loadInfo = api.getUserInfo(id_user);
        loadInfo.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    id_class = response.body().getInformation().getId_class();
                    switch (role) {
                        case "siswa":
                            shClass.setText(response.body().getInformation().getClassLevel());
                            shMajors.setText(response.body().getInformation().getMajors());
                            classLevel = response.body().getInformation().getClassLevel();
                            majors = response.body().getInformation().getMajors();
                            break;
                        case "guru":

                            break;
                    }

                } else {
                    Toast.makeText(getActivity(), "failed load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chooseMenu() {
        if (CheckPermission()) {
            final Dialog popUpdialog = new Dialog(getContext(), R.style.AppBottomSheetDialogTheme);
            popUpdialog.setContentView(R.layout.dialog_select_picture);
            Button btnOpenCamera = popUpdialog.findViewById(R.id.btn_opencamera);
            Button btnOpenGallery = popUpdialog.findViewById(R.id.btn_open_gallery);
            btnOpenCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capture, CAPTURE_REQUEST_CODE);
                    popUpdialog.dismiss();
                }
            });

            btnOpenGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    popUpdialog.dismiss();
                }
            });

            popUpdialog.show();
        }

    }

    private void logout(final ProgressDialog loading){
        int id_user = getActivity().getSharedPreferences("userInfo", 0).getInt("id", 0);
        String token = getActivity().getSharedPreferences("session", 0).getString("token", "");
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> logoutAccount = api.logoutUser(id_user, token);
        logoutAccount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                if (response.isSuccessful()){
                    try {
                        Log.d("logoutvalue", "response: "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getActivity().getSharedPreferences("session", 0).edit().clear().commit();
                    getActivity().getSharedPreferences("userInfo", 0).edit().clear().commit();
                    startActivity(new Intent(getActivity(), Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    getActivity().finish();
                }else{
                    Log.d("logoutvalue", "response: "+response.body());
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Unknown error")
                            .setMessage("Something wrong with server, please try again later")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Oppsss....")
                        .setMessage("Cant connect to server please check your internet connection")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageDecodedUpload(bitmap);
                }
            }
            break;
            case REQUEST_GALLERY: {
                if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] imageprojection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, imageprojection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int indexImage = cursor.getColumnIndex(imageprojection[0]);
                        part_image = cursor.getString(indexImage);
                        if (part_image != null) {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                                imageDecodedUpload(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    private void imageDecodedUpload(final Bitmap bitmap) {
        String image = imageToString();
        SharedPreferences getId_user = getContext().getSharedPreferences("userInfo", 0);
        int id_user = getId_user.getInt("id", 0);
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> upload = api.uploadBase64Pict(id_user, image);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String JSONResponse = response.body().string();
                            Gson objGson = new Gson();
                            ResponseData objResp = objGson.fromJson(JSONResponse, ResponseData.class);
                            Log.d("Uploadimage", "" + JSONResponse);
                            Log.d("Uploadimage", "" + objResp.getImage_url());
                            if (objResp.getImage_url() != null) {
                                userImage.setImageBitmap(bitmap);
                                userInfoStorage.addPict(objResp.getImage_url());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("Uploadimage", "" + e.getMessage());
                        }
                    } else if (response.code() == 401) {
                        startActivity(new Intent(context, Login.class));
                        getActivity().finishAffinity();
                        Toast.makeText(context, "The session has ended, please login again", Toast.LENGTH_SHORT).show();
                        userInfoStorage.clearUser();
                        sessionManager.preferenceLogout();
                    } else if (response.code() == 422) {
                        Toast.makeText(context, "An error occurs, please refresh first", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403) {
                        Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(context, "A server error has occurred", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 405) {
                        Toast.makeText(context, "Method Not accepted by server, please login again", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, Login.class));
                    }

                } else {
                    Toast.makeText(getActivity(), "System Error, please try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Uploadimage", "" + t.getMessage());
            }
        });
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
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
