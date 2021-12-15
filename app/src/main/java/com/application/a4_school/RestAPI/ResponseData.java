package com.application.a4_school.RestAPI;

import com.application.a4_school.Auth.sessionResp.UserInfo;
import com.application.a4_school.Models.ClassRoom;
import com.application.a4_school.Models.FilesUpload;
import com.application.a4_school.Models.Help;
import com.application.a4_school.Models.Members;
import com.application.a4_school.Models.Profession;
import com.application.a4_school.Models.Schedule;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
    private String image_url;
    private int last_page;
    private List<Schedule> jadwal_mengajar;
    private List<ClassRoom> index_class_siswa;
    private List<ClassRoom> index_class_guru;
    private List<Members> members;
    @SerializedName("kelas")
    private List<UserInfo> listMajors;
    private UserInfo information;
    private List<FilesUpload> filesDetail;
    private List<Members> completed_user;
    private List<Help> faq_list;
    private List<Profession> listProf;

    public List<Profession> getListProf() {
        return listProf;
    }

    public UserInfo getInformation() {
        return information;
    }

    public List<UserInfo> getListMajors() {
        return listMajors;
    }

    public List<Members> getCompleted_user() {
        return completed_user;
    }

    public void setListMajors(List<UserInfo> listMajors) {
        this.listMajors = listMajors;
    }

    public List<Help> getFaq_list() {
        return faq_list;
    }

    @SerializedName("message")
    private String messageJson;

    public List<FilesUpload> getFilesDetail() {
        return filesDetail;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public String getImage_url() {
        return image_url;
    }

    public List<Schedule> getJadwal_mengajar() {
        return jadwal_mengajar;
    }

    public List<ClassRoom> getIndex_class_guru() {
        return index_class_guru;
    }

    public int getLast_page() {
        return last_page;
    }

    public List<ClassRoom> getIndex_class_siswa() {
        return index_class_siswa;
    }

    public List<Members> getMembers() {
        return members;
    }
}
