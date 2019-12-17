package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win10 on 9/11/2017.
 */

public class CaseDetailPOJO  {

    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;
    @SerializedName("litigation")
    public Litigation litigation;
    @SerializedName("dpp")
    public Dpp dpp;
    @SerializedName("counsel")
    public Counsel counsel;
    @SerializedName("casenotes")
    public List<CaseNotes> caseNotes;


    public static class Litigation {
        @SerializedName("stage1")
        public Stage1 stage1;
        @SerializedName("stage2")
        public Stage2 stage2;

        public static class Stage2 {
            @SerializedName("step1")
            public Step1 step1;
            @SerializedName("step10")
            public Step10 step10;

            public static class Step10 {
                @SerializedName("filesAndCommentsByLitigation")
                public ArrayList<FilesAndComments> filesAndCommentsLitigation;
            }

            public static class Step1 {
                @SerializedName("caseRecievedByLitigationFromCounsel")
                public String caseRecievedByLitigationFromCounsel;
                @SerializedName("caseSentByLitigationToCounsel")
                public String caseSentByLitigationToCounsel;
            }
        }

        public static class Stage1 {
            @SerializedName("step1")
            public Step1 step1;
            @SerializedName("step4")
            public Step4 step4;


            public static class Step1 {
                @SerializedName("caseCreatedbyLitigation")
                public String caseCreatedbyLitigation;
                @SerializedName("caseSentByDppToLitigation")
                public String caseSentByDppToLitigation;
                @SerializedName("caseAcceptByDpp")
                public String caseAcceptByDpp;
            }

            public static class Step4 {
                @SerializedName("caseRecievedByLitigationFromCounsel")
                public String caseRecievedByLitigationFromCounsel;
                @SerializedName("caseSentByLitigationToDpp")
                public String caseSentByLitigationToDpp;
            }
        }
    }


    public static class Dpp {
        @SerializedName("stage1")
        public Stage1 stage1;

        public static class Stage1 {
            @SerializedName("step2")
            public Step2 step2;
            @SerializedName("step5")
            public Step5 step5;
            @SerializedName("step9")
            public Step9 step9;

            public static class Step9 {
                @SerializedName("accusedRecommendations")
                public ArrayList<AccusedRecommendations> accusedRecommendations;
            }

            public static class Step2 {
                @SerializedName("caseRecievedFromLitigation")
                public String caseRecievedFromLitigation;
                @SerializedName("caseSentByDppToCounsel")
                public String caseSentByDppToCounsel;
                @SerializedName("counselName")
                public String counselName;
            }

            public static class Step5 {
                @SerializedName("caseRecievedByDppFromLitigation")
                public String caseRecievedByDppFromLitigation;
                @SerializedName("caseSentByDppToLitigation")
                public String caseSentByDppToLitigation;
                @SerializedName("filesAndCommentsByDpp")
                public FilesAndComments filesAndCommentsByDpp;

            }
        }
    }

    public static class FilesAndComments {
        @SerializedName("file_name")
        public String file_name;
        @SerializedName("comments")
        public String comments;
        @SerializedName("get_type")
        public String fileType;
    }

    public static class AccusedRecommendations {
        @SerializedName("recommendation")
        public String recommendation;
        @SerializedName("accuse_name")
        public String accuse_name;
    }

    public static class Counsel {
        @SerializedName("stage1")
        public Stage1 stage1;

        @SerializedName("stage3")
        public Stage3 stage3;




        public static class Stage1 {

            @SerializedName("step3")
            public Step3 step3;

            public static class Step3 {
                @SerializedName("caseSentByDpp")
                public String caseSentByDpp;
                @SerializedName("caseSentByCounselToLitigation")
                public String caseSentByCounselToLitigation;
                @SerializedName("filesAndCommentsByCounsel")
                public List<FilesAndComments> filesAndCommentsByCounsel;
                @SerializedName("accusedRecommendations")
                public List<AccusedRecommendations> accusedRecommendations;

            }


        }



        public static class Stage3 {
            @SerializedName("step1")
            public Step1 step1;

            public static class Step1 {
                @SerializedName("caseRecievedByCounselFromLitigation")
                public String caseRecievedByCounselFromLitigation;
                @SerializedName("caseClosedOn")
                public String caseClosedOn;
            }
        }
    }

    public static class CaseNotes {
        @SerializedName("id")
        public String id;
        @SerializedName("case_note")
        public String caseNote;




    }
}