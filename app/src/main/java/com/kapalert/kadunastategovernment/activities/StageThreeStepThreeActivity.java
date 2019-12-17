package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterAdjournmentList;
import com.kapalert.kadunastategovernment.adapters.AdapterJudgementAccusedList;
import com.kapalert.kadunastategovernment.adapters.AdapterStringSpinner;
import com.kapalert.kadunastategovernment.templates.AdjournmentDataJSON;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CommonListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselAccusedListPOJO;
import com.kapalert.kadunastategovernment.templates.SelectJudgementJSON;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StageThreeStepThreeActivity extends AppBaseActivity {

    private TextView mDateProCommences, mDateFirstMention, mDateProCompletes, mDateDefCommences, mDateDefCompleted, mDateAddressComp, mDateJudgement, mDateSentence, mDateFinalReport, mDateClose;
    private EditText mJudgementNarrative;
    private RecyclerView mAccusedResult, mAdjournmentList;
    private Button mSubmit, mAddAdjournmentData;
    private ArrayList<CounselAccusedListPOJO.Accused> mAccusedList = new ArrayList<>();
    private ArrayList<AdjournmentDataJSON.AdjournmentData> mAdjournmentArr = new ArrayList<>();
    private AdapterAdjournmentList mAdjournmentAdapter;
    private String mAdjournmentReasonID = "";
    private CaseListPOJO.CaseList caseObj;
    private CommonListPOJO mAllList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_three_step_three);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);
        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);

        if (caseObj == null) {
            finish();
            return;
        }
        initViews();
    }

    private void initViews() {
        mSubmit = (Button) findViewById(R.id.submit);
        mAddAdjournmentData = (Button) findViewById(R.id.add_adjournment_data);
        mDateProCommences = (TextView) findViewById(R.id.date_prosecution_commences);
        mDateFirstMention = (TextView) findViewById(R.id.date_first_mention);
        mDateProCompletes = (TextView) findViewById(R.id.date_prosecution_completed);
        mDateDefCommences = (TextView) findViewById(R.id.date_def_commences);
        mDateDefCompleted = (TextView) findViewById(R.id.date_def_completed);
        mDateAddressComp = (TextView) findViewById(R.id.date_address_complete);
        mDateJudgement = (TextView) findViewById(R.id.date_of_judgement);
        mDateSentence = (TextView) findViewById(R.id.date_sentencing);
        mDateFinalReport = (TextView) findViewById(R.id.date_final_case_report_dpp);
        mDateClose = (TextView) findViewById(R.id.date_case_closed);
        mJudgementNarrative = (EditText) findViewById(R.id.judgement_narrative);

        mAccusedResult = (RecyclerView) findViewById(R.id.judgement_accused_list);
        mAdjournmentList = (RecyclerView) findViewById(R.id.adjournment_list);

        setupDatePicker();
        fetchAllList();
        mAddAdjournmentData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAdjournmentDataDialog();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAPI();
            }
        });
    }

    private void setupAdjournmentList() {
        mAdjournmentList.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAdjournmentList.setLayoutManager(manager);

        mAdjournmentAdapter = new AdapterAdjournmentList(mContext, mAdjournmentArr, mAllList.allList.adjournmentReasonList);
        mAdjournmentList.setAdapter(mAdjournmentAdapter);
    }

    private void showAddAdjournmentDataDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_adjourment_data);

        final TextView hearingDate = (TextView) dialog.findViewById(R.id.date_hearing);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button add = (Button) dialog.findViewById(R.id.add);
        final TextView newHearingDate = (TextView) dialog.findViewById(R.id.date_new_hearing);
        final EditText progressMade = (EditText) dialog.findViewById(R.id.progress_made);
        Spinner adjournmentReason = (Spinner) dialog.findViewById(R.id.adjournment_reason);

        assignDatePicker(hearingDate);
        assignDatePicker(newHearingDate);

        if (!mAdjournmentArr.isEmpty()) {
            String newHearingDateStr = mAdjournmentArr.get(mAdjournmentArr.size() - 1).getNewHearingDate();
            hearingDate.setText(newHearingDateStr);
            hearingDate.setEnabled(false);
        }
        // Adjournment Reason Spinner
        ArrayList<String> adjournmentList = new ArrayList<>();

        for (CommonListPOJO.CommonList court : mAllList.allList.adjournmentReasonList) {
            adjournmentList.add(court.name);
        }

        AdapterStringSpinner adapterAdjournmentReason = new AdapterStringSpinner(mContext, adjournmentList, getString(R.string.select_adjournment_reason));
        adjournmentReason.setAdapter(adapterAdjournmentReason);
        adjournmentReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mAdjournmentReasonID = mAllList.allList.adjournmentReasonList.get(position - 1).id;
                } else {
                    mAdjournmentReasonID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String progressMadeStr = progressMade.getText().toString();
                String hearingDateStr = hearingDate.getText().toString();
                String newHearingDateStr = newHearingDate.getText().toString();

                if (progressMadeStr.isEmpty()) {
                    Utils.showToast(mContext, getString(R.string.reasonable_progress_made_error));
                } else if (hearingDateStr.isEmpty() || newHearingDateStr.isEmpty()) {
                    Utils.showToast(mContext, getString(R.string.select_all_dates_error));
                } else if (mAdjournmentReasonID.isEmpty()) {
                    Utils.showToast(mContext, getString(R.string.select_adjournment_reason_error));
                } else if (!beforeDate(hearingDateStr, newHearingDateStr)) {
                    Utils.showToast(mContext, getString(R.string.select_hearing_date_before_new_hearing_error));
                } else {
                    mAdjournmentArr.add(new AdjournmentDataJSON.AdjournmentData(hearingDateStr, mAdjournmentReasonID, newHearingDateStr, progressMadeStr));
                    mAdjournmentAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean beforeDate(String hearingDateStr, String newHearingDateStr) {
        try {
            Date hearingDate = new SimpleDateFormat("yyyy-MM-dd").parse(hearingDateStr);
            Date newHearingDate = new SimpleDateFormat("yyyy-MM-dd").parse(newHearingDateStr);

            return hearingDate.before(newHearingDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void submitAPI() {
        String accusedJson = generateJudgementJson();
        String adjournmentJson = generateAdjournmentJson();
        if (allFieldsValid() && accusedJson != null && adjournmentJson != null) {
            new ServerRequest<UserInfoJson>(mContext, Constants.getForm3SubmitUrl(mContext, caseObj.id, adjournmentJson,
                    mDateProCommences.getText().toString(),
                    mDateProCompletes.getText().toString(),
                    mDateDefCommences.getText().toString(),
                    mDateDefCompleted.getText().toString(),
                    mDateAddressComp.getText().toString(),
                    mDateJudgement.getText().toString(),
                    mDateSentence.getText().toString(),
                    mDateFinalReport.getText().toString(),
                    mDateClose.getText().toString(),
                    accusedJson,
                    mDateFirstMention.getText().toString(),
                    mJudgementNarrative.getText().toString()
            ), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.showToast(mContext, response.body().message);
                        finish();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        } else if (accusedJson == null) {
            Utils.showToast(mContext, getString(R.string.select_judgement_each_accused_error));
        } else if (adjournmentJson == null) {
            Utils.showToast(mContext, getString(R.string.add_adjourment_data_error));
        }
    }

    private boolean allFieldsValid() {

        if (!allDatesFilled(mDateProCommences, mDateFirstMention, mDateProCompletes, mDateDefCommences, mDateDefCompleted, mDateAddressComp, mDateJudgement, mDateSentence, mDateFinalReport, mDateClose)) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }

        if (mJudgementNarrative.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.all_fields_required));
            return false;
        }

        return true;
    }

    private boolean allDatesFilled(TextView... dateViews) {

        for (TextView textView : dateViews) {
            if (textView.getText().toString().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void fetchAllList() {
        new ServerRequest<CommonListPOJO>(mContext, Constants.getAllListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<CommonListPOJO> call, Response<CommonListPOJO> response) {
                if (response.body().status) {
                    if (response.body().allList != null) {

                        mAllList = response.body();
                        setupSpinners(mAllList.allList);
                        setupAdjournmentList();
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                    finish();
                }
            }
        };
    }

    private void setupSpinners(final CommonListPOJO.AllList allList) {
        setupAccusedList(allList.judgementList);
    }

    private void setupAccusedList(final List<CommonListPOJO.CommonList> judgementList) {
        mAccusedResult.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAccusedResult.setLayoutManager(manager);

        new ServerRequest<CounselAccusedListPOJO>(mContext, Constants.getCounselAccusedListUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<CounselAccusedListPOJO> call, Response<CounselAccusedListPOJO> response) {

                if (response.body().status) {
                    if (response.body().accuseds != null && !response.body().accuseds.isEmpty()) {
                        ArrayList<CounselAccusedListPOJO.Accused> accusedArrayList = new ArrayList<>(response.body().accuseds);
                        mAccusedList = new ArrayList<>();

                        for (CounselAccusedListPOJO.Accused accused : accusedArrayList) {
                            if (accused.stepNo.equalsIgnoreCase("5"))
                                mAccusedList.add(accused);
                        }
                        AdapterJudgementAccusedList adapterJudgementAccusedList = new AdapterJudgementAccusedList(mContext, mAccusedList, judgementList);
                        mAccusedResult.setAdapter(adapterJudgementAccusedList);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                        finish();
                        return;
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                    finish();
                    return;
                }
            }
        };
    }

    private void setupDatePicker() {
        assignDatePicker(mDateAddressComp);
        assignDatePicker(mDateFirstMention);
        assignDatePicker(mDateClose);
        assignDatePicker(mDateDefCommences);
        assignDatePicker(mDateDefCompleted);
        assignDatePicker(mDateFinalReport);
        assignDatePicker(mDateJudgement);
        assignDatePicker(mDateProCommences);
        assignDatePicker(mDateProCompletes);
        assignDatePicker(mDateSentence);
    }

    private void assignDatePicker(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate.set(Calendar.HOUR_OF_DAY, 6);
                        selectedDate.set(Calendar.MINUTE, 6);
                        selectedDate.set(Calendar.SECOND, 6);
                        selectedDate.set(Calendar.MILLISECOND, 6);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        textView.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private String generateJudgementJson() {
        ArrayList<SelectJudgementJSON.AccusedJudgementJson> list = new ArrayList<>();

        for (CounselAccusedListPOJO.Accused accused : mAccusedList) {
            if (accused.selectedJudgementID.isEmpty()) {
                return null;
            } else {
                list.add(new SelectJudgementJSON.AccusedJudgementJson(accused.id, accused.selectedJudgementID));
            }
        }
        SelectJudgementJSON selectJudgementJSON = new SelectJudgementJSON(list);

        return new Gson().toJson(selectJudgementJSON);
    }

    private String generateAdjournmentJson() {
        if (mAdjournmentArr.isEmpty()) {
            return null;
        }
        AdjournmentDataJSON selectAdjournmentJSON = new AdjournmentDataJSON(mAdjournmentArr);

        return new Gson().toJson(selectAdjournmentJSON);
    }

}
