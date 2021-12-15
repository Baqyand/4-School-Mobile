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

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionnListHolder> {
    private ArrayList<Help> listHelp;
    private OnitemClickCallbak onitemClickCallbak;

    public QuestionListAdapter(ArrayList<Help> list){
        this.listHelp = list;
    }

    public void setOnitemClickCallbak(OnitemClickCallbak onitemClickCallbak) {
        this.onitemClickCallbak = onitemClickCallbak;
    }

    @NonNull
    @Override
    public QuestionnListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_item, parent, false);
        return new QuestionnListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionnListHolder holder, final int position) {
        holder.title.setText(listHelp.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClickCallbak.onitem(listHelp.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHelp.size();
    }
    public interface OnitemClickCallbak {
        void onitem(Help help);
    }

    public class QuestionnListHolder extends RecyclerView.ViewHolder {
        TextView title;
        public QuestionnListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}
