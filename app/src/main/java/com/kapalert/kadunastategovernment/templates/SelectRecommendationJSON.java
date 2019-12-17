package com.kapalert.kadunastategovernment.templates;

import com.kapalert.kadunastategovernment.activities.CreateCaseActivity;

import java.util.ArrayList;

/**
 * Created by win10 on 8/16/2017.
 */

public class SelectRecommendationJSON {

    private ArrayList<AccusedJson> accused_data;

    public SelectRecommendationJSON(ArrayList<AccusedJson> accused_data) {
        this.accused_data = accused_data;
    }

    public static class AccusedJson {
        String accusedID;
        int recommendID;

        public AccusedJson(String accusedID, int recommendID) {
            this.accusedID = accusedID;
            this.recommendID = recommendID;
        }

        public String getAccusedID() {
            return accusedID;
        }

        public void setAccusedID(String accusedID) {
            this.accusedID = accusedID;
        }

        public int getRecommendID() {
            return recommendID;
        }

        public void setRecommendID(int recommendID) {
            this.recommendID = recommendID;
        }
    }
}
