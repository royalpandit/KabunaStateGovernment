package com.kapalert.kadunastategovernment.response;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.OffenceModal;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterOffenceNatureList1 extends RecyclerView.Adapter<AdapterOffenceNatureList1.OffenceNatureHolder> {

    private Context mContext;
    private ArrayList<ActionModel> mList;

    public AdapterOffenceNatureList1(Context mContext, ArrayList<ActionModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public OffenceNatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_users, parent, false);
        return new OffenceNatureHolder(view);
    }

    @Override
    public void onBindViewHolder(final OffenceNatureHolder holder, int position) {
        final ActionModel data = mList.get(position);

        holder.name.setText(data.getName());
        holder.selected.setChecked(data.isSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.selected.performClick();
            }
        });
        holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class OffenceNatureHolder extends RecyclerView.ViewHolder {

        TextView name;
        AppCompatCheckBox selected;

        public OffenceNatureHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.user_name);
            selected = (AppCompatCheckBox) itemView.findViewById(R.id.user_check);
        }
    }
}
