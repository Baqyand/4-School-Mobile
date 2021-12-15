package com.application.a4_school.Models;

import com.application.a4_school.R;

import java.util.ArrayList;

public class HomeData {
  public static String[] judul = new  String []{
        "JOBS",
        "MAPS"
  };

  public static String[] detail = new String[]{
        "D.21 - XII RPL 2",
        "SMKN 4 Bandung"
  };

  public static int[] bghome = new int[]{
    R.drawable.job_illust_1,
    R.drawable.maps_illust_1
  };

  public static ArrayList<Home> getlisthome(){
    ArrayList<Home> home = new ArrayList<>();

    for (int i = 0; i < 2; i++){
      Home home1 = new Home();
      home1.setJudul(judul[i]);
      home1.setDetail(detail[i]);
      home1.setBghome(bghome[i]);
      home.add(home1);
    }

    return home;
  }

}
