package com.application.a4_school.Auth;

import com.application.a4_school.Auth.sessionResp.UserInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionResponse {
    private UserInfo userInfo;

    @SerializedName("access_token")
    private String token;

    @SerializedName("expires_in")
    private int session_time;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getToken() {
        return token;
    }

    public int getSession_time() {
        return session_time;
    }
}
