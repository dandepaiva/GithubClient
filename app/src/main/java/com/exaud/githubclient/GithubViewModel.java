package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;

public class GithubViewModel extends ViewModel {
    private int page=78;
    private boolean isPressed = false;

    public void saveData(int page){
        this.page = page;
    }

    public int getPage(){
        return page;
    }

    void setPressed(boolean press){
        isPressed = press;
    }

    boolean isPressed(){
        return isPressed;
    }
}
