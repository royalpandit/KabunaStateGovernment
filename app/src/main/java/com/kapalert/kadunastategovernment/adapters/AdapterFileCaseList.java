package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.FileListActivity;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterFileCaseList extends RecyclerView.Adapter<AdapterFileCaseList.FileCaseHolder> {

    private Context mContext;
    private ArrayList<CaseListPOJO.CaseList> mList;

    public AdapterFileCaseList(Context mContext, ArrayList<CaseListPOJO.CaseList> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public FileCaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_file_case, parent, false);

        return new FileCaseHolder(view);
    }

    @Override
    public void onBindViewHolder(FileCaseHolder holder, int position) {

        final CaseListPOJO.CaseList data = mList.get(position);

        holder.fileNum.setText(String.format(mContext.getString(R.string.file_count_pattern), data.fileCount));
        holder.caseId.setText(String.format(mContext.getString(R.string.case_id_pattern), data.id));
        holder.caseName.setText(String.format(mContext.getString(R.string.case_name_pattern), data.file_name));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TinyDB(mContext).putString(Constants.DB_SELECTED_FILE_CASE, new Gson().toJson(data));
                Utils.intentToActivity(mContext, FileListActivity.class);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FileCaseHolder extends RecyclerView.ViewHolder {

        TextView caseId, caseName, fileNum;

        public FileCaseHolder(View itemView) {
            super(itemView);

            caseId = (TextView) itemView.findViewById(R.id.case_id);
            fileNum = (TextView) itemView.findViewById(R.id.case_files_number);
            caseName = (TextView) itemView.findViewById(R.id.case_name);
        }
    }
}
