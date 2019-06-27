package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class GithubViewModel extends ViewModel {
    private int repositoryPage=78;
    private int commitsPage = 87;
    private boolean isPressed = false;

    public int getCommitsPage() {
        return commitsPage;
    }

    public boolean setCommitsPage(int commitsPage) {
        if(commitsPage<=0){
            Log.e("PAGE", "setCommitsPage: PAGE ERROR");
            return false;
        }
        this.commitsPage = commitsPage;
        return true;
    }


    public boolean setRepositoryPage(int page){
        if(commitsPage<=0){
            Log.e("PAGE", "setCommitsPage: PAGE ERROR");
            return false;
        }
        this.repositoryPage = page;
        return true;
    }

    public int getRepositoryPage(){
        return repositoryPage;
    }

    void setPressed(boolean press){
        isPressed = press;
    }

    boolean isPressed(){
        return isPressed;
    }
}
