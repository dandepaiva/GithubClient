package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;

public class GithubViewModel extends ViewModel {
    private int page=78;

    public GithubViewModel() {
        super();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public boolean saveData(int page){
        this.page = page;
        return true;
    }

    public int getPage(){
        return page;
    }
}
