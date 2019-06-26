package com.exaud.githubclient.models;

public class Repository extends BaseModel {
    private final String name;
    private final String description;
    private final String url;


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
