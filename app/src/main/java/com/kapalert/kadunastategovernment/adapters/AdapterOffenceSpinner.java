package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.OffenceModal;

import java.util.ArrayList;

/**
 * Created by win10 on 8/15/2017.
 */

public class AdapterOffenceSpinner extends BaseAdapter {

    private ArrayList<OffenceModal> mList;
    private Context mContext;

    public AdapterOffenceSpinner(Context mContext, ArrayList<OffenceModal> mList, String title) {
        this.mList = mList;
        this.mContext = mContext;
        mList.add(0, new OffenceModal(title, ""));
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

        text.setText(mList.get(position).getName());

        return view;
    }
}
