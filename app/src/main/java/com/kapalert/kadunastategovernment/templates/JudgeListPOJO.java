package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/31/2017.
 */

public class JudgeListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("judgeList")
    public List<Judge> judgesList;

    public static class Judge {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
}
