package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;

public class CommitViewModel extends ViewModel {
    boolean lastPage;
    private String url;
    private int page;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
