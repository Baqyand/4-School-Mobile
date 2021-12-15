package com.application.a4_school.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.application.a4_school.ForgotPassword;
import com.application.a4_school.LocalStorage.UserInfoStorage;
import com.application.a4_school.MainActivity;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Activity implements View.OnClickListener {
    EditText etUsername, etPw;
    Button btnlogin;
    TextView resetPw;
    TextView txtRegister;
    SessionManager sessionManager;
    UserInfoStorage userInfoStorage;
    String token;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        userInfoStorage = new UserInfoStorage(getApplicationContext());
        etUsername = findViewById(R.id.inputlogin);
        etPw = findViewById(R.id.inputpassword);
        btnlogin = findViewById(R.id.btn_login);
        resetPw = findViewById(R.id.reset_pw);
        txtRegister = findViewById(R.id.register);

        resetPw.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCMRegis", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                token = task.getResult();
                Log.d("FCMRegis", token);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            this.finish();
            super.onBackPressed();
            return;
        }
        exit = true;
        Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Wait a second...");
        pd.show();
        switch (view.getId()) {
            case R.id.btn_login:
                final String username = etUsername.getText().toString();
                final String pw = etPw.getText().toString();
                APIService api = APIClient.getClient().create(APIService.class);
                Call<ResponseBody> login = api.login(username, pw, token);
                login.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        pd.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                try {
                                    String responseJSON = response.body().string();
                                    Log.d("login", "response : " + responseJSON);
                                    Gson objGson = new Gson();
                                    SessionResponse objResp = objGson.fromJson(responseJSON, SessionResponse.class);
                                    if (objResp.getToken() != null) {
                                        String role = objResp.getUserInfo().getRole();
                                        Log.d("login", "role : " + role);
                                        sessionManager.createSession(objResp.getToken(), role);
                                        userInfoStorage.createInfo(objResp.getUserInfo().getName(), objResp.getUserInfo().getEmail(), objResp.getUserInfo().getId(), objResp.getUserInfo().getPhoto(), objResp.getUserInfo().getId_class(), objResp.getUserInfo().getProfession(), objResp.getUserInfo().getBirthday());
                                        if (role.equals("guru")) {
                                            userInfoStorage.saveNisOrNip(objResp.getUserInfo().getNip());
                                            Intent toDasboard = new Intent(Login.this, MainActivity.class);
                                            toDasboard.putExtra("EXTRA_ROLE", role);
                                            startActivity(toDasboard);
                                            finish();
                                        } else {
                                            userInfoStorage.saveNisOrNip(objResp.getUserInfo().getNis());
                                            Toast.makeText(Login.this, "student page has not been created", Toast.LENGTH_SHORT).show();
                                            Intent toDasboard = new Intent(Login.this, MainActivity.class);
                                            toDasboard.putExtra("EXTRA_ROLE", role);
                                            startActivity(toDasboard);
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("Login", "" + e.getMessage());
                                }
                            } else if (response.code() == 422) {
                                Toast.makeText(Login.this, "Username / Password is still blank", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(Login.this, "Wrong username / password", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 403) {
                                Toast.makeText(Login.this, "Token Invalid", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 404 || response.code() == 405) {
                                Toast.makeText(Login.this, "A server error has occurred", Toast.LENGTH_SHORT).show();
                            }
//                              else if (username.isEmpty()) {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
//                                alertDialogBuilder.setTitle("Field the Blank Form Input");
//                                alertDialogBuilder
//                                        .setMessage("Please Enter Username or Password")
//                                        .setCancelable(false)
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
//                                alertDialogBuilder.setTitle("Incorrect Form Input");
//                                alertDialogBuilder
//                                        .setMessage("Please enter your Email or password correctly")
//                                        .setCancelable(false)
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                            } else if (pw.isEmpty()) {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
//                                alertDialogBuilder.setTitle("Field the Blank Form Input");
//                                alertDialogBuilder
//                                        .setMessage("Please Enter Password")
//                                        .setCancelable(false)
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                            } else {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
//                                alertDialogBuilder.setTitle("Incorrect");
//                                alertDialogBuilder
//                                        .setMessage("Email or password not recornized")
//                                        .setCancelable(false)
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                            }
                        } else {
                            Toast.makeText(Login.this, "Wrong Username / Password ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                        alertDialogBuilder.setTitle("Internet Connection Error");
                        alertDialogBuilder
                                .setMessage("Please check your internet connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        Toast.makeText(Login.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("loginvalue", "Message : " + t.getMessage());
                    }
                });
                break;

            case R.id.reset_pw:
                pd.dismiss();
                Intent toForgotPassword = new Intent(Login.this, ForgotPassword.class);
                startActivity(toForgotPassword);
                break;

            case R.id.register:
                pd.dismiss();
                Intent toRegister = new Intent(Login.this, Register.class);
                startActivity(toRegister);
                break;
        }

    }

    private void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false);

        // membuat alert dialog dari builder
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        // menampilkan alert dialog
        alertDialog.show();
    }
}



