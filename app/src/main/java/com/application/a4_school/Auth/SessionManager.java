package com.application.a4_school.Auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private int mode = 0;
    private static final String REFNAME = "session";
    private static final String token = "token";
    private static final String role = "role";
    private Context context;

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REFNAME, mode);
        editor = sharedPreferences.edit();
    }
    public void createSession(String jwtToken, String Role){
        editor.putString(token, jwtToken);
        editor.putString(role, Role);
        editor.commit();
    }
    public void preferenceLogout(){
        editor.clear();
        editor.commit();
    }
}
