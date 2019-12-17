package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.EditEvent;
import com.kapalert.kadunastategovernment.templates.EventListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.PartMap;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterEventsList extends RecyclerView.Adapter<AdapterEventsList.EventHolder> {

    private Context mContext;
    private ArrayList<EventListPOJO.EventData> mList;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aaa");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private UserInfoJson.UserData loggedInUser;

    public AdapterEventsList(Context mContext, ArrayList<EventListPOJO.EventData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.loggedInUser = Constants.getUser(mContext);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_events, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, int position) {
        final EventListPOJO.EventData data = mList.get(position);

        try {
            Date startDate = Constants.SET_DATE_FORMAT.parse(data.start);
            Date endDate = Constants.SET_DATE_FORMAT.parse(data.end);

            holder.start.setText(String.format(mContext.getString(R.string.start_event_pattern), timeFormat.format(startDate)));
            holder.day.setText(dayFormat.format(startDate));
            holder.year.setText(yearFormat.format(startDate));
            holder.month.setText(monthFormat.format(startDate));
            holder.end.setText(String.format(mContext.getString(R.string.end_event_pattern), timeFormat.format(endDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.userid.equalsIgnoreCase(loggedInUser.id)) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        holder.desc.setText(data.description);
        holder.name.setText(data.title);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDeleteAPI(data);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TinyDB(mContext).putString(Constants.DB_SELECTED_EVENT, new Gson().toJson(data));
                Utils.intentToActivity(mContext, EditEvent.class);
            }
        });

        holder.desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showMessageDialog(mContext, mContext.getString(R.string.description), data.description);
            }
        });
    }

    private void eventDeleteAPI(final EventListPOJO.EventData data) {
        Utils.showDialog(mContext, mContext.getString(R.string.are_you_sure), mContext.getString(R.string.delete_event), mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new ServerRequest<UserInfoJson>(mContext, Constants.getEventDeleteUrl(mContext, data.ID), true) {
                    @Override
                    public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                        if (response.body().status) {
                            Utils.showToast(mContext, response.body().message);
                            mList.remove(data);
                            notifyDataSetChanged();
                        } else {
                            Utils.showToast(mContext, response.body().errorMessage);
                        }
                    }
                };
            }
        }, null, mContext.getString(R.string.no), null, null, true);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {

        TextView day, start, end, desc, year, month, name;
        ImageView delete, edit;

        public EventHolder(View itemView) {
            super(itemView);

            year = (TextView) itemView.findViewById(R.id.event_year);
            name = (TextView) itemView.findViewById(R.id.event_name);
            month = (TextView) itemView.findViewById(R.id.event_month);
            day = (TextView) itemView.findViewById(R.id.event_day);
            start = (TextView) itemView.findViewById(R.id.event_start);
            end = (TextView) itemView.findViewById(R.id.event_end);
            desc = (TextView) itemView.findViewById(R.id.event_desc);
            delete = (ImageView) itemView.findViewById(R.id.event_delete);
            edit = (ImageView) itemView.findViewById(R.id.event_edit);
        }
    }
}
