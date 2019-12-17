package com.kapalert.kadunastategovernment.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StageNineResponse {

@SerializedName("status")
@Expose
private String status;
@SerializedName("errorMessage")
@Expose
private String errorMessage;
@SerializedName("message")
@Expose
private String message;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getErrorMessage() {
return errorMessage;
}

public void setErrorMessage(String errorMessage) {
this.errorMessage = errorMessage;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}