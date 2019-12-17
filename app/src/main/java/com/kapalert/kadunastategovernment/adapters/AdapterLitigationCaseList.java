package com.kapalert.kadunastategovernment.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseDetailActivity;
import com.kapalert.kadunastategovernment.activities.StageTwoStepOneActivity;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.NoteJSON;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by win10 on 8/11/2017.
 */

public class AdapterLitigationCaseList extends BaseCaseListAdapter {


    public AdapterLitigationCaseList(Context mContext, ArrayList<CaseListPOJO.CaseList> mList, Fragment fragment) {
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
        setupButtons(data, holder.accept, holder.send, holder.close, holder.stageStatus,holder.btn_uploadNote);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void setupButtons(final CaseListPOJO.CaseList data, Button accept, Button send, Button close, TextView status,Button btn_uploadNote) {
        View.OnClickListener sendButtonListener = null;
        View.OnClickListener acceptButtonListener = null;

        int stage = data.stage;
        int step = data.step;

        Utils.showToast(mContext,btn_uploadNote.getText().toString());

        UserInfoJson.UserData userData = Constants.getUser(mContext);
        String userID = userData.id;

        int statusStringID = R.string.blank, buttonStringID = R.string.blank;

        if (stage == 1) {
            switch (step) {
                case 0:
                    send.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);

                    buttonStringID = R.string.send_dpp;
                    statusStringID = R.string.case_created;
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendCase(data.id, Constants.API_EXCHANGE_TYPE_ROLE, Constants.API_FORWARD_TYPE_SEND, Constants.USER_ROLE_DPP, 1, 1);
                        }
                    };
                    break;
                case 1:
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_dpp;
                    break;
                case 2:
                    hideAllButtons(send, close, accept);
                    buttonStringID = R.string.send_counsel;
                    statusStringID = R.string.case_accepted_dpp;
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
                    if (data.litigationID.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.VISIBLE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    acceptButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptCase(data.id, 1, 6);
                        }
                    };
                    statusStringID = R.string.case_sent_litigation;
                    break;
                case 6:
                    if (data.litigationID.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendCase(data.id, Constants.API_EXCHANGE_TYPE_ROLE, Constants.API_FORWARD_TYPE_SEND, Constants.USER_ROLE_DPP, 1, 7);
                        }
                    };
                    buttonStringID = R.string.send_dpp;
                    statusStringID = R.string.case_accepted_litigation;
                    break;
                case 7:
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_dpp;
                    break;
                case 8:
                    hideAllButtons(send, close, accept);
                    buttonStringID = R.string.send_litigation;
                    statusStringID = R.string.case_accepted_dpp;
                    break;
                case 9:
                    if (data.litigationID.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.VISIBLE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    statusStringID = R.string.case_sent_litigation;
                    acceptButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptCase(data.id, 2, 10);
                        }
                    };

                    break;
            }
            close.setVisibility(View.GONE);

        } else if (stage == 2) {
            if (step == 10) {

                if (data.litigationID.equalsIgnoreCase(userID)) {
                    send.setVisibility(View.VISIBLE);
                    accept.setVisibility(View.GONE);


                } else {
                    send.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);


                }
                buttonStringID = R.string.send_counsel;
                statusStringID = R.string.case_accepted_litigation;
                sendButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TinyDB(mContext).putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                        Utils.intentToActivity(mContext, StageTwoStepOneActivity.class);
                    }
                };





            } else if (step == 11) {
                hideAllButtons(send, close, accept);
                statusStringID = R.string.case_sent_counsel;
            }
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
