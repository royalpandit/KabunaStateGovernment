package com.kapalert.kadunastategovernment.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.ChatListActivity;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterChatUserList extends RecyclerView.Adapter<AdapterChatUserList.UserHolder> {

    private Context mContext;
    private ArrayList<UserListPOJO.UserDetail> mList;
    private Dialog dialog;

    public AdapterChatUserList(Context mContext, ArrayList<UserListPOJO.UserDetail> mList, Dialog dialog) {
        this.mContext = mContext;
        this.mList = mList;
        this.dialog = dialog;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_chat_users, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        final UserListPOJO.UserDetail data = mList.get(position);

        holder.name.setText(data.getCompleteName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new TinyDB(mContext).putString(Constants.DB_SELECTED_CHAT_NEW, new Gson().toJson(data));
                new TinyDB(mContext).remove(Constants.DB_SELECTED_CHAT);
                Utils.intentTo(mContext, ChatListActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        TextView name;

        public UserHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }
}
