package com.kapalert.kadunastategovernment.adapters;

import android.app.Dialog;
import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by win10 on 9/12/2017.
 */

public class AdapterFilesUploadedList extends RecyclerView.Adapter<AdapterFilesUploadedList.FileHolder> {

    private OnDownloadClickListener downloadClickListener;
    private Context mContext;
    private ArrayList<CaseDetailPOJO.FilesAndComments> mList;
    private Dialog mDialog;

    public AdapterFilesUploadedList(Context mContext, ArrayList<CaseDetailPOJO.FilesAndComments> mList, Dialog mDialog, OnDownloadClickListener downloadClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mDialog = mDialog;
        this.downloadClickListener = downloadClickListener;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_file_uploaded, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position) {
        final CaseDetailPOJO.FilesAndComments data = mList.get(position);

        holder.fileName.setText(String.format(mContext.getString(R.string.file_sent_pattern), data.file_name));
        if (data.fileType == null || data.fileType.isEmpty())
            holder.fileComment.setText(String.format(mContext.getString(R.string.comment_pattern), data.comments));
        else {
            if (data.fileType.equalsIgnoreCase("2")) {
                holder.fileComment.setText(mContext.getString(R.string.file_type_send));
            } else
                holder.fileComment.setText(mContext.getString(R.string.file_type_receive));
        }
        holder.downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadClickListener.downloadFile(data.file_name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class FileHolder extends RecyclerView.ViewHolder {

        TextView fileName, fileComment;
        ImageView downloadFile;

        public FileHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.file_name);
            fileComment = (TextView) itemView.findViewById(R.id.file_comment);
            downloadFile = (ImageView) itemView.findViewById(R.id.download_file);
        }
    }

    public interface OnDownloadClickListener {
        void downloadFile(String url);
    }
}
