package com.application.a4_school;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.application.a4_school.Auth.Login;
import com.application.a4_school.LocalStorage.UserInfoStorage;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
    private static String TAG = "ForgotPasswordSend";
    private LinearLayout containerEmail, containerForgotPassword;
    private EditText edtEmail, edtPasswordnew, edtpasswordconfirm, edtToken;
    private Button btnResetpw;
    private AlertDialog.Builder dialog;
    private UserInfoStorage userInfoStorage;
    private String pw;
    private String confirmpw;
    private String statement = "sendmail";
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        intialize();
        userInfoStorage = new UserInfoStorage(getApplicationContext());
        dialog = new AlertDialog.Builder(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(ForgotPassword.this, Login.class);
                startActivity(toLogin);
            }
        });

        btnResetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (statement){
                    case "sendmail":
                        sendMailToken();
                        break;

                    case "resetpw":
                        pw = edtPasswordnew.getText().toString().trim();
                        confirmpw = edtpasswordconfirm.getText().toString().trim();
                        if (pw.equals(confirmpw)){
                            resetPw();
                        }else{
                            edtpasswordconfirm.setError("Password is not match");
                        }
                        break;
                }

            }
        });
    }

    private void intialize(){
        containerEmail = findViewById(R.id.container_emailforgot);
        containerForgotPassword = findViewById(R.id.containerNewPassword);
        edtEmail = findViewById(R.id.edtEmailForgot);
        edtPasswordnew = findViewById(R.id.edtPasswordForgot);
        edtpasswordconfirm = findViewById(R.id.edtPasswordForgotConfirm);
        edtToken = findViewById(R.id.edtTokenForgot);
        btnResetpw = findViewById(R.id.btn_reset_password);
        btnBack = findViewById(R.id.btn_back);
    }

    private void sendMailToken(){
        final String mail = edtEmail.getText().toString().trim();
        userInfoStorage.saveEmail(mail);
        APIService api = APIClient.getClient().create(APIService.class);
        Call<JsonObject> sendMail = api.sendMailToken(mail);
        sendMail.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "success: "+response.body().getAsJsonObject());
                    dialog.setTitle("Token sended");
                    dialog.setMessage("Your access token was sended to "+mail);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    statement = "resetpw";
                    containerEmail.setVisibility(View.GONE);
                    containerForgotPassword.setVisibility(View.VISIBLE);
                }else{
                    Log.d(TAG, "not success: "+response.body().getAsJsonObject());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "failure: "+t.getMessage());
                dialog.setTitle("Opps");
                dialog.setMessage("Cant connect to server");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void resetPw(){
        APIService api = APIClient.getClient().create(APIService.class);
        String mail = getSharedPreferences("userInfo", 0).getString("email", "");
        Call<JsonObject> sendNewPw = api.resetPassword(mail, pw, edtToken.getText().toString().trim());
        sendNewPw.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "success: "+response.body().getAsJsonObject());
                    dialog.setTitle("Sucess");
                    dialog.setMessage("Password change successfully");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent tologin = new Intent(ForgotPassword.this, Login.class);
                            startActivity(tologin);
                        }
                    },1000);
                }else{
                    Log.d(TAG, "not success: "+response.body());
                    dialog.setTitle("Failed!");
                    dialog.setMessage("Something error on server, please try again later");
                    dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "failure: "+t.getMessage());
                dialog.setTitle("Oppsss...");
                dialog.setMessage("Can't connect to server please check your internet connection");
                dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}