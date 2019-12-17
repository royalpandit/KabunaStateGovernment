package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kapalert.kadunastategovernment.response.ActionModel;

import java.util.List;

/**
 * Created by win10 on 8/15/2017.
 */

public class OffencePOJO {


    @SerializedName("status")
    public String status;
    @SerializedName("alert")
    public String alert;
    @SerializedName("offenceList")
    public List<OffenceModal> offenceList;
    @SerializedName("investigationUnitList")
    public List<OffenceModal> offenceInvestList;
    @SerializedName("offenceLocationList")
    public List<OffenceModal> offenceLocationList;


}
