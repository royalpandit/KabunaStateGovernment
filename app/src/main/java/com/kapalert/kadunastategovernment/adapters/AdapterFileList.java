package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.FileListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterFileList extends RecyclerView.Adapter<AdapterFileList.FileHolder> {

    private Context mContext;
    private ArrayList<FileListPOJO.CaseFile> mList;
    private DownloadClickListener mDownloadListener;

    public AdapterFileList(Context mContext, ArrayList<FileListPOJO.CaseFile> mList, DownloadClickListener mDownloadListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mDownloadListener = mDownloadListener;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position) {
        final FileListPOJO.CaseFile data = mList.get(position);

        holder.fileName.setText(data.file_name);
        holder.fileDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadListener.downloadClicked(Constants.getCaseFileUrlStr(data.case_id, data.file_name));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FileHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        ImageView fileDownload;

        public FileHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.file_name);
            fileDownload = (ImageView) itemView.findViewById(R.id.file_download);
        }
    }

    public interface DownloadClickListener {
        public void downloadClicked(String url);
    }
}
