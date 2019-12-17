package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterUserList extends RecyclerView.Adapter<AdapterUserList.UserHolder> {

    private Context mContext;
    private ArrayList<UserListPOJO.UserDetail> mList;

    public AdapterUserList(Context mContext, ArrayList<UserListPOJO.UserDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_users, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        final UserListPOJO.UserDetail data = mList.get(position);

        holder.name.setText(data.getCompleteName());
        holder.select.setChecked(data.isSelected());

        holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSelected(isChecked);
                mList.set(position, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        TextView name;
        AppCompatCheckBox select;

        public UserHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.user_name);
            select = (AppCompatCheckBox) itemView.findViewById(R.id.user_check);
        }
    }
}
