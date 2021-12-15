package com.application.a4_school.Models;

import com.google.gson.annotations.SerializedName;

public class Help {
    @SerializedName("id")
    String id;
    @SerializedName("pertanyaan")
    String title;
    @SerializedName("jawaban")
    String answer;
    @SerializedName("kategori")
    String kategori;

    public Help(String id, String title, String answer, String kategori){
        this.title  = title;
        this.id = id;
        this.kategori = kategori;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
