package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 9/8/2017.
 */

public class AnnouncementListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("announcementList")
    public List<Announcement> announcementList;

    public static class Announcement {
        @SerializedName("id")
        public String id;
        @SerializedName("from_user")
        public String from_user;
        @SerializedName("to_user")
        public String to_user;
        @SerializedName("message")
        public String message;
        @SerializedName("created_at")
        public String created_at;
    }
}
