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
import com.kapalert.kadunastategovernment.templates.PoliceFileListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterPoliceFileList extends RecyclerView.Adapter<AdapterPoliceFileList.PoliceFileHolder> {

    private Context mContext;
    private ArrayList<PoliceFileListPOJO.PoliceFile> mList;

    public AdapterPoliceFileList(Context mContext, ArrayList<PoliceFileListPOJO.PoliceFile> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public PoliceFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_file_police, parent, false);
        return new PoliceFileHolder(view);
    }

    @Override
    public void onBindViewHolder(PoliceFileHolder holder, int position) {
        final PoliceFileListPOJO.PoliceFile data = mList.get(position);

        holder.fileName.setText(data.file_name);
        holder.fileType.setText((data.get_type.equalsIgnoreCase("2") ? mContext.getString(R.string.file_type_send) : mContext.getString(R.string.file_type_receive)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PoliceFileHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        TextView fileType;

        public PoliceFileHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.file_name);
            fileType = (TextView) itemView.findViewById(R.id.file_type);
        }
    }

}
