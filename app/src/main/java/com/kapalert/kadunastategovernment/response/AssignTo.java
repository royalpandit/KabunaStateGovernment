package com.kapalert.kadunastategovernment.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignTo {

@SerializedName("id")
@Expose
private String id;
@SerializedName("group_name")
@Expose
private String groupName;
@SerializedName("secretary_name")
@Expose
private String secretaryName;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getGroupName() {
return groupName;
}

public void setGroupName(String groupName) {
this.groupName = groupName;
}

public String getSecretaryName() {
return secretaryName;
}

public void setSecretaryName(String secretaryName) {
this.secretaryName = secretaryName;
}

}