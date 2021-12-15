package com.application.a4_school.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ClassRoom implements Parcelable {
    @SerializedName("id")
    private String id_taskclass;
    private String nama_matpel;
    @SerializedName("judul")
    private String title;
    @SerializedName("deskripsi")
    private String description;
    @SerializedName("tipe")
    private String type;
    @SerializedName("tenggat")
    private String deadline;
    @SerializedName("created_at")
    private String date;
    @SerializedName("file")
    private String file_url;
    @SerializedName("completed_count")
    private int completedcount;

    protected ClassRoom(Parcel in) {
        id_taskclass = in.readString();
        nama_matpel = in.readString();
        title = in.readString();
        description = in.readString();
        type = in.readString();
        deadline = in.readString();
        date = in.readString();
        file_url = in.readString();
        completedcount = in.readInt();
    }

    public static final Creator<ClassRoom> CREATOR = new Creator<ClassRoom>() {
        @Override
        public ClassRoom createFromParcel(Parcel in) {
            return new ClassRoom(in);
        }

        @Override
        public ClassRoom[] newArray(int size) {
            return new ClassRoom[size];
        }
    };

    public ClassRoom() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId_taskclass() {
        return id_taskclass;
    }

    public void setId_taskclass(String id_taskclass) {
        this.id_taskclass = id_taskclass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getCompletedcount() {
        return completedcount;
    }

    public void setCompletedcount(int completedcount) {
        this.completedcount = completedcount;
    }


    public String getNama_matpel() {
        return nama_matpel;
    }

    public void setNama_matpel(String nama_matpel) {
        this.nama_matpel = nama_matpel;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_taskclass);
        dest.writeString(nama_matpel);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(deadline);
        dest.writeString(date);
        dest.writeString(file_url);
        dest.writeInt(completedcount);
    }
}
