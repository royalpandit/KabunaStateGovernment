package com.kapalert.kadunastategovernment.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kapalert.kadunastategovernment.templates.OffenceModal;

public class ActionListRespons {

@SerializedName("status")
@Expose
private String status;
@SerializedName("alert")
@Expose
private String alert;
@SerializedName("actionList")
@Expose
public List<ActionModel> actionList ;

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

public List<ActionModel> getActionList() {
return actionList;
}

public void setActionList(List<ActionModel> actionList) {
this.actionList = actionList;
}

}