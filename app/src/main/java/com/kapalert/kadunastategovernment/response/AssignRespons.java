package com.kapalert.kadunastategovernment.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignRespons {

@SerializedName("status")
@Expose
private String status;
@SerializedName("alert")
@Expose
private String alert;
@SerializedName("assignTo")
@Expose
private List<AssignTo> assignTo = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getAlert() {
return alert;
}

public void setAlert(String alert) {
this.alert = alert;
}

public List<AssignTo> getAssignTo() {
return assignTo;
}

public void setAssignTo(List<AssignTo> assignTo) {
this.assignTo = assignTo;
}

}