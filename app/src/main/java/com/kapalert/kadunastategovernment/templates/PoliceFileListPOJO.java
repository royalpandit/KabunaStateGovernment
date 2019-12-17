package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/30/2017.
 */

public class PoliceFileListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("accusedList")
    public List<PoliceFile> fileList;

    public static class PoliceFile {
        @SerializedName("id")
        public String id;
        @SerializedName("case_id")
        public String case_id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("file_name")
        public String file_name;
        @SerializedName("manual_file_name")
        public String manual_file_name;
        @SerializedName("comments")
        public String comments;
        @SerializedName("step")
        public String step;
        @SerializedName("get_type")
        public String get_type;
        @SerializedName("date_time")
        public String date_time;
    }
}
