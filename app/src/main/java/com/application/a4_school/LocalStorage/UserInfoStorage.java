package com.application.a4_school.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toolbar;

public class UserInfoStorage {
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private int mode = 0;
    private static final String APPNAME = "4-School";
    private static final String REFNAME = "userInfo";
    private static final String Id = "id";
    private static final String NipOrNis = "nip_or_nis";
    private static final String Name = "name";
    private static final String Email = "email";
    private static final String Image = "image";
    private static final String Birthday = "birthday";
    private static final String Id_class = "id_class";
    private static final String Profession = "profession";
    private Context context;

    public UserInfoStorage(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(REFNAME, mode);
        editor = sharedPreferences.edit();
    }
    public void createInfo(String name, String email, int id, String image, String id_classs, String profession, String birthday) {
        editor.putString(Name, name);
        editor.putString(Email, email);
        editor.putInt(Id, id);
        editor.putString(Image, image);
        editor.putString(Id_class, id_classs);
        editor.putString(Profession, profession);
        editor.putString(Birthday, birthday);
        editor.commit();
    }
    public void saveNisOrNip(String nisornip){
        editor.putString(NipOrNis, nisornip);
        editor.commit();
    }
    public void addPict(String url){
        editor.putString(Image, url);
        editor.commit();
    }
    public void savename(String value){
        editor.putString(Name, value);
        editor.commit();
    }
    public void saveEmail(String value){
        editor.putString(Email, value);
        editor.commit();
    }
    public void clearUser(){
        editor.clear();
        editor.commit();
    }
    public void setPreference(Context context){
        sharedPreferences = context.getSharedPreferences(APPNAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

}
