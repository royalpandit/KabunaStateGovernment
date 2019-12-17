package com.kapalert.kadunastategovernment.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.EditEvent;
import com.kapalert.kadunastategovernment.adapters.AdapterEventsList;
import com.kapalert.kadunastategovernment.templates.EventListPOJO;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**

 */
public class CalendarFragment extends BaseFragment {

    private RecyclerView mList;
    private Button mAddEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle(R.string.events);

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mList = (RecyclerView) view.findViewById(R.id.event_list);
        mAddEvent = (Button) view.findViewById(R.id.add_event);

        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB.remove(Constants.DB_SELECTED_EVENT);
                Utils.intentToActivity(mContext, EditEvent.class);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        setupList();
    }

    private void setupList() {

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);

        new ServerRequest<EventListPOJO>(mContext, Constants.getEventListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<EventListPOJO> call, Response<EventListPOJO> response) {

                if (response.body().status) {
                    if (response.body().eventList != null && !response.body().eventList.isEmpty()) {
                        ArrayList<EventListPOJO.EventData> list = new ArrayList<>(response.body().eventList);

                        AdapterEventsList adapterEventsList = new AdapterEventsList(mContext, list);
                        mList.setAdapter(adapterEventsList);
                    } else {
                        Utils.showToast(mContext, getString(R.string.no_data_found));
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };

    }

}
