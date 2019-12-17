package com.kapalert.kadunastategovernment.templates;

import java.util.ArrayList;

/**
 * Created by win10 on 8/16/2017.
 */

public class SelectJudgementJSON {

    private ArrayList<AccusedJudgementJson> accused_data;

    public SelectJudgementJSON(ArrayList<AccusedJudgementJson> accused_data) {
        this.accused_data = accused_data;
    }

    public static class AccusedJudgementJson {
        String accusedID;
        String judgementID;

        public AccusedJudgementJson(String accusedID, String judgementID) {
            this.accusedID = accusedID;
            this.judgementID = judgementID;
        }

        public String getJudgementID() {
            return judgementID;
        }

        public void setJudgementID(String judgementID) {
            this.judgementID = judgementID;
        }

        public String getAccusedID() {
            return accusedID;
        }

        public void setAccusedID(String accusedID) {
            this.accusedID = accusedID;
        }

    }
}
