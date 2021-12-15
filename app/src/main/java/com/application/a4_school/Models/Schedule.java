package com.application.a4_school.Models;

import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("id_jadwal")
    private int id_schedule;
    private int background;
    @SerializedName("hari")
    private String days;
    private String jam_mulai;
    private String id_matpel;
    private String jam_selesai;
    private String ruangan;
    @SerializedName("mapel")
    private String nama_mapel;
    private String id_kelas;
    private String tingkatan;
    private String jurusan;

    public int getId_schedule() {
        return id_schedule;
    }

    public void setId_schedule(int id_schedule) {
        this.id_schedule = id_schedule;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getDays() {
        return days;
    }

    public String getRuangan() {
        return ruangan;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(String jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public String getJam_selesai() {
        return jam_selesai;
    }

    public void setJam_selesai(String jam_selesai) {
        this.jam_selesai = jam_selesai;
    }

    public String getNama_mapel() {
        return nama_mapel;
    }

    public void setNama_mapel(String nama_mapel) {
        this.nama_mapel = nama_mapel;
    }

    public String getTingkatan() {
        return tingkatan;
    }

    public String getId_kelas() {
        return id_kelas;
    }

    public String getId_matpel() {
        return id_matpel;
    }

    public void setTingkatan(String tingkatan) {
        this.tingkatan = tingkatan;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }
}
