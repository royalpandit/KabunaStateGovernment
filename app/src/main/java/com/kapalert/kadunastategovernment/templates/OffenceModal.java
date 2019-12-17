package com.kapalert.kadunastategovernment.templates;

/**
 * Created by win10 on 8/15/2017.
 */

public class OffenceModal {

    String name, id;
    boolean selected = false;

    public OffenceModal(String name, String id) {
        this.name = name;
        this.id = id;
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
