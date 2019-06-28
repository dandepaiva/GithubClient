package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class RepositoryViewModel extends ViewModel {
    private int repositoryPage=-1;
    private String user;

    public int getRepositoryPage(){
        return repositoryPage;
    }

    public void setRepositoryPage(int page){
        if(page<=0){
            Log.e("PAGE", "setCommitsPage: PAGE ERROR");
        }
        this.repositoryPage = page;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
