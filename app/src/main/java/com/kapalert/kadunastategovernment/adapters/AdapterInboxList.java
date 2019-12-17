package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.ChatListActivity;
import com.kapalert.kadunastategovernment.templates.InboxListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterInboxList extends RecyclerView.Adapter<AdapterInboxList.ChatGroupHolder> {

    private Context mContext;
    private ArrayList<InboxListPOJO.ActiveChat> mList;

    public AdapterInboxList(Context mContext, ArrayList<InboxListPOJO.ActiveChat> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ChatGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_inbox, parent, false);
        return new ChatGroupHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatGroupHolder holder, int position) {
        final InboxListPOJO.ActiveChat data = mList.get(position);

        holder.name.setText(Utils.fromHtml(data.getCompleteName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TinyDB(mContext).putString(Constants.DB_SELECTED_CHAT, new Gson().toJson(data));
                new TinyDB(mContext).remove(Constants.DB_SELECTED_CHAT_NEW);
                Utils.intentTo(mContext, ChatListActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ChatGroupHolder extends RecyclerView.ViewHolder {

        TextView name/*, lastMessage*/;

        public ChatGroupHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.inbox_name);
//            lastMessage = (TextView) itemView.findViewById(R.id.inbox_last_message);
        }
    }
}
