package com.kapalert.kadunastategovernment.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterCounselSpinner;
import com.kapalert.kadunastategovernment.adapters.AdapterOffenceSpinner;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StageOneStepTwoActivity extends AppBaseActivity {

    private Spinner mSelectCounsel;
    private Button mSend;
    private String mSelectedCounselID = "";
    private ArrayList<UserListPOJO.UserDetail> mCounselList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_one_step_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        initViews();
    }

    private void initViews() {
        mSelectCounsel = (Spinner) findViewById(R.id.select_counsel);
        mSend = (Button) findViewById(R.id.send);

        fetchCounselList();
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendClick();
            }
        });
    }

    private void fetchCounselList() {
        new ServerRequest<UserListPOJO>(mContext, Constants.getCounselListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<UserListPOJO> call, Response<UserListPOJO> response) {

                if (response.body().status) {
                    if (response.body().usersList != null && !response.body().usersList.isEmpty()) {
                        List<UserListPOJO.UserDetail> list = new ArrayList<>(response.body().usersList);
                        list = response.body().usersList;
                        System.out.println("=====list=====sf=="+list.size());
//                        for (int i = 0; i < list.size(); i++) {
//                            if (list.get(i).ID.equalsIgnoreCase(Constants.getUser(mContext).id)) {
//                                list.remove(i);
//                                break;
//                            }
//                        }
                        mCounselList = new ArrayList<>(list);
                    } else {
                        mCounselList = new ArrayList<>();
                    }
                    setupSpinner();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void sendClick() {
        if (!mSelectedCounselID.isEmpty()) {
            CaseListPOJO.CaseList caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
            if (caseObj == null) {
                onBackPressed();
                return;
            }
            new ServerRequest<UserInfoJson>(mContext, Constants.getCaseProgressUrl(mContext, caseObj.id, Constants.API_EXCHANGE_TYPE_USER, Constants.API_FORWARD_TYPE_SEND, mSelectedCounselID, "" + ((caseObj.stage == 2) ? 2 : 1), "" + ((caseObj.stage == 2) ? 11 : 3)), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        } else {
            Utils.showToast(mContext, getString(R.string.please_select_counsel));
        }
    }

    private void setupSpinner() {

        AdapterCounselSpinner adapterOffenceInvestigationSpinner = new AdapterCounselSpinner(mContext, mCounselList, getString(R.string.select_counsel));
        mSelectCounsel.setAdapter(adapterOffenceInvestigationSpinner);
        mSelectCounsel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSelectedCounselID = mCounselList.get(position).ID;
                } else {
                    mSelectedCounselID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
