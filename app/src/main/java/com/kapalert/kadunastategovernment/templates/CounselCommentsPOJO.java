package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/28/2017.
 */

public class CounselCommentsPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("commentsList")
    public List<Comment> comments;

    public static class Comment {
        @SerializedName("comments")
        public String comment;
        @SerializedName("file_name")
        public String fileName;
    }
}
