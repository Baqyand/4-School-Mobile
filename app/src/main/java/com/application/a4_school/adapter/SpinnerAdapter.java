package com.application.a4_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.application.a4_school.R;

public class SpinnerAdapter extends BaseAdapter {
    private String[] list;
    private LayoutInflater inflater;


    public SpinnerAdapter(Context context, String[] list){
        this.list = list;
        inflater = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_task, null);

        TextView task = view.findViewById(R.id.task);
        task.setText(list[position]);
        return view;
    }
}
