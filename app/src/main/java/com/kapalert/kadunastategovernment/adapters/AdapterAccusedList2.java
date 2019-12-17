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
import com.kapalert.kadunastategovernment.activities.CreateCaseActivity;
import com.kapalert.kadunastategovernment.activities.CreateCivilCaseActivity;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterAccusedList2 extends RecyclerView.Adapter<AdapterAccusedList2.AccusedHolder> {

    private Context mContext;
    private ArrayList<CreateCivilCaseActivity.AccusedModal2> mList;

    public AdapterAccusedList2(Context mContext, ArrayList<CreateCivilCaseActivity.AccusedModal2> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public AccusedHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_accused, parent, false);
        return new AccusedHolder(view);
    }

    @Override
    public void onBindViewHolder(final AccusedHolder holder, int position) {
        final CreateCivilCaseActivity.AccusedModal2 data = mList.get(position);

        holder.name.setText(data.getName());
        holder.age.setText(data.getAge());
        holder.gender.setText(data.getPlan());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(mContext, mContext.getString(R.string.delete_accused_message), mContext.getString(R.string.are_you_sure), mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(data);
                        notifyDataSetChanged();
                    }
                },null,mContext.getString(R.string.no),null,null,true);
            }
        });

    }

    @Override



    public int getItemCount() {
        return mList.size();
    }

    public class AccusedHolder extends RecyclerView.ViewHolder {

        TextView name, age,gender;
        ImageView delete;

        public AccusedHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.accused_name);
            age = (TextView) itemView.findViewById(R.id.accused_age);
            gender=(TextView)itemView.findViewById(R.id.accused_gender);
            delete = (ImageView) itemView.findViewById(R.id.accused_delete);
        }
    }
}
