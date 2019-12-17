package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.AnnouncementListPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterAnnouncementList extends RecyclerView.Adapter<AdapterAnnouncementList.NotificationHolder> {

    private Context mContext;
    private ArrayList<AnnouncementListPOJO.Announcement> mList;

    public AdapterAnnouncementList(Context mContext, ArrayList<AnnouncementListPOJO.Announcement> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_announcement, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {
        AnnouncementListPOJO.Announcement data = mList.get(position);

        holder.text.setText(data.message);
        String time = "";
        try {
            time = Utils.convertDateToDisplayDate(Constants.SET_DATE_FORMAT.parse(data.created_at));
        } catch (Exception e) {
            e.printStackTrace();
            time = data.created_at;
        }

        holder.date.setText(time);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        TextView text, date;

        public NotificationHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.notification_text);
            date = (TextView) itemView.findViewById(R.id.notification_date);
        }
    }
}
