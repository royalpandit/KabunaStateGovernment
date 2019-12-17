package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.HomeActivity;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by win10 on 8/24/2017.
 */

public class BaseCaseListAdapter extends RecyclerView.Adapter<AdapterLitigationCaseList.CaseHolder> {
    public Context mContext;
    public ArrayList<CaseListPOJO.CaseList> mList;
    public Fragment fragment;

    @Override
    public AdapterLitigationCaseList.CaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterLitigationCaseList.CaseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CaseHolder extends RecyclerView.ViewHolder {

        TextView caseId, caseName, stageNum, stepNum, stageStatus;
        Button accept, send, close,senddata,uploadandforward, btn_uploadNote;
        CardView bg;

        public CaseHolder(View itemView) {
            super(itemView);

            caseId = (TextView) itemView.findViewById(R.id.case_id);
            accept = (Button) itemView.findViewById(R.id.case_accept);
            close = (Button) itemView.findViewById(R.id.case_close);
            send = (Button) itemView.findViewById(R.id.case_send);
            senddata=(Button) itemView.findViewById(R.id.senddata);
            uploadandforward=(Button) itemView.findViewById(R.id.uploadandforward);
            caseName = (TextView) itemView.findViewById(R.id.case_name);
            stageNum = (TextView) itemView.findViewById(R.id.case_stage);
            stageStatus = (TextView) itemView.findViewById(R.id.case_status);
            stepNum = (TextView) itemView.findViewById(R.id.case_step);
            bg = (CardView) itemView.findViewById(R.id.bg);
            btn_uploadNote =  itemView.findViewById(R.id.btn_uploadNote);




        }


    }

    public void sendCase(String id, String apiExchangeTypeRole, String apiForwardTypeSend, String userRoleDpp, int stageNum, int stepNum) {

        new ServerRequest<UserInfoJson>(mContext, Constants.getCaseProgressUrl(mContext, id, apiExchangeTypeRole, apiForwardTypeSend, userRoleDpp, "" + stageNum, "" + stepNum), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showToast(mContext, response.body().message);
                    fragment.onResume();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    public void acceptCase(String id, int stageNum, int stepNum) {

        new ServerRequest<UserInfoJson>(mContext, Constants.getCaseProgressUrl(mContext, id, "", Constants.API_FORWARD_TYPE_ACCEPT, "", "" + stageNum, "" + stepNum), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showToast(mContext, response.body().message);
                    fragment.onResume();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

}
