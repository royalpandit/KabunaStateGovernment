package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterUserList;
import com.kapalert.kadunastategovernment.templates.EventListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

public class EditEvent extends AppBaseActivity implements View.OnClickListener {

    private EditText mName, mDesc;
    private TextView mDate, mStarts, mEnds, mSelectUsers;
    private Button mSubmit;

    private EventListPOJO.EventData mEvent;
    private boolean mAddEvent = true;
    private ArrayList<UserListPOJO.UserDetail> mUserList = new ArrayList<>();
    private Date mEventDate, mEventStart, mEventEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            mEvent = new Gson().fromJson(db.getString(Constants.DB_SELECTED_EVENT), EventListPOJO.EventData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAddEvent = (mEvent == null);
        fetchAllUsersList();
    }

    private void fetchAllUsersList() {
        new ServerRequest<UserListPOJO>(mContext, Constants.getUserListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<UserListPOJO> call, Response<UserListPOJO> response) {
                if (response.body().status) {
                    if (response.body().usersList != null && !response.body().usersList.isEmpty()) {
                        ArrayList<UserListPOJO.UserDetail> list = new ArrayList<>(response.body().usersList);
                        for (int i = 0; i < response.body().usersList.size(); i++) {
                            if (list.get(i).ID.equalsIgnoreCase(Constants.getUser(mContext).id)) {
                                list.remove(i);
                                break;
                            }
                        }
                        mUserList = new ArrayList<>(list);
                    } else {
                        mUserList = new ArrayList<>();
                    }
                    initViews();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void initViews() {
        mName = (EditText) findViewById(R.id.event_name);
        mDesc = (EditText) findViewById(R.id.event_desc);
        mDate = (TextView) findViewById(R.id.event_date);
        mStarts = (TextView) findViewById(R.id.event_start);
        mEnds = (TextView) findViewById(R.id.event_end);
        mSelectUsers = (TextView) findViewById(R.id.select_users);
        mSubmit = (Button) findViewById(R.id.submit);

        mStarts.setOnClickListener(this);
        mEnds.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mSelectUsers.setOnClickListener(this);
        mSubmit.setOnClickListener(this);

        if (!mAddEvent) {
            fillFields();
        }
    }

    private void fillFields() {
        mName.setText(mEvent.title);
        mDesc.setText(mEvent.description);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aaa");

        try {
            Date date = Constants.SET_DATE_FORMAT.parse(mEvent.start);
            Date endDate = Constants.SET_DATE_FORMAT.parse(mEvent.end);
            mEventDate = date;
            mEventStart = date;
            mEventEnd = endDate;

            mDate.setText(dateFormat.format(date));
            mStarts.setText(timeFormat.format(date));
            mEnds.setText(timeFormat.format(endDate));
        } catch (Exception e) {
            mDate.setText(mEvent.start);
        }

        int selectedUsersNum = mEvent.assignedToUsers.split(",").length;
        for (int i = 0; i < mUserList.size(); i++) {
            UserListPOJO.UserDetail userDetail = mUserList.get(i);
            String[] assignedUsers = mEvent.assignedToUsers.split(",");
            for (String assignedUser : assignedUsers) {
                if (userDetail.ID.equalsIgnoreCase(assignedUser)) {
                    userDetail.setSelected(true);
                    break;
                }
            }
        }
        mSelectUsers.setText(String.format(getString(R.string.selected_users_pattern), "" + selectedUsersNum));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.event_date:
                selectDate();
                break;
            case R.id.event_start:
                if (mAddEvent)
                    showTimePicker(mStarts, Calendar.getInstance(), true);
                else {
                    try {
                        Date date = Constants.SET_DATE_FORMAT.parse(mEvent.start);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        showTimePicker(mStarts, cal, true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        showTimePicker(mStarts, Calendar.getInstance(), true);
                    }
                }
                break;
            case R.id.event_end:
                if (mAddEvent)
                    showTimePicker(mEnds, Calendar.getInstance(), false);
                else {
                    try {
                        Date date = Constants.SET_DATE_FORMAT.parse(mEvent.end);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        showTimePicker(mEnds, cal, false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        showTimePicker(mEnds, Calendar.getInstance(), false);
                    }
                }
                break;
            case R.id.select_users:
                selectUsers();
                break;
            case R.id.submit:
                submitClick();
                break;
        }
    }

    private void submitClick() {
        if (allFieldsValid()) {

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(mEventDate);

            Calendar selectedStartTime = Calendar.getInstance();
            selectedStartTime.setTime(mEventStart);

            Calendar selectedEndTime = Calendar.getInstance();
            selectedEndTime.setTime(mEventEnd);

            selectedStartTime.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));
            selectedStartTime.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
            selectedStartTime.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));

            selectedEndTime.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));
            selectedEndTime.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
            selectedEndTime.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));

            Call<UserInfoJson> call;
            if (mAddEvent)
                call = Constants.getEventCreateUrl(
                        mContext,
                        mName.getText().toString(),
                        mDesc.getText().toString(),
                        Constants.SET_DATE_FORMAT.format(selectedStartTime.getTime()),
                        Constants.SET_DATE_FORMAT.format(selectedEndTime.getTime()),
                        getSelectedUsersStr()
                );
            else
                call = Constants.getEventEditUrl(
                        mContext,
                        mEvent.ID,
                        mName.getText().toString(),
                        mDesc.getText().toString(),
                        Constants.SET_DATE_FORMAT.format(selectedStartTime.getTime()),
                        Constants.SET_DATE_FORMAT.format(selectedEndTime.getTime()),
                        getSelectedUsersStr()
                );
            new ServerRequest<UserInfoJson>(mContext, call, true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.showToast(mContext, response.body().message);
                        if (mAddEvent) {
                            onBackPressed();
                        }
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        }
    }

    private String getSelectedUsersStr() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mUserList.size(); i++) {
            UserListPOJO.UserDetail userDetail = mUserList.get(i);
            if (userDetail.isSelected()) {
                if (i != (mUserList.size() - 1)) {
                    stringBuilder.append(userDetail.ID + ",");
                }
            }
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    private boolean allFieldsValid() {
        if (mName.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.all_fields_required));
            return false;
        }
        if (mDesc.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.all_fields_required));
            return false;
        }
        if (mStarts.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.all_fields_required));
            return false;
        }
        if (mEnds.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.all_fields_required));
            return false;
        }
        if (mSelectUsers.getText().toString().isEmpty() || mSelectUsers.getText().toString().startsWith("0 ")) {
            Utils.showToast(mContext, getString(R.string.no_selected_users_error));
            return false;
        }

        return true;
    }

    private void selectUsers() {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_select_users);

        final RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.users_list);

        setupUsersList(userList);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedUsers = 0;
                for (UserListPOJO.UserDetail userDetail : mUserList) {
                    if (userDetail.isSelected()) {
                        selectedUsers++;
                    }
                }
                if (selectedUsers != 0)
                    mSelectUsers.setText(String.format(getString(R.string.selected_users_pattern), "" + selectedUsers));
                else
                    mSelectUsers.setText("");
                dialog.dismiss();
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

    private void setupUsersList(RecyclerView list) {

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);

        AdapterUserList adapterUserList = new AdapterUserList(mContext, mUserList);
        list.setAdapter(adapterUserList);
    }

    private void showTimePicker(final TextView textView, Calendar cal, final boolean isStart) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                if (isStart) {
                    mEventStart = selectedTime.getTime();
                } else {
                    mEventEnd = selectedTime.getTime();
                }
                textView.setText(dateFormat.format(selectedTime.getTime()));
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        if (mEventDate != null)
            cal.setTime(mEventDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                mEventDate = selectedDate.getTime();
                mDate.setText(dateFormat.format(selectedDate.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
