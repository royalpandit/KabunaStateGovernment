package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by win10 on 8/16/2017.
 */

public class FileListPOJO {

    @SerializedName("status")
    public boolean status;
    @SerializedName("alert")
    public String alert;
    @SerializedName("caseFileList")
    public List<CaseFile> caseFile;

    public static class CaseFile {
        @SerializedName("id")
        public String id;
        @SerializedName("file_name")
        public String file_name;
        @SerializedName("case_id")
        public String case_id;
    }
}
