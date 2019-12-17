package com.kapalert.kadunastategovernment.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseCategoryActivity;
import com.kapalert.kadunastategovernment.adapters.AdapterCounselCaseList;
import com.kapalert.kadunastategovernment.adapters.AdapterDPPCaseList;
import com.kapalert.kadunastategovernment.adapters.AdapterLitigationCaseList;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 *
 */
public class CaseListFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    public static CaseListFragment instance;
    Spinner genderspinner;
    String selState="Civil";
    private String[] state = {"Civil", "Criminal", "Hearing Notice", "Motions", "Appeals"};
    private Button mCreateCase;
    private RecyclerView mCaseList;
    private TextView mEmptyList;
    public  String Tabselect="";

    public CaseListFragment(Bundle bundle) {
        super();
        if (bundle!=null)
        Tabselect=bundle.getString("seltype");
    }

    public static CaseListFragment getInstance() {

        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.case_management);
         instance = this;
        View view = inflater.inflate(R.layout.fragment_case_list, container, false);

        mEmptyList = view.findViewById(R.id.empty_list);
        mCreateCase = view.findViewById(R.id.create_case);
        mCaseList = view.findViewById(R.id.case_list);
        genderspinner = view.findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mCaseList.setLayoutManager(manager);
        if (Constants.getUser(mContext).userRole.equalsIgnoreCase(Constants.USER_ROLE_LITIGATION_CRIMINAL)) {
            mCreateCase.setVisibility(View.VISIBLE);
        } else {
            mCreateCase.setVisibility(View.GONE);
        }
        mCreateCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.intentToActivity(mContext, CaseCategoryActivity.class);
            }
        });


        return view;
    }
public static void setupclass(String Total){
    //    CaseListFragment caseListFragment=new CaseListFragment();
   //     caseListFragment.setupList(true);
    //setupgroupList(true);
    Log.d("1212",Total);
}
            @Override
    public void onResume() {
        super.onResume();
                setupList(true );

    }

    public void setupList(boolean showProgress ) {

        new Gson().fromJson("", new TypeToken<ArrayList<String>>() {
        }.getType());


        new ServerRequest<CaseListPOJO>(mContext, Constants.getCaseListUrl(mContext,selState,Tabselect), showProgress) {
            @Override
            public void onCompletion(Call<CaseListPOJO> call, Response<CaseListPOJO> response) {
                if (response.body().status) {
                    RecyclerView.Adapter adapterCaseList = null;
                    if (response.body().caseList != null) {
                        ArrayList<CaseListPOJO.CaseList> list = new ArrayList<>(response.body().caseList);
                        UserInfoJson.UserData user = Constants.getUser(mContext);
                        if (user.userRole.equalsIgnoreCase(Constants.USER_ROLE_COUNSEL)) {
                            adapterCaseList = new AdapterCounselCaseList(mContext, list, CaseListFragment.this);
                        } else if (user.userRole.equalsIgnoreCase(Constants.USER_ROLE_DPP)) {
                            adapterCaseList = new AdapterDPPCaseList(mContext, list, CaseListFragment.this);
                        } else {
                            adapterCaseList = new AdapterLitigationCaseList(mContext, list, CaseListFragment.this);
                        }
                        mCaseList.setAdapter(adapterCaseList);
                    }

                    if (adapterCaseList == null || adapterCaseList.getItemCount() == 0) {
                        mEmptyList.setVisibility(View.VISIBLE);
                        mCaseList.setVisibility(View.GONE);
                    } else {
                        mEmptyList.setVisibility(View.GONE);
                        mCaseList.setVisibility(View.VISIBLE);
                    }
                } else {
                    mEmptyList.setVisibility(View.VISIBLE);
                    mCaseList.setVisibility(View.GONE);
                }
            }
        };

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderspinner.setSelection(position);
        selState = (String) genderspinner.getSelectedItem();
        setupList(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
