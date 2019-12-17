package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterDPPCaseList;
import com.kapalert.kadunastategovernment.adapters.AdapterPoliceFileList;
import com.kapalert.kadunastategovernment.templates.PoliceFileListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class StageNineActivity extends AppCompatActivity {


    TextView datetv;
    Button submit;
    private Calendar mDateOffence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_nine);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();
       final String caseid =(String) b.get("caseid");

        datetv=(TextView) findViewById(R.id.datereqmda);
        submit=(Button) findViewById(R.id.ok);
        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(StageNineActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                        mDateOffence = selectedDate;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        datetv.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerRequest<UserInfoJson>(StageNineActivity.this, Constants.getPoliceDateUrl(StageNineActivity.this, caseid,datetv.getText().toString()), true) {
                    @Override
                    public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                        if (response.body().status) {

                            Utils.showToast(StageNineActivity.this, response.body().message);
                            Intent it=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(it);
                        } else {
                            Utils.showToast(StageNineActivity.this, response.body().errorMessage);
                        }
                    }
                };
            }
        });
    }

}
