package com.kapalert.kadunastategovernment.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterAnnouncementList;
import com.kapalert.kadunastategovernment.templates.AnnouncementListPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AnnouncementFragment extends BaseFragment {

    private RecyclerView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.announcements);
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        initViews(view);

        return view;

    }

    private void initViews(View view) {
        mList = (RecyclerView) view.findViewById(R.id.announcement_list);

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);
        setupList();
    }

    private void setupList() {

        new ServerRequest<AnnouncementListPOJO>(mContext, Constants.getAnnouncementListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<AnnouncementListPOJO> call, Response<AnnouncementListPOJO> response) {

                if (response.body().status) {
                    if (response.body().announcementList != null && !response.body().announcementList.isEmpty()) {
                        ArrayList<AnnouncementListPOJO.Announcement> list = new ArrayList<>(response.body().announcementList);
                        AdapterAnnouncementList adapterAnnouncementList = new AdapterAnnouncementList(mContext, list);
                        mList.setAdapter(adapterAnnouncementList);
                    } else {
                        Utils.showToast(mContext, getString(R.string.announcement_list_empty));
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };


    }

}
