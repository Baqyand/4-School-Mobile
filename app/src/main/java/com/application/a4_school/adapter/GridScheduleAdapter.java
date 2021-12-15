package com.application.a4_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.Schedule;
import com.application.a4_school.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class GridScheduleAdapter extends RecyclerView.Adapter<GridScheduleAdapter.GridViewHolder> {
    private List<Schedule> listSchedule;
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public GridScheduleAdapter(ArrayList<Schedule> list, Context context){
        this.listSchedule = list;
        this.context = context;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_schedule, viewGroup, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, int position) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            holder.background.setBackgroundDrawable(ContextCompat.getDrawable(context, listSchedule.get(position).getBackground()) );
        } else {
            holder.background.setBackground(ContextCompat.getDrawable(context, listSchedule.get(position).getBackground()));
        }

        holder.days.setText(listSchedule.get(position).getDays());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listSchedule.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSchedule.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Schedule dataSchedule);
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout background;
        TextView days;

        GridViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.grid);
            days = itemView.findViewById(R.id.days);
        }
    }
}
