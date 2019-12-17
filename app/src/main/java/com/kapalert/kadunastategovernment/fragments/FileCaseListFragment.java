package com.kapalert.kadunastategovernment.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterFileCaseList;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**

 */
public class FileCaseListFragment extends BaseFragment {

    private RecyclerView mList;
    private TextView mEmptyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.file_management);
        View view = inflater.inflate(R.layout.fragment_file_case_list, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mEmptyList = (TextView) view.findViewById(R.id.empty_list);
        mList = (RecyclerView) view.findViewById(R.id.file_case_list);

        setupList();
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);
        String casetype = "";
        String Tabselect="";

        new ServerRequest<CaseListPOJO>(mContext, Constants.getCaseListUrl(mContext,casetype,Tabselect), true) {
            @Override
            public void onCompletion(Call<CaseListPOJO> call, Response<CaseListPOJO> response) {
                if (response.body().status) {
                    AdapterFileCaseList adapterFileCaseList = null;
                    if (response.body().caseList != null) {
                        ArrayList<CaseListPOJO.CaseList> list = new ArrayList<>(response.body().caseList);
                        adapterFileCaseList = new AdapterFileCaseList(mContext, list);
                        mList.setAdapter(adapterFileCaseList);
                    }

                    if (adapterFileCaseList == null || adapterFileCaseList.getItemCount() == 0) {
                        mEmptyList.setVisibility(View.VISIBLE);
                        mList.setVisibility(View.GONE);
                    } else {
                        mEmptyList.setVisibility(View.GONE);
                        mList.setVisibility(View.VISIBLE);
                    }
                } else {
                    mEmptyList.setVisibility(View.VISIBLE);
                    mList.setVisibility(View.GONE);
                }
            }
        };

    }

}
