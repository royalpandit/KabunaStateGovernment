package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.OffenceModal;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;

import java.util.ArrayList;

/**
 * Created by win10 on 8/15/2017.
 */

public class AdapterCounselSpinner extends BaseAdapter {

    private ArrayList<UserListPOJO.UserDetail> mList;
    private Context mContext;

    public AdapterCounselSpinner(Context mContext, ArrayList<UserListPOJO.UserDetail> mList, String title) {
        this.mList = mList;
        this.mContext = mContext;
        mList.add(0, new UserListPOJO.UserDetail(title,""));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_offense_spinner, parent, false);

        TextView text = (TextView) view.findViewById(R.id.text);

        text.setText(mList.get(position).getCompleteName());

        return view;
    }
}
