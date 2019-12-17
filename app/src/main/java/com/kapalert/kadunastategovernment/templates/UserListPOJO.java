package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/23/2017.
 */

public class UserListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("usersList")
    public List<UserDetail> usersList;

    public static class UserDetail {
        @SerializedName("ID")
        public String ID;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("username")
        public String username;

        public boolean selected;

        public UserDetail(String first_name, String last_name) {
            this.first_name = first_name;
            this.last_name = last_name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getCompleteName() {
            return first_name + " " + last_name;
        }
    }
}
