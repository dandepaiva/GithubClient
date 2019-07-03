package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Commit;
import com.exaud.githubclient.models.Repository;
import com.exaud.githubclient.repositories.RepositoryViewModel;

import java.util.List;

public class CommitViewModel extends ViewModel {
    private String repositoryUrl;
    private ObservableInt page;
    private ObservableField<List<Commit>> commitList;

    CommitViewModel(){
        commitList = new ObservableField<>();
        page = new ObservableInt();
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public ObservableInt getPage() {
        return page;
    }

    public ObservableField<List<Commit>> getCommitList() {
        return commitList;
    }

    void onOpenCommitListActivity(){
        GithubRepository.getInstance().loadCommits(1, getRepositoryUrl(), new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {

                CommitViewModel.this.commitList.set(commitList);
                CommitViewModel.this.page.set(1);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    void onNextButtonPress(){
        int nextPage = getPage().get() + 1;
        GithubRepository.getInstance().loadCommits(nextPage, getRepositoryUrl(), new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    CommitViewModel.this.page.set(nextPage);
                } else {
                    onError("This is the last page");
                }
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    void onPreviousButtonPress(){
        int previousPage = getPage().get() - 1;
        GithubRepository.getInstance().loadCommits(previousPage, getRepositoryUrl(), new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    getPage().set(previousPage);
                }
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
