package com.kapalert.kadunastategovernment.templates;

import com.google.gson.annotations.SerializedName;
import com.kapalert.kadunastategovernment.utils.Constants;

/**
 * Created by win10 on 6/26/2017.
 */

public class NoteJSON {


    @SerializedName("status")
    public boolean status;
    @SerializedName("errorMessage")
    public String errorMessage;
    @SerializedName("message")
    public String message;



}
