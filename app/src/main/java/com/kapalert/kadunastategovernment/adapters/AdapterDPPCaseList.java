package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseDetailActivity;
import com.kapalert.kadunastategovernment.activities.StageNineActivity;
import com.kapalert.kadunastategovernment.activities.StageNineForward;
import com.kapalert.kadunastategovernment.activities.StageOneStepEightActivity;
import com.kapalert.kadunastategovernment.activities.StageOneStepTwoActivity;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterDPPCaseList extends BaseCaseListAdapter {

    public AdapterDPPCaseList(Context mContext, ArrayList<CaseListPOJO.CaseList> mList, Fragment fragment) {
        this.mContext = mContext;
        this.mList = mList;
        this.fragment = fragment;
    }



    @Override
    public CaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_case, parent, false);

        return new CaseHolder(view);
    }

    @Override
    public void onBindViewHolder(CaseHolder holder, int position) {

        final CaseListPOJO.CaseList data = mList.get(position);

        holder.caseId.setText(String.format(mContext.getString(R.string.case_id_pattern), data.id));
        holder.stageNum.setText(String.format(mContext.getString(R.string.case_stage_pattern), "" + data.stage));
        holder.stepNum.setText(String.format(mContext.getString(R.string.case_step_pattern), "" + data.step));
        holder.caseName.setText(String.format(mContext.getString(R.string.case_name_pattern), data.file_name));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TinyDB(mContext).putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                Utils.intentToActivity(mContext, CaseDetailActivity.class);
            }
        });


        if (data.ownerID.equalsIgnoreCase(Constants.getUser(mContext).id)) {
            holder.bg.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
        } else {
            if (data.acceptedBy.equalsIgnoreCase(Constants.getUser(mContext).id)) {
                holder.bg.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
            } else {
                holder.bg.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }
        }
        setupButtons(data, holder.accept, holder.send, holder.close, holder.senddata,holder.uploadandforward, holder.stageStatus,holder.btn_uploadNote);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void setupButtons(final CaseListPOJO.CaseList data, Button accept, Button send, Button close,Button senddata,Button uploadandforward, TextView status,Button btn_uploadNote) {
        View.OnClickListener sendButtonListener = null;
        View.OnClickListener acceptButtonListener = null;

        int stage = data.stage;
        int step = data.step;

        UserInfoJson.UserData userData = Constants.getUser(mContext);
        String userType = userData.userRole;
        String userID = userData.id;

        int statusStringID = R.string.blank, buttonStringID = R.string.blank;

        if (stage == 1) {
            switch (step) {
                case 0:
                    hideAllButtons(send, close, accept);
                    buttonStringID = R.string.send_dpp;
                    statusStringID = R.string.case_created;
                    break;
                case 1:
                    send.setVisibility(View.GONE);
                    accept.setVisibility(View.VISIBLE);
                    statusStringID = R.string.case_sent_dpp;
                    acceptButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptCase(data.id, 1, 2);
                        }
                    };
                    break;
                case 2:
                    if (data.acceptedBy.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    buttonStringID = R.string.send_counsel;
                    statusStringID = R.string.case_accepted_dpp;
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TinyDB(mContext).putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                            Utils.intentToActivity(mContext, StageOneStepTwoActivity.class);
                        }
                    };
                    break;
                case 3:
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_counsel;
                    break;
                case 4:
                    hideAllButtons(send, close, accept);
                    buttonStringID = R.string.send_litigation;
                    statusStringID = R.string.case_accepted_counsel;
                    break;
                case 5:
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_litigation;
                    break;
                case 6:
                    hideAllButtons(send, close, accept);
                    buttonStringID = R.string.send_dpp;
                    statusStringID = R.string.case_accepted_litigation;
                    break;
                case 7:
                    send.setVisibility(View.GONE);
                    accept.setVisibility(View.VISIBLE);
                    statusStringID = R.string.case_sent_dpp;
                    acceptButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptCase(data.id, 1, 8);
                        }
                    };
                    break;
                case 8:
                    if (data.acceptedBy.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    buttonStringID = R.string.send_Recomendation;
                    statusStringID = R.string.case_accepted_dpp;
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TinyDB(mContext).putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                            Utils.intentToActivity(mContext, StageOneStepEightActivity.class);
                        }
                    };
                    break;
                case 9:
                    if(data.isLitigationDateDone.equals("true")){
                        senddata.setVisibility(View.INVISIBLE);
                        uploadandforward.setVisibility(View.VISIBLE);
                        uploadandforward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent it=new Intent(mContext, StageNineForward.class);
                                it.putExtra("caseid", data.id);
                                mContext.startActivity(it);
                            }
                        });
                    }else{
                        senddata.setVisibility(View.VISIBLE);
                        senddata.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent it=new Intent(mContext, StageNineActivity.class);
                                it.putExtra("caseid", data.id);
                                mContext.startActivity(it);
                            }
                        });
                    }

                    hideAllButtons(send, close, accept);

                    statusStringID = R.string.case_sent_litigation;
                    break;
            }
            close.setVisibility(View.GONE);
        } else if (stage == 2) {
            uploadandforward.setVisibility(View.INVISIBLE);
            if (step == 10) {
                statusStringID = R.string.case_accepted_litigation;
            } else if (step == 11) {
                statusStringID = R.string.case_sent_counsel;
            }
            hideAllButtons(send, close, accept);
            close.setVisibility(View.GONE);
        } else if (stage == 3) {
            hideAllButtons(send, close, accept);
            if (step == 12) {
                statusStringID = R.string.case_accepted_counsel;
            } else if (step == 13) {
                statusStringID = R.string.case_form_1_submitted;
            } else if (step == 14) {
                statusStringID = R.string.case_form_2_submitted;
            } else {
                statusStringID = R.string.case_closed;
            }
        } else {
            send.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
        }
        status.setText(String.format(mContext.getString(R.string.case_status_pattern), mContext.getString(statusStringID)));
        send.setText(mContext.getString(buttonStringID));
        send.setOnClickListener(sendButtonListener);
        accept.setOnClickListener(acceptButtonListener);

    }

    private void hideAllButtons(Button send, Button close, Button accept) {
        send.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
    }
}
