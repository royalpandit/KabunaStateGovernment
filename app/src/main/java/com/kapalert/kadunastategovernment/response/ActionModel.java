package com.kapalert.kadunastategovernment.response;

public class ActionModel {
    String name, id;
    boolean selected = false;

    public ActionModel(String name, String id, boolean selected) {
        this.name = name;
        this.id = id;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
