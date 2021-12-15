package com.application.a4_school.Models;

import com.application.a4_school.R;

import java.util.ArrayList;

public class ScheduleData {
    public static String[]days = new String[]{
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
    };
    public static int[] background = new int[]{
            R.drawable.bg_content_1,
            R.drawable.bg_content_2,
            R.drawable.bg_content_3,
            R.drawable.bg_content_4,
            R.drawable.bg_content_5,
            R.drawable.bg_content_5,
    };
    public static ArrayList<Schedule> getListData(){
        ArrayList<Schedule> list = new ArrayList<>();
        for (int i=0; i<days.length; i++) {
            Schedule sch = new Schedule();
            sch.setDays(days[i]);
            sch.setBackground(background[i]);
            list.add(sch);
        }
        return list;
    }
}
