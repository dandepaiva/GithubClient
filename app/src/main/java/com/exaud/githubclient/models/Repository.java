package com.exaud.githubclient.models;

import com.google.gson.annotations.SerializedName;

public class Repository extends BaseModel {
    private String name;
    private String description;
    private String url;


    public Repository(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
