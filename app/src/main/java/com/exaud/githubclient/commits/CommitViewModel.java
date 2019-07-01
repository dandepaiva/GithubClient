package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;

import com.exaud.githubclient.models.Commit;

import java.util.List;

public class CommitViewModel extends ViewModel {
    List<Commit> commits;
    private String url;
    private int page;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
