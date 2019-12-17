package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;
import com.kapalert.kadunastategovernment.utils.Constants;

/**
 * Created by win10 on 6/26/2017.
 */

public class UserInfoJson {


    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("user_data")
    public UserData user_data;

    public static class UserData {
        @SerializedName("id")
        public String id;
        @SerializedName("username")
        public String username;
        @SerializedName("casetype")
        public String casetype;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("email")
        public String email;
        @SerializedName("token")
        public String token;
        @SerializedName("avatar")
        public String image;
        @SerializedName("phone")
        public String phone;
        @SerializedName("user_role")
        public String userRole;

        public String getImageURL() {
            return Constants.USER_IMAGE_BASE_URL + image;
        }

        public String getCompleteName() {
            return first_name + " " + last_name;
        }
    }
}
