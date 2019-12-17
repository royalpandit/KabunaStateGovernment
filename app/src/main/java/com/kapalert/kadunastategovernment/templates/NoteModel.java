package com.kapalert.kadunastategovernment.templates;

public class NoteModel
{
    String id;
    String notes;

    public NoteModel(String id, String notes) {
        this.id = id;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
