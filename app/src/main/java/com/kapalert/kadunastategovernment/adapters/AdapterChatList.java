package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.ChatListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by win10 on 6/23/2017.
 */

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.GroupConversationHolder> {

    private Context mContext;
    private ArrayList<ChatListPOJO.ChatMessage> mList;
    private TimeZone timeZone;
    private UserInfoJson.UserData currentUser;

    public AdapterChatList(Context mContext, ArrayList<ChatListPOJO.ChatMessage> mList) {
        this.mContext = mContext;
        this.mList = mList;
        timeZone = TimeZone.getDefault();
        currentUser = Constants.getUser(mContext);
    }

    @Override
    public GroupConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_chat, parent, false);
        return new GroupConversationHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupConversationHolder holder, int position) {
        ChatListPOJO.ChatMessage data = mList.get(position);

        long timestamp =  data.timestamp * 1000L;
        String time = "";
        try {
            Calendar messageDate = Calendar.getInstance();
            messageDate.setTimeInMillis(timestamp);
            Calendar todayDate = Calendar.getInstance();

//            messageDate.setTimeZone(timeZone);
//            todayDate.setTimeZone(timeZone);

            if (messageDate.get(Calendar.DAY_OF_YEAR) == todayDate.get(Calendar.DAY_OF_YEAR) && messageDate.get(Calendar.YEAR) == todayDate.get(Calendar.YEAR)) {
                time = "today " + new SimpleDateFormat("HH:mm").format(messageDate.getTime());
            } else {
                time = new SimpleDateFormat("dd MMM yy HH:mm").format(messageDate.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.userid.equalsIgnoreCase(currentUser.id)) {
            holder.myContainer.setVisibility(View.VISIBLE);
            holder.senderContainer.setVisibility(View.GONE);
            holder.myName.setText(currentUser.getCompleteName());
            holder.myMessage.setText(data.message);
            holder.myTime.setText(time);
        } else {
            holder.myContainer.setVisibility(View.GONE);
            holder.senderContainer.setVisibility(View.VISIBLE);
            holder.senderName.setText(data.getCompleteName());
            holder.senderMessage.setText(data.message);
            holder.senderTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class GroupConversationHolder extends RecyclerView.ViewHolder {

        TextView senderMessage, senderName, myName, myMessage, senderTime, myTime;
        LinearLayout senderContainer, myContainer;

        public GroupConversationHolder(View itemView) {
            super(itemView);

            senderMessage = (TextView) itemView.findViewById(R.id.sender_message);
            myName = (TextView) itemView.findViewById(R.id.my_name);
            myTime = (TextView) itemView.findViewById(R.id.my_time);
            senderTime = (TextView) itemView.findViewById(R.id.sender_time);
            senderName = (TextView) itemView.findViewById(R.id.sender_name);
            myMessage = (TextView) itemView.findViewById(R.id.my_message);
            senderContainer = (LinearLayout) itemView.findViewById(R.id.sender_container);
            myContainer = (LinearLayout) itemView.findViewById(R.id.my_container);
        }
    }
}
