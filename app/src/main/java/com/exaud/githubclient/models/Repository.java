package com.exaud.githubclient.models;

public class Repository extends BaseModel {
    private String name;
    private String description;

    public Repository(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
