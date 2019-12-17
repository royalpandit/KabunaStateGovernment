package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.AdjournmentDataJSON;
import com.kapalert.kadunastategovernment.templates.CommonListPOJO;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterAdjournmentList extends RecyclerView.Adapter<AdapterAdjournmentList.AdjournmentHolder> {

    private Context mContext;
    private ArrayList<AdjournmentDataJSON.AdjournmentData> mList;
    private List<CommonListPOJO.CommonList> adjournmentList;

    public AdapterAdjournmentList(Context mContext, ArrayList<AdjournmentDataJSON.AdjournmentData> mList, List<CommonListPOJO.CommonList> adjournmentReasonList) {
        this.mContext = mContext;
        this.mList = mList;
        this.adjournmentList = adjournmentReasonList;
    }

    @Override
    public AdjournmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_adjournment, parent, false);
        return new AdjournmentHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdjournmentHolder holder, int position) {
        final AdjournmentDataJSON.AdjournmentData data = mList.get(position);

        holder.hearingDate.setText(String.format(mContext.getString(R.string.hearing_date_pattern), data.getHearingDate()));
        holder.newHearingDate.setText(String.format(mContext.getString(R.string.new_hearing_date_pattern), data.getNewHearingDate()));
        holder.progressMade.setText(String.format(mContext.getString(R.string.reasonable_progress_made_pattern), data.getProgressMade()));
        for (CommonListPOJO.CommonList reason : adjournmentList) {
            if (reason.id.equalsIgnoreCase(data.adjourmentReasonId)) {
                holder.adjournmentReason.setText(String.format(mContext.getString(R.string.adjournment_reason_pattern), reason.name));
                break;
            }
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(mContext, mContext.getString(R.string.delete_adjournment_message), mContext.getString(R.string.are_you_sure), mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(data);
                        notifyDataSetChanged();
                    }
                }, null, mContext.getString(R.string.no), null, null, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdjournmentHolder extends RecyclerView.ViewHolder {

        TextView hearingDate, newHearingDate, adjournmentReason, progressMade;
        ImageView delete;

        public AdjournmentHolder(View itemView) {
            super(itemView);

            hearingDate = (TextView) itemView.findViewById(R.id.hearing_date);
            newHearingDate = (TextView) itemView.findViewById(R.id.new_hearing_date);
            adjournmentReason = (TextView) itemView.findViewById(R.id.adjournment_reason);
            progressMade = (TextView) itemView.findViewById(R.id.progress_made);
            delete = (ImageView) itemView.findViewById(R.id.accused_delete);
        }
    }
}
