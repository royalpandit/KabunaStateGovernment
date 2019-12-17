package com.kapalert.kadunastategovernment.templates;

import java.util.ArrayList;

/**
 * Created by win10 on 9/4/2017.
 */

public class AdjournmentDataJSON {

    public static class AdjournmentData {
        public String hearingDate, adjourmentReasonId, newHearingDate, progressMade;

        public AdjournmentData(String hearingDate, String adjourmentReasonId, String newHearingDate, String progressMade) {
            this.hearingDate = hearingDate;
            this.adjourmentReasonId = adjourmentReasonId;
            this.newHearingDate = newHearingDate;
            this.progressMade = progressMade;
        }

        public String getHearingDate() {
            return hearingDate;
        }

        public void setHearingDate(String hearingDate) {
            this.hearingDate = hearingDate;
        }

        public String getAdjourmentReasonId() {
            return adjourmentReasonId;
        }

        public void setAdjourmentReasonId(String adjourmentReasonId) {
            this.adjourmentReasonId = adjourmentReasonId;
        }

        public String getNewHearingDate() {
            return newHearingDate;
        }

        public void setNewHearingDate(String newHearingDate) {
            this.newHearingDate = newHearingDate;
        }

        public String getProgressMade() {
            return progressMade;
        }

        public void setProgressMade(String progressMade) {
            this.progressMade = progressMade;
        }
    }

    public ArrayList<AdjournmentData> adjournment_data = new ArrayList<>();

    public AdjournmentDataJSON(ArrayList<AdjournmentData> list) {
        this.adjournment_data = list;
    }
}
