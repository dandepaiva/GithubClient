package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Commit;

import java.util.List;

public class CommitViewModel extends ViewModel {
    private String repositoryUrl;
    private int page;
    private ObservableField<List<Commit>> commitList;
    private ObservableBoolean nextButtonEnabled;
    private ObservableBoolean previousButtonEnabled;
    private ObservableField<String> pageNumberCommitsText;

    CommitViewModel() {
        commitList = new ObservableField<>();
        nextButtonEnabled = new ObservableBoolean(true);
        previousButtonEnabled = new ObservableBoolean(true);
        pageNumberCommitsText = new ObservableField<>();
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
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

    public ObservableField<String> getPageNumberCommitsText() {
        return pageNumberCommitsText;
    }

    void onOpenCommitListActivity() {
        GithubRepository.getInstance().loadCommits(1, repositoryUrl, new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {

                CommitViewModel.this.commitList.set(commitList);
                CommitViewModel.this.page = 1;
                pageNumberCommitsText.set(buildPageNumberText(page));
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    public void onNextButtonPress(View view) {
        nextButtonEnabled.set(false);

        int nextPage = page + 1;
        GithubRepository.getInstance().loadCommits(nextPage, repositoryUrl, new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    page = nextPage;
                    pageNumberCommitsText.set(buildPageNumberText(page));
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

    public void onPreviousButtonPress(View view) {
        previousButtonEnabled.set(false);

        if (page <= 1) {
            showToast("This is the first page!");
            previousButtonEnabled.set(true);
            return;
        }

        int previousPage = page - 1;
        GithubRepository.getInstance().loadCommits(previousPage, repositoryUrl, new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                if (commitList.size() > 0) {
                    CommitViewModel.this.commitList.set(commitList);
                    page = previousPage;
                    pageNumberCommitsText.set(buildPageNumberText(page));
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

    String buildPageNumberText(int page) {
        return GithubClientApplication.getContext().getString(R.string.page_number, page);
    }
}
