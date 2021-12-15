package com.application.a4_school.Models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class FilesUpload {
    private String id;
    @SerializedName("extensi")
    private String typefile;
    @SerializedName("name_file")
    private String namefile;
    @SerializedName("file")
    private String file_url;
    private String path;
    private Uri uri;
    private String realMime;
    @SerializedName("nilai")
    private int points;
    private File files;
    private String status;

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public File getFile() {
        return files;
    }

    public void setFile(File file) {
        this.files = file;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getRealMime() {
        return realMime;
    }

    public void setRealMime(String realMime) {
        this.realMime = realMime;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTypefile() {
        return typefile;
    }

    public void setTypefile(String typefile) {
        this.typefile = typefile;
    }

    public String getNamefile() {
        return namefile;
    }

    public void setNamefile(String namefile) {
        this.namefile = namefile;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
