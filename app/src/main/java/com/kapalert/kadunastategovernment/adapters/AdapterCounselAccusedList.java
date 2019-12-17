package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.CounselAccusedListPOJO;

import java.util.ArrayList;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterCounselAccusedList extends RecyclerView.Adapter<AdapterCounselAccusedList.CounselCommentHolder> {

    private Context mContext;
    private ArrayList<CounselAccusedListPOJO.Accused> mList;

    public AdapterCounselAccusedList(Context mContext, ArrayList<CounselAccusedListPOJO.Accused> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CounselCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_accused_recommend, parent, false);
        return new CounselCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(final CounselCommentHolder holder, int position) {
        final CounselAccusedListPOJO.Accused data = mList.get(position);

        ArrayList<String> list = new ArrayList<>();
        list.add(mContext.getString(R.string.select_recommendation));
        list.add(mContext.getString(R.string.prosecute));
        list.add(mContext.getString(R.string.discharge));
        list.add(mContext.getString(R.string.further_investigation));

        holder.recommendation.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1, list));
        holder.recommendation.setSelection(data.selectedReccomendation);
        holder.recommendation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.selectedReccomendation = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.accusedName.setText(data.accuse_name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CounselCommentHolder extends RecyclerView.ViewHolder {

        TextView accusedName;
        Spinner recommendation;

        public CounselCommentHolder(View itemView) {
            super(itemView);

            accusedName = (TextView) itemView.findViewById(R.id.accused_name);
            recommendation = (Spinner) itemView.findViewById(R.id.accused_recommendation);
        }
    }

}
