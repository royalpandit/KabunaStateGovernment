package com.kapalert.kadunastategovernment.activities;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.kapalert.kadunastategovernment.R;

public class CaseCategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner genderspinner;
    private String[] state = { "Please Select Case ", "Civil", "Criminal","Hearing Notice","Motions","Appeals"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_category);
        genderspinner = (Spinner) findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        genderspinner.setSelection(position);
        String selState = (String) genderspinner.getSelectedItem();
       // Toast.makeText(Cas, ""+selState, Toast.LENGTH_SHORT).show();
        if(selState.equals("Civil")){
            Intent it=new Intent(getApplicationContext(),CreateCivilCaseActivity.class);
            startActivity(it);
        }else if(selState.equals("Criminal")) {
            Intent it=new Intent(getApplicationContext(),CreateCaseActivity.class);
            startActivity(it);
        }else if(selState.equals("Hearing Notice")) {
            Intent it=new Intent(getApplicationContext(), Create_HearingNoticeCase_Acitivity.class);
            startActivity(it);
        }else if(selState.equals("Motions")) {
            Intent it=new Intent(getApplicationContext(),Create_MotionCase_Activity.class);
            startActivity(it);
        }else if(selState.equals("Appeals")) {
            Intent it=new Intent(getApplicationContext(),Create_AppealCase_Activity.class);
            startActivity(it);
        }
        else {
            //Toast.makeText(this, "Please Select Case", Toast.LENGTH_SHORT).show();
        }
   
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
