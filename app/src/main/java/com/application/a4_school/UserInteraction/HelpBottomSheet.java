package com.application.a4_school.UserInteraction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.a4_school.R;


public class HelpBottomSheet extends Fragment {


    public static HelpBottomSheet newInstance(String param1, String param2) {
        HelpBottomSheet fragment = new HelpBottomSheet();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_bottom_sheet, container, false);
        return view;
    }
}