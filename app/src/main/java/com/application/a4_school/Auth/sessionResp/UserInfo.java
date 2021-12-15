package com.application.a4_school.Auth.sessionResp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable {
    private int id;
    private String nip;
    private String nis;
    @SerializedName("id_kelas")
    private String id_class;
    private String name;
    private String email;
    private String role;
    private String photo;
    @SerializedName("tingkatan")
    private String classLevel;
    @SerializedName("jurusan")
    private  String majors;
    @SerializedName("tanggal_lahir")
    private String birthday;
    private String fcm_token;
    @SerializedName("profesi")
    private String profession;
    private String condition;

    protected UserInfo(Parcel in) {
        id = in.readInt();
        nip = in.readString();
        nis = in.readString();
        id_class = in.readString();
        name = in.readString();
        email = in.readString();
        role = in.readString();
        photo = in.readString();
        classLevel = in.readString();
        majors = in.readString();
        birthday = in.readString();
        fcm_token = in.readString();
        profession = in.readString();
    }

    public UserInfo(String condition) {
        this.condition = condition;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getClassLevel() {
        return classLevel;
    }

    public String getMajors() {
        return majors;
    }

    public UserInfo() {
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId_class() {
        return id_class;
    }

    public void setId_class(String id_class) {
        this.id_class = id_class;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getId() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getNis() {
        return nis;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPhoto() {
        return photo;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nip);
        parcel.writeString(nis);
        parcel.writeString(id_class);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(role);
        parcel.writeString(photo);
        parcel.writeString(classLevel);
        parcel.writeString(majors);
        parcel.writeString(birthday);
        parcel.writeString(fcm_token);
        parcel.writeString(profession);
    }
    
    public String toString(){
        return majors;
    }
}
