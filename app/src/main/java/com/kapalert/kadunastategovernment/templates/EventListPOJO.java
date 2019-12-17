package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/23/2017.
 */

public class EventListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("eventList")
    public List<EventData> eventList;

    public static class EventData {
        @SerializedName("ID")
        public String ID;
        @SerializedName("title")
        public String title;
        @SerializedName("start")
        public String start;
        @SerializedName("end")
        public String end;
        @SerializedName("description")
        public String description;
        @SerializedName("userid")
        public String userid;
        @SerializedName("projectid")
        public String projectid;
        @SerializedName("assignedToUsers")
        public String assignedToUsers;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
    }
}
