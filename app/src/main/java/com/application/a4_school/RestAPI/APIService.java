package com.application.a4_school.RestAPI;

import android.util.Log;

import com.application.a4_school.Models.ClassRoom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface APIService {

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded",
    })
    @POST("login")
    Call<ResponseBody> login(@Field("email") String email,
                             @Field("password") String password,
                             @Field("fcm_token") String fcm_token);
    //@Field("fcm_token") String fcm_token);

    @POST("logout")
    Call<ResponseBody> logoutUser(@Query("id") int id_user,
                                  @Query("token") String logoutToken);

    @FormUrlEncoded
    @PATCH("GuruSchedule/nilai")
    Call<ResponseBody> uploadPoint(@Header("Authorization") String jwt_token, @Field("id_tugas[]") ArrayList<String> listId, @Field("nilai") int nilai);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(@Field("email") String email,
                                @Field("password") String pw,
                                @Field("name") String name);

    @POST("register")
    Call<JsonObject> cekRegist();

    @FormUrlEncoded
    @PATCH("upload/{id}")
    Call<ResponseBody> uploadBase64Pict(@Path("id") int id_user, @Field("photo") String encodedPhoto);

    @FormUrlEncoded
    @POST("users/create")
    Call<ResponseBody> register2(@Field("role") String role,
                                 @Field("name") String name,
                                 @Field("email") String email,
                                 @Field("password") String pw,
                                 @Field("nis") String nis,
                                 @Field("nip") String nip,
                                 @Field("id_kelas") String id_kelas,
                                 @Field("profesi") String profesi);

    @GET("SiswaSchedule")
    Call<ResponseStudent> getSiswaSchedule(@Query("id_kelas") String id_class, @Header("Authorization") String jwt_token);

    @GET("GuruSchedule")
    Call<ResponseData> getListSchedule(@Query("id") int id_user, @Header("Authorization") String jwt_token);

    @GET("classInfo")
    Call<JsonObject> getClassInformation(@Query("id_kelas") String id_class);

    @GET("SiswaSchedule/classRoomData")
    Call<ClassRoom> getClassData(@Query("id_kelas") String id_kelas, @Header("Authorization") String jwt_token);

    @FormUrlEncoded
    @POST("forgot")
    Call<JsonObject> sendMailToken(@Field("email") String email);

    @Multipart
    @POST("GuruSchedule/create_tugas/{id_jadwal}")
    Call<ResponseBody> uploadTaskTheory(@Header("Authorization") String jwt_token,
                                        @Path("id_jadwal") int id_schedule,
                                        @Part("judul") RequestBody title,
                                        @Part("deskripsi") RequestBody description,
                                        @Part("tipe") RequestBody type,
                                        @Part("tenggat") RequestBody deadline,
                                        @Part MultipartBody.Part[] file);

    @Multipart
    @POST("SiswaSchedule/index_classroom_siswa/assign/{id_tugas_kelas}")
    Call<ResponseBody> assignTask(@Header("Authorization") String jwt_token,
                                  @Path("id_tugas_kelas") String id_task,
                                  @Part("id_siswa") RequestBody id_siswa,
                                  @Part("status") RequestBody status,
                                  @Part MultipartBody.Part[] file);

    @FormUrlEncoded
    @PATCH("update_Profile/{id_user}")
    Call<ResponseBody> updateProfile(@Path("id_user") int id_user,
                                     @Field("name") String name,
                                     @Field("nip") String nip,
                                     @Field("nis") String nis,
                                     @Field("kelas") String kelas,
                                     @Field("profesi") String profesi,
                                     @Field("tanggal_tanggal") String tanggal_lahir);

    @FormUrlEncoded
    @POST("resetpassword")
    Call<JsonObject> resetPassword(@Field("email") String email, @Field("password") String password, @Field("token") String accessToken);

    @GET("GuruSchedule/checkTask")
    Call<ResponseData> checkTask(@Header("Authorization") String jwt_token, @Query("id_tugas_kelas") String id_task);

    @GET("GuruSchedule/index_classroom_guru/{id_jadwal}")
        //inidirubah id_jadwal
    Call<ResponseData> getListClassItemGuru(@Path("id_jadwal") int id_schedule, @Header("Authorization") String jwt_token);

    @GET("SiswaSchedule/index_classroom_siswa/{id_jadwal}")
    Call<ResponseData> getListClassItemSiswa(@Path("id_jadwal") int id_schedule, @Header("Authorization") String jwt_token);

    @GET("index_classroom/memberclass")
    Call<ResponseData> getListMembersClass(@Query("id_kelas") String id_class, @Query("page") int page);

    @GET("userinformation")
    Call<ResponseData> getUserInfo(@Query("id") int id_user);

    @GET("ShowKelas")
    Call<ResponseData> getMajors(@Query("tingkatan") String classlevel);

    @GET("index_classroom/file/{id_tugas}")
    Call<ResponseData> getListFiles(@Path("id_tugas") String id_taskclass, @Query("id_siswa") int id_siswa, @Query("condition") String condition);

    @GET("faq-content")
    Call<ResponseData> gethelp();

    @Streaming
    @GET()
    Call<ResponseBody> downloadFile(@Url String url);

}
