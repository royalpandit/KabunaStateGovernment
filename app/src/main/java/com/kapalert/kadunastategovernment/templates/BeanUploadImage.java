package com.kapalert.kadunastategovernment.templates;

import android.net.Uri;

public class BeanUploadImage {

    String name;
    String uri;

    public BeanUploadImage(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

