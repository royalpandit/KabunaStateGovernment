package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterStringSpinner;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CourtListPOJO;
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

public class StageThreeStepOneActivity extends AppBaseActivity {

    private Spinner mCourtName;
    private TextView mDateCPETyping, mDateCPECopies, mDateLitigationSent, mDateCPECourtSent;
    private String mCourtID = "";
    private Button mSubmit;
    private CaseListPOJO.CaseList caseObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_three_stage_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);
        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
        if (caseObj == null) {
            Utils.showToast(mContext, getString(R.string.something_went_wrong));
            finish();
            return;
        }

        fetchCourtList();
    }

    private void initViews() {
        mCourtName = (Spinner) findViewById(R.id.court_name);
        mDateCPETyping = (TextView) findViewById(R.id.cpe_typing_date);
        mDateCPECopies = (TextView) findViewById(R.id.cpe_copies_date);
        mDateLitigationSent = (TextView) findViewById(R.id.cpe_signed_date);
        mDateCPECourtSent = (TextView) findViewById(R.id.cpe_sent_to_court_date);
        mSubmit = (Button) findViewById(R.id.submit);

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
            new ServerRequest<UserInfoJson>(mContext, Constants.getForm1SubmitUrl(mContext, caseObj.id, mDateCPETyping.getText().toString(), mDateCPECopies.getText().toString(), mDateCPECourtSent.getText().toString(), mDateLitigationSent.getText().toString(), mCourtID), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.showToast(mContext, response.body().message);
                        Utils.intentTo(mContext, StageThreeStepTwoActivity.class);
                        finish();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        }
    }

    private boolean allFieldsValid() {

        if (mDateCPETyping.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mDateCPECopies.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mDateLitigationSent.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mDateCPECourtSent.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_all_dates_error));
            return false;
        }
        if (mCourtID.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.select_court_name_error));
            return false;
        }

        return true;
    }

    private void fetchCourtList() {
        new ServerRequest<CourtListPOJO>(mContext, Constants.getCourtListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<CourtListPOJO> call, Response<CourtListPOJO> response) {
                if (response.body().status) {
                    if (response.body().courtList != null && !response.body().courtList.isEmpty()) {
                        initViews();
                        setupCourtSpinner(response.body().courtList);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
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
        assignDatePicker(mDateCPECopies);
        assignDatePicker(mDateCPECourtSent);
        assignDatePicker(mDateCPETyping);
        assignDatePicker(mDateLitigationSent);
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
