package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterStringSpinner;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CourtListPOJO;
import com.kapalert.kadunastategovernment.templates.JudgeListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StageThreeStepTwoActivity extends AppBaseActivity {

    private TextView mDateHearingReceived, mDateFirstHearing;
    private EditText mRefNum;
    private Spinner mCourtName, mNameJudge;
    private Button mSubmit;
    private CaseListPOJO.CaseList caseObj;
    private String mCourtID = "";
    private String mJudgeID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_three_step_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
        if (caseObj == null) {
            Utils.showToast(mContext, getString(R.string.something_went_wrong));
            finish();
            return;
        }
        initViews();
    }

    private void initViews() {
        mDateHearingReceived = (TextView) findViewById(R.id.date_hearing_notice_received);
        mDateFirstHearing = (TextView) findViewById(R.id.date_first_hearing);
        mCourtName = (Spinner) findViewById(R.id.name_trial_court);
        mNameJudge = (Spinner) findViewById(R.id.name_trial_judge);
        mRefNum = (EditText) findViewById(R.id.file_ref_number);
        mSubmit = (Button) findViewById(R.id.submit);

        fetchCourtNames();
        setupDatePickers();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFormAPI();
            }
        });
    }

    private void submitFormAPI() {
        if (allFieldsValid()) {

            new ServerRequest<UserInfoJson>(mContext, Constants.getForm2SubmitUrl(mContext, caseObj.id, mDateHearingReceived.getText().toString(), mDateFirstHearing.getText().toString(), mCourtID, mJudgeID, mRefNum.getText().toString()), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.intentTo(mContext, StageThreeStepThreeActivity.class);
                        Utils.showToast(mContext, response.body().message);
                        finish();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        }
    }

    private boolean allFieldsValid() {

        if (mDateFirstHearing.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mRefNum.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.ref_num_error));
            return false;
        }
        if (mDateHearingReceived.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mCourtID.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_court_name_error));
            return false;
        }
        if (mJudgeID.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_judge_error));
            return false;
        }

        return true;
    }

    private void fetchCourtNames() {
        new ServerRequest<CourtListPOJO>(mContext, Constants.getCourtListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<CourtListPOJO> call, Response<CourtListPOJO> response) {
                if (response.body().status) {
                    if (response.body().courtList != null && !response.body().courtList.isEmpty()) {
                        setupCourtSpinner(response.body().courtList);
                        fetchJudgesList();
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void fetchJudgesList() {
        new ServerRequest<JudgeListPOJO>(mContext, Constants.getJudgesListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<JudgeListPOJO> call, Response<JudgeListPOJO> response) {
                if (response.body().status) {
                    if (response.body().judgesList != null && !response.body().judgesList.isEmpty()) {
                        setupJudgesSpinner(response.body().judgesList);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void setupJudgesSpinner(final List<JudgeListPOJO.Judge> judgesList) {
        ArrayList<String> list = new ArrayList<>();
        for (JudgeListPOJO.Judge judge : judgesList) {
            list.add(judge.name);
        }

        AdapterStringSpinner adapterOffenceInvestigationSpinner = new AdapterStringSpinner(mContext, list, getString(R.string.select_name_of_court));
        mNameJudge.setAdapter(adapterOffenceInvestigationSpinner);
        mNameJudge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mJudgeID = judgesList.get(position - 1).id;
                } else {
                    mJudgeID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupCourtSpinner(final List<CourtListPOJO.Court> courtList) {
        ArrayList<String> list = new ArrayList<>();
        for (CourtListPOJO.Court court : courtList) {
            list.add(court.name);
        }

        AdapterStringSpinner adapterOffenceInvestigationSpinner = new AdapterStringSpinner(mContext, list, getString(R.string.select_name_of_court));
        mCourtName.setAdapter(adapterOffenceInvestigationSpinner);
        mCourtName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mCourtID = courtList.get(position - 1).id;
                } else {
                    mCourtID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupDatePickers() {
        assignDatePicker(mDateFirstHearing);
        assignDatePicker(mDateHearingReceived);
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

}
