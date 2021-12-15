package com.application.a4_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.Schedule;
import com.application.a4_school.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ListViewHolder>{
    private List<Schedule> list;
    private Context context;
    String role;
    private GridScheduleAdapter.OnItemClickCallback onItemClickCallback;

    public ScheduleListAdapter(List<Schedule> list, String role,Context context) {
        this.list = list;
        this.context = context;
        this.role = role;
    }

    public void setOnItemClickCallback(GridScheduleAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule_bottom_sheet, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        if (list.get(position) != null){
            if (role.equals("guru")){
                holder.shClass.setText(list.get(position).getTingkatan()+ " " +list.get(position).getJurusan());
            }else{
                holder.shClass.setText(list.get(position).getNama_mapel());
            }
            holder.shTime.setText(list.get(position).getJam_mulai()+" - "+list.get(position).getJam_selesai());
            holder.shRoom.setText(list.get(position).getRuangan());

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView shTime, shRoom, shClass;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            shTime = itemView.findViewById(R.id.time_schedule);
            shRoom = itemView.findViewById(R.id.room_schedule);
            shClass = itemView.findViewById(R.id.class_schedule);
        }
    }
}
