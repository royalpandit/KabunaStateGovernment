package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/28/2017.
 */

public class CounselAccusedListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("accusedList")
    public List<Accused> accuseds;

    public static class Accused {
        @SerializedName("id")
        public String id;
        @SerializedName("accuse_name")
        public String accuse_name;
        @SerializedName("recommendation")
        public int selectedReccomendation = 0;
        @SerializedName("recommend_by")
        public String recommendBy = "";
        @SerializedName("step_no")
        public String stepNo = "";
        public String selectedJudgementID = "";
    }
}
