package com.application.a4_school.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.ClassRoom;
import com.application.a4_school.R;
import com.application.a4_school.UserInteraction.BottomSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ListViewHolder> {
    private List<ClassRoom> listClassMain;
    private Context context;
    private String[] headerContent;
    private String id_class;
    private String role;
    private OnItemClickCallback onItemClickCallback;

    public ClassListAdapter(List<ClassRoom> listClassMain, String[] headerContent, String id_class, String role,Context context) {
        this.listClassMain = listClassMain;
        this.context = context;
        this.headerContent = headerContent;
        this.id_class = id_class;
        this.role = role;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_class_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        if (position == 0) {
            holder.containerHeader.setVisibility(View.VISIBLE);
            holder.row_main_card.setClickable(false);
            holder.row_main_card.setForeground(null);
            holder.containerMain.setVisibility(View.GONE);
            holder.shlevel_header.setText(headerContent[1]);
            holder.shinfoclass_header.setText(headerContent[2]);
            holder.shCountmember_header.setText(headerContent[3] + " members");
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(listClassMain.get(holder.getAdapterPosition()));
                }
            });
            holder.containerHeader.setVisibility(View.GONE);
            holder.containerMain.setVisibility(View.VISIBLE);
        }
        if (!listClassMain.isEmpty()) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd MMM";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            Date date = null;
            String str = null;
            if (listClassMain.get(position).getDate() != null) {
                try {
                    date = inputFormat.parse(listClassMain.get(position).getDate());
                    str = outputFormat.format(date);
                    holder.shtgl.setText(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            holder.shtype.setText(listClassMain.get(position).getType());

            if (listClassMain.get(position).getType().equals("Theory")) {
                holder.shcompletedcount.setVisibility(View.INVISIBLE);
                holder.btnCheck.setVisibility(View.GONE);
            }
            holder.shtittle.setText(listClassMain.get(position).getTitle());

            if (role.equals("siswa")){
                holder.shcompletedcount.setVisibility(View.INVISIBLE);
                holder.btnCheck.setVisibility(View.INVISIBLE);
            }
            holder.shcompletedcount.setText(String.valueOf(listClassMain.get(position).getCompletedcount()) + " Completed this task");

            holder.shCountmember_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("openbottommembers", "clicked");
                    BottomSheet bottomSheet = new BottomSheet(id_class, "memberlist");
                    bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
                }
            });

            holder.btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheet bottomSheet = new BottomSheet(listClassMain.get(position).getId_taskclass(), "completedlist");
                    bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
                    onItemClickCallback.onItemClicked(listClassMain.get(holder.getAdapterPosition()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listClassMain.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(ClassRoom classRoomList);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView shtgl, shtype, shtittle, shcompletedcount, btnCheck, shlevel_header, shinfoclass_header, shCountmember_header, shBtnAssignment;
        ImageButton btnInfo;
        ConstraintLayout containerHeader, containerMain;
        CardView row_main_card;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            shtgl = itemView.findViewById(R.id.shtglclass);
            shtype = itemView.findViewById(R.id.shtasktheory);
            shtittle = itemView.findViewById(R.id.shtittleclass);
            shcompletedcount = itemView.findViewById(R.id.shcompletedtaskclass);
            btnCheck = itemView.findViewById(R.id.btn_checktaskclass);
            containerHeader = itemView.findViewById(R.id.content_main_header);
            containerMain = itemView.findViewById(R.id.content_main);
            shlevel_header = itemView.findViewById(R.id.shlevelclass_nav);
            shinfoclass_header = itemView.findViewById(R.id.shinfoclass_nav);
            shCountmember_header = itemView.findViewById(R.id.shclass_count_member);
            shBtnAssignment = itemView.findViewById(R.id.shclass_assignment_menu);
            btnInfo = itemView.findViewById(R.id.btn_info);
            row_main_card = itemView.findViewById(R.id.row_class_card_main);
        }
    }
}
