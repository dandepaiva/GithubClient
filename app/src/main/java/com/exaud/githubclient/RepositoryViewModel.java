package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class RepositoryViewModel extends ViewModel {
    private int repositoryPage=-1;
    private String user;

    public int getRepositoryPage(){
        return repositoryPage;
    }

    public boolean setRepositoryPage(int page){
        if(page<=0){
            Log.e("PAGE", "setCommitsPage: PAGE ERROR");
            return false;
        }
        this.repositoryPage = page;
        return true;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
