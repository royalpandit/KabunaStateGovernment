package com.kapalert.kadunastategovernment.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MdaInvokedRespons {

@SerializedName("status")
@Expose
private String status;
@SerializedName("alert")
@Expose
private String alert;
@SerializedName("mdaList")
@Expose
private List<MdaList> mdaList = null;

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

public List<MdaList> getMdaList() {
return mdaList;
}

public void setMdaList(List<MdaList> mdaList) {
this.mdaList = mdaList;
}

}