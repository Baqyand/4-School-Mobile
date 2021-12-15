package com.application.a4_school.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.Help;
import com.application.a4_school.R;

import java.util.ArrayList;

public class HelpViewAdapter extends RecyclerView.Adapter<HelpViewAdapter.HelpViewModel> {

    ArrayList<Help> list;

    public  HelpViewAdapter(ArrayList<Help> data){
        this.list = data;
    }

    @NonNull
    @Override
    public HelpViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_item, parent, false);
        return new HelpViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewModel holder, int position) {
        list.get(position);
        holder.title.setText(list.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HelpViewModel extends RecyclerView.ViewHolder {
        TextView title;
        public HelpViewModel(@NonNull View itemView) {
            super(itemView);

            title  = itemView.findViewById(R.id.tv_title);

        }
    }
}
