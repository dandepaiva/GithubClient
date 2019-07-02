package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;

import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private int page;
    private String user;
    private List<Repository> repositories;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
