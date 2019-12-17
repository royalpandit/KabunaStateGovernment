package com.kapalert.kadunastategovernment.activities;



import com.google.gson.JsonElement;

import java.util.ArrayList;

public class CreateCaseJSON1   {
    private ArrayList<CreateCivilCaseActivity.AccusedModal1> accused_data;
    private ArrayList<CreateCivilCaseActivity.AccusedModal2> accused_data1;
    private ArrayList<String> nature_of_offence;

    public CreateCaseJSON1(ArrayList<CreateCivilCaseActivity.AccusedModal1> accused_data, ArrayList<CreateCivilCaseActivity.AccusedModal2> accused_data1, ArrayList<String> nature_of_offence) {
        this.accused_data = accused_data;
        this.accused_data1=accused_data1;
        this.nature_of_offence = nature_of_offence;
    }


}
