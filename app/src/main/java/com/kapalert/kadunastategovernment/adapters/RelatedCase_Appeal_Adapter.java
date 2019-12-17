package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.Bean_RelatedCase_POJO;
import com.kapalert.kadunastategovernment.utils.TinyDB;

import java.util.List;

import static com.kapalert.kadunastategovernment.activities.Create_AppealCase_Activity.recycle_relatedCase_list;
import static com.kapalert.kadunastategovernment.activities.Create_AppealCase_Activity.tv_relatedCase;
import static com.kapalert.kadunastategovernment.utils.Constants.APPEAL_RELATED_CASE_ID;

public class RelatedCase_Appeal_Adapter extends RecyclerView.Adapter<RelatedCase_Appeal_Adapter.MyViewHolder>
{
    Context mContext;
    List<Bean_RelatedCase_POJO> relatedCase_pojoList;
    TinyDB sessionManager;
    public RelatedCase_Appeal_Adapter(Context mContext, List<Bean_RelatedCase_POJO> relatedCase_pojoList)
    {
        this.mContext = mContext;
        this.relatedCase_pojoList = relatedCase_pojoList;
        sessionManager = new TinyDB(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note_case, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        String  caseName = relatedCase_pojoList.get(position).getRelatedCase_name();
       holder.tv_name.setText(caseName);

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_relatedCase.setText(caseName);
                sessionManager.putString(APPEAL_RELATED_CASE_ID,relatedCase_pojoList.get(position).getRelatedCase_id());
                recycle_relatedCase_list.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return relatedCase_pojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.notes_data);

        }
    }
}
