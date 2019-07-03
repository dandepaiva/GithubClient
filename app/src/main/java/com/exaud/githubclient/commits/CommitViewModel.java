package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.models.Commit;

import java.util.List;

public class CommitViewModel extends ViewModel {
    private String repositoryUrl;
    private ObservableInt page;
    private ObservableField<List<Commit>> commitList;
    private ObservableBoolean nextButtonEnabled;
    private ObservableBoolean previousButtonEnabled;

    CommitViewModel() {
        commitList = new ObservableField<>();
        page = new ObservableInt();
        nextButtonEnabled = new ObservableBoolean();
        previousButtonEnabled = new ObservableBoolean();
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

    public ObservableBoolean getNextButtonEnabled() {
        return nextButtonEnabled;
    }

    public ObservableBoolean getPreviousButtonEnabled() {
        return previousButtonEnabled;
    }

    void onOpenCommitListActivity() {
        GithubRepository.getInstance().loadCommits(1, repositoryUrl, new GithubRepository.CommitCallback() {
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

    void onNextButtonPress() {
        nextButtonEnabled.set(false);

        int nextPage = page.get() + 1;
        GithubRepository.getInstance().loadCommits(nextPage, repositoryUrl, new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    page.set(nextPage);
                } else {
                    onError("This is the last page");
                }
                nextButtonEnabled.set(true);
            }

            @Override
            public void onError(String message) {
                showToast(message);
                nextButtonEnabled.set(true);
            }
        });
    }

    void onPreviousButtonPress() {
        previousButtonEnabled.set(false);

        if (page.get() <= 1) {
            showToast("This is the first page!");
            previousButtonEnabled.set(true);
            return;
        }

        int previousPage = page.get() - 1;
        GithubRepository.getInstance().loadCommits(previousPage, repositoryUrl, new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    page.set(previousPage);
                }
                previousButtonEnabled.set(true);

            }

            @Override
            public void onError(String message) {
                showToast(message);
                previousButtonEnabled.set(true);
            }
        });
    }

    void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
