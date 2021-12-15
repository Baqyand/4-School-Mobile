package com.application.a4_school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.a4_school.Models.FilesUpload;
import com.application.a4_school.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

public class ClassFilesAdapter extends RecyclerView.Adapter<ClassFilesAdapter.ListViewHolder> {
    private List<FilesUpload> listFiles;
    private Context context;
    private OnItemClickCallback onItemClickCallback;
    private String condition;

    public ClassFilesAdapter(List<FilesUpload> listFiles, Context context, String condition) {
        this.listFiles = listFiles;
        this.context = context;
        this.condition = condition;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_files_selected, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        switch (listFiles.get(position).getTypefile()) {
            case "ai":
                setholderIcon(holder, R.drawable.ext_ai);
                break;
            case "bmp":
                setholderIcon(holder, R.drawable.ext_bmp);
                break;
            case "cdr":
                setholderIcon(holder, R.drawable.ext_cdr);
                break;
            case "css":
                setholderIcon(holder, R.drawable.ext_css);
                break;
            case "doc":
            case "docx":
                setholderIcon(holder, R.drawable.ext_doc);
                break;
            case "flv":
                setholderIcon(holder, R.drawable.ext_flv);
                break;
            case "gif":
                setholderIcon(holder, R.drawable.ext_gif);
                break;
            case "html":
                setholderIcon(holder, R.drawable.ext_html);
                break;
            case "jpg":
            case "jpeg":
                setholderIcon(holder, R.drawable.ext_jpg);
                break;
            case "js":
                setholderIcon(holder, R.drawable.ext_js);
                break;
            case "mov":
                setholderIcon(holder, R.drawable.ext_mov);
                break;
            case "mp3":
                setholderIcon(holder, R.drawable.ext_mp3);
                break;
            case "mpg":
                setholderIcon(holder, R.drawable.ext_mpg);
                break;
            case "pdf":
                setholderIcon(holder, R.drawable.ext_pdf);
                break;
            case "php":
                setholderIcon(holder, R.drawable.ext_php);
                break;
            case "png":
                setholderIcon(holder, R.drawable.ext_png);
                break;
            case "ppt":
            case "pptx":
                setholderIcon(holder, R.drawable.ext_ppt);
                break;
            case "ps":
                setholderIcon(holder, R.drawable.ext_ps);
                break;
            case "psd":
                setholderIcon(holder, R.drawable.ext_psd);
                break;
            case "sql":
                setholderIcon(holder, R.drawable.ext_sql);
                break;
            case "svg":
                setholderIcon(holder, R.drawable.ext_svg);
                break;
            case "txt":
                setholderIcon(holder, R.drawable.ext_txt);
                break;
            case "xlsx":
            case "xls":
                setholderIcon(holder, R.drawable.ext_xlsx);
                break;
            case "zip":
                setholderIcon(holder, R.drawable.ext_zip);
                break;
            default:
                setholderIcon(holder, R.drawable.ext_undefined);
                break;
        }
        holder.txtFilename.setText(listFiles.get(position).getNamefile());
        if (condition.equals("form")) {
            holder.btnDeleteFiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listFiles.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), listFiles.size());
                }
            });
        }else{
            holder.btnDeleteFiles.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listFiles.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFiles.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(FilesUpload filesUpload, int index);
    }

    public RequestManager setholderIcon(ListViewHolder holder, int drawable) {
        RequestManager thumbnailpreview = Glide.with(context);
        thumbnailpreview.load(drawable).into(holder.imgThumbnail);
        return thumbnailpreview;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView txtFilename;
        ImageView imgThumbnail;
        ImageButton btnDeleteFiles;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDeleteFiles = itemView.findViewById(R.id.btn_delete_files_upload);
            txtFilename = itemView.findViewById(R.id.txt_thumb_filename);
            imgThumbnail = itemView.findViewById(R.id.img_thumb_files);
        }
    }

}
