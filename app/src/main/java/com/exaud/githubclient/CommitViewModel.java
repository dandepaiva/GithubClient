package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class CommitViewModel extends ViewModel {
    private int commitsPage = 87;

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

}
