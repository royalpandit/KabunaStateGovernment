package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/30/2017.
 */

public class ChatListPOJO {


    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("chat")
    public List<ChatMessage> chat;

    public static class ChatMessage {
        @SerializedName("ID")
        public String ID;
        @SerializedName("message")
        public String message;
        @SerializedName("timestamp")
        public long timestamp;
        @SerializedName("userid")
        public String userid;
        @SerializedName("username")
        public String username;
        @SerializedName("chatid")
        public String chatID;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("online_timestamp")
        public long online_timestamp;

        public String getCompleteName() {
            return first_name + " " + last_name;
        }
    }
}
