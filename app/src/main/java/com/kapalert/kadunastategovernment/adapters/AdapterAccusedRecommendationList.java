package com.kapalert.kadunastategovernment.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;

import java.util.ArrayList;

/**
 * Created by win10 on 9/12/2017.
 */

public class AdapterAccusedRecommendationList extends RecyclerView.Adapter<AdapterAccusedRecommendationList.AccusedHolder> {

    private Context mContext;
    private ArrayList<CaseDetailPOJO.AccusedRecommendations> mList;
    private Dialog mDialog;

    public AdapterAccusedRecommendationList(Context mContext, ArrayList<CaseDetailPOJO.AccusedRecommendations> mList, Dialog mDialog) {
        this.mContext = mContext;
        this.mList = mList;
        this.mDialog = mDialog;
    }

    @Override
    public AccusedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_accused_recommendation, parent, false);
        return new AccusedHolder(view);
    }

    @Override
    public void onBindViewHolder(AccusedHolder holder, int position) {
        final CaseDetailPOJO.AccusedRecommendations data = mList.get(position);

        holder.accusedName.setText(String.format(mContext.getString(R.string.name_pattern), data.accuse_name));
        String accusedStr = "";
        if (data.recommendation.equalsIgnoreCase("1")) {
            accusedStr = mContext.getString(R.string.prosecute);
        } else if (data.recommendation.equalsIgnoreCase("2")) {
            accusedStr = mContext.getString(R.string.discharge);
        } else {
            accusedStr = mContext.getString(R.string.further_investigation);
        }
        holder.accusedRecommendation.setText(String.format(mContext.getString(R.string.accused_recommendation_pattern), accusedStr));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class AccusedHolder extends RecyclerView.ViewHolder {

        TextView accusedName, accusedRecommendation;

        public AccusedHolder(View itemView) {
            super(itemView);

            accusedName = (TextView) itemView.findViewById(R.id.accused_name);
            accusedRecommendation = (TextView) itemView.findViewById(R.id.accused_recommendation);
        }
    }
}
