package com.kapalert.kadunastategovernment.templates;

import com.kapalert.kadunastategovernment.activities.CreateCaseActivity;

import java.util.ArrayList;

/**
 * Created by win10 on 8/16/2017.
 */

public class CreateCaseJSON {

    private ArrayList<CreateCaseActivity.AccusedModal> accused_data;
    private ArrayList<String> nature_of_offence;

    public CreateCaseJSON(ArrayList<CreateCaseActivity.AccusedModal> accused_data, ArrayList<String> nature_of_offence) {
        this.accused_data = accused_data;
        this.nature_of_offence = nature_of_offence;
    }
}
