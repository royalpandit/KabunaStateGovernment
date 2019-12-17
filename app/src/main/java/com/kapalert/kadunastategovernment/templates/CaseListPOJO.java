package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/16/2017.
 */

public class CaseListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("alert")
    public String alert;
    @SerializedName("caseList")
    public List<CaseList> caseList;

    public static class CaseList {
        @SerializedName("id")
        public String id;
        @SerializedName("file_name")
        public String file_name;
        @SerializedName("stage")
        public int stage;
        @SerializedName("step")
        public int step;
        @SerializedName("to_user_or_role_id")
        public String ownerID = "";
        @SerializedName("user_id")
        public String acceptedBy = "";
        @SerializedName("case_creator_id")
        public String litigationID = "";
        @SerializedName("isRecommendationDone")
        public String isRecommendationDone = "";
        @SerializedName("isLitigationDateDone")
        public String isLitigationDateDone = "";
        @SerializedName("totalCount")
        public String fileCount;
        @SerializedName("ppd_file_reference")
        public String fileRefNum;

    }
}
