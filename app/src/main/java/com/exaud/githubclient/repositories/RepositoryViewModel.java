package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;

public class RepositoryViewModel extends ViewModel {
    private int page;
    private String user;

    public int getPage(){
        return page;
    }

    public void setPage(int page){
        this.page = page;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
