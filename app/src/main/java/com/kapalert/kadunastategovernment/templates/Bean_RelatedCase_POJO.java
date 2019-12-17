package com.kapalert.kadunastategovernment.templates;

public class Bean_RelatedCase_POJO
{
    String relatedCase_id = "";
    String relatedCase_name = "";

    public Bean_RelatedCase_POJO(String relatedCase_id, String relatedCase_name) {
        this.relatedCase_id = relatedCase_id;
        this.relatedCase_name = relatedCase_name;
    }

    public String getRelatedCase_id() {
        return relatedCase_id;
    }

    public void setRelatedCase_id(String relatedCase_id) {
        this.relatedCase_id = relatedCase_id;
    }

    public String getRelatedCase_name() {
        return relatedCase_name;
    }

    public void setRelatedCase_name(String relatedCase_name) {
        this.relatedCase_name = relatedCase_name;
    }
}
