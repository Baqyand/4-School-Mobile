package com.application.a4_school.Models;

import com.google.gson.annotations.SerializedName;

public class Members {
    @SerializedName("id")
    private int id_user;
    private String name;
    private String email;
    private String nis;
    private String photo;
    private String file_url;

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getId_user() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNis() {
        return nis;
    }

    public String getPhoto() {
        return photo;
    }
}
