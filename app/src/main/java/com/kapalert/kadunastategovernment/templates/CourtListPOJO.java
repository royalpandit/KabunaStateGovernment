package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/31/2017.
 */

public class CourtListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("caseList")
    public List<Court> courtList;

    public static class Court {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }
}
