package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 9/1/2017.
 */

public class CommonListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("adjournmentReasonList")
    public AllList allList;

    public static class AllList {
        @SerializedName("accusedJudgementList")
        public List<CommonList> judgementList;
        @SerializedName("adjournmentReasonList")
        public List<CommonList> adjournmentReasonList;
        @SerializedName("caseClosureList")
        public List<CommonList> caseClosureList;
    }

    public static class CommonList {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
}
