package com.kapalert.kadunastategovernment.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisputeRespons {

@SerializedName("status")
@Expose
private String status;
@SerializedName("alert")
@Expose
private String alert;
@SerializedName("offenceLocationList")
@Expose
private List<OffenceLocationList> offenceLocationList = null;

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

public List<OffenceLocationList> getOffenceLocationList() {
return offenceLocationList;
}

public void setOffenceLocationList(List<OffenceLocationList> offenceLocationList) {
this.offenceLocationList = offenceLocationList;
}

}