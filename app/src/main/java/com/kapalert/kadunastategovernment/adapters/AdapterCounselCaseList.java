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
import com.kapalert.kadunastategovernment.activities.StageOneStepFourActivity;
import com.kapalert.kadunastategovernment.activities.StageOneStepTwoActivity;
import com.kapalert.kadunastategovernment.activities.StageThreeStepOneActivity;
import com.kapalert.kadunastategovernment.activities.StageThreeStepThreeActivity;
import com.kapalert.kadunastategovernment.activities.StageThreeStepTwoActivity;
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

public class AdapterCounselCaseList extends BaseCaseListAdapter {

    public AdapterCounselCaseList(Context mContext, ArrayList<CaseListPOJO.CaseList> mList, Fragment fragment) {
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


    private void setupButtons(final CaseListPOJO.CaseList data, Button accept, Button send, final Button close, TextView status,Button btn_uploadNote) {
        View.OnClickListener sendButtonListener = null;
        View.OnClickListener acceptButtonListener = null;
        View.OnClickListener closeButtonListener = null;
        final TinyDB db = new TinyDB(mContext);

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
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.intentToActivity(mContext, StageOneStepTwoActivity.class);
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
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.intentToActivity(mContext, StageOneStepTwoActivity.class);
                        }
                    };
                    break;
                case 3:
                    if (data.ownerID.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.VISIBLE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    acceptButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptCase(data.id, 1, 4);
                        }
                    };
                    statusStringID = R.string.case_sent_counsel;
                    break;
                case 4:

                    if (data.acceptedBy.equalsIgnoreCase(userID)) {
                        send.setVisibility(View.VISIBLE);
                        btn_uploadNote.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.GONE);
                    } else {
                        send.setVisibility(View.GONE);
                        accept.setVisibility(View.GONE);
                    }
                    sendButtonListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TinyDB(mContext).putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                            Utils.intentToActivity(mContext, StageOneStepFourActivity.class);
                        }
                    };
                    buttonStringID = R.string.send_litigation;
                    statusStringID = R.string.case_accepted_counsel;



                   final String caseID = data.id;
                    btn_uploadNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewDailog(caseID);

                           //// Utils.showToast(mContext,"Yes");
                        }
                    });

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
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_dpp;
                    break;
                case 8:
                    buttonStringID = R.string.send_litigation;
                    statusStringID = R.string.case_accepted_dpp;

                    break;
                case 9:
                    hideAllButtons(send, close, accept);
                    statusStringID = R.string.case_sent_litigation;
                    break;
            }
            close.setVisibility(View.GONE);
        } else if (stage == 2) {
            if (step == 10) {
                hideAllButtons(send, close, accept);
                buttonStringID = R.string.send_counsel;
                statusStringID = R.string.case_accepted_litigation;
            } else if (step == 11) {
                if (data.ownerID.equalsIgnoreCase(userID)) {
                    send.setVisibility(View.GONE);
                    accept.setVisibility(View.VISIBLE);
                } else {
                    send.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);
                }
                statusStringID = R.string.case_sent_counsel;
                acceptButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptCase(data.id, 3, 12);
                    }
                };
            }
            close.setVisibility(View.GONE);
        } else if (stage == 3) {
            if (step == 12) {
                if (data.acceptedBy.equalsIgnoreCase(userID)) {
                    close.setVisibility(View.VISIBLE);
                }else{
                    close.setVisibility(View.GONE);
                }
                statusStringID = R.string.case_accepted_counsel;
                closeButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                        Utils.intentTo(mContext, StageThreeStepOneActivity.class);
                    }
                };
            } else if (step == 13) {
                if (data.acceptedBy.equalsIgnoreCase(userID)) {
                    close.setVisibility(View.VISIBLE);
                }else{
                    close.setVisibility(View.GONE);
                }
                statusStringID = R.string.case_form_1_submitted;
                closeButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                        Utils.intentTo(mContext, StageThreeStepTwoActivity.class);
                    }
                };
            } else if (step == 14) {
                if (data.acceptedBy.equalsIgnoreCase(userID)) {
                    close.setVisibility(View.VISIBLE);
                }else{
                    close.setVisibility(View.GONE);
                }
                statusStringID = R.string.case_form_2_submitted;
                closeButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.putString(Constants.DB_SELECTED_CASE, new Gson().toJson(data));
                        Utils.intentTo(mContext, StageThreeStepThreeActivity.class);
                    }
                };
            } else {
                statusStringID = R.string.case_closed;
                close.setVisibility(View.GONE);
            }
            send.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
        } else {
            send.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
        }
        status.setText(String.format(mContext.getString(R.string.case_status_pattern), mContext.getString(statusStringID)));
        send.setText(mContext.getString(buttonStringID));
        send.setOnClickListener(sendButtonListener);
        accept.setOnClickListener(acceptButtonListener);
        close.setOnClickListener(closeButtonListener);
    }

    private void hideAllButtons(Button send, Button close, Button accept) {
        send.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
    }



    public void viewDailog(final String caseID) {

        final EditText edt_note;
        Button btn_submit,btn_cancel;
        final String strNote ;
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.dialog_send_note);

        edt_note = (EditText) dialog.findViewById(R.id.edt_note);
        btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);




        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.cancel_note))
                        .setMessage(mContext.getString(R.string.are_you_sure_want_cancel_entry))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                dialog.dismiss();

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ////requestSendNote(mContext,caseID,strNote);

                new ServerRequest<NoteJSON>(mContext, Constants. requestSendNote(mContext,caseID,edt_note.getText().toString()), true) {
                    @Override
                    public void onCompletion(Call<NoteJSON> call, Response<NoteJSON> response) {
                        NoteJSON responseJSON = response.body();
                        System.out.println("======responseJSON======send======="+response);
                        if (responseJSON.status) {
                            Utils.showToast(mContext,mContext.getString(R.string.successfully_uploaded));
                            dialog.dismiss();
                        } else {
                            Utils.showToast(mContext, responseJSON.errorMessage);
                        }
                    }
                };

            }
        });



        dialog.show();
    }
}
