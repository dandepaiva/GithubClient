package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;

import com.exaud.githubclient.models.Commit;

import java.util.List;

public class CommitViewModel extends ViewModel {
    private String repositoryUrl;
    private int page;
    private List<Commit> commitList;

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }
}
