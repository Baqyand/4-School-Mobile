package com.application.a4_school.Models;

import java.util.ArrayList;

public class ClassRoomData {
    public static String[] id_classRoom = new String[]{
            "0"
    };

    public static String[] date = new String[]{
            ""
    };

    public static String[] type = new String[]{
            ""
    };

    public static String[] tittle = new String[]{
            ""
    };

    public static int[] completedcount = new int[]{
            0
    };

    public static ArrayList<ClassRoom> getlistClassroom(){
        ArrayList<ClassRoom> list = new ArrayList<>();

        for (int i = 0; i<tittle.length; i++){
            ClassRoom classRoom = new ClassRoom();
            classRoom.setId_taskclass(id_classRoom[i]);
            classRoom.setDate(date[i]);
            classRoom.setType(type[i]);
            classRoom.setTitle(tittle[i]);
            classRoom.setCompletedcount(completedcount[i]);
            list.add(classRoom);
        }
        return list;
    }
}
