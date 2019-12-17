package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 9/5/2017.
 */

public class InboxListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("activeChat")
    public List<ActiveChat> activeChat;

    public static class ActiveChat {
        @SerializedName("chatID")
        public String ID;
        @SerializedName("title")
        public String title;
        @SerializedName("active")
        public String active;
        @SerializedName("unread")
        public String unread;
        @SerializedName("chatuserid")
        public String chatuserid;
        @SerializedName("name")
        public String userFirstName;
        @SerializedName("last_name")
        public String userLastName;
        @SerializedName("lc_title")
        public String lc_title;
        @SerializedName("to_user_id")
        public String toUserID;

        public String getCompleteName() {
            return userFirstName ;
        }
    }
}
