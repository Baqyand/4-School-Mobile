package com.application.a4_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.ClassRoom;
import com.application.a4_school.Models.Members;
import com.application.a4_school.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberClassListAdapter extends RecyclerView.Adapter<MemberClassListAdapter.MyViewHolder> {
    private List<Members> list;
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public MemberClassListAdapter(List<Members> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_memberclass_btmsheet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getPhoto()).placeholder(R.drawable.empty_profile).into(holder.imgMemberList);
        holder.nameMembers.setText(list.get(position).getName());
        holder.nisMembers.setText(list.get(position).getNis());

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

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    public interface OnItemClickCallback {
        void onItemClicked(Members memberCompletedTask);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgMemberList;
        TextView nameMembers, nisMembers;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMemberList   = itemView.findViewById(R.id.member_image);
            nameMembers     = itemView.findViewById(R.id.memberclass_name);
            nisMembers      = itemView.findViewById(R.id.memberclass_nis);
        }
    }
}
