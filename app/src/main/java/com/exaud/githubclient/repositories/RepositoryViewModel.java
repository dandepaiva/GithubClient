package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private String user;
    private int page;
    private ObservableField<List<Repository>> repositories;
    private ObservableBoolean nextButtonEnabled;
    private ObservableBoolean previousButtonEnabled;
    private ObservableBoolean findButtonEnabled;
    private ObservableField<String> pageNumberText;
    private ObservableField<String> userText;

    public RepositoryViewModel() {
        repositories = new ObservableField<>();
        nextButtonEnabled = new ObservableBoolean(true);
        previousButtonEnabled = new ObservableBoolean(true);
        findButtonEnabled = new ObservableBoolean(true);
        pageNumberText = new ObservableField<>();
        userText = new ObservableField<>("googlesamples");
    }

    public ObservableField<List<Repository>> getRepositories() {
        return repositories;
    }

    public ObservableBoolean getNextButtonEnabled() {
        return nextButtonEnabled;
    }

    public ObservableBoolean getPreviousButtonEnabled() {
        return previousButtonEnabled;
    }

    public ObservableBoolean getFindButtonEnabled() {
        return findButtonEnabled;
    }

    public ObservableField<String> getPageNumberText() {
        return pageNumberText;
    }

    public ObservableField<String> getUserText() {
        return userText;
    }

    public void onFindButtonPress(View view) {
        findButtonEnabled.set(false);
        user = userText.get();

        GithubRepository.getInstance().loadDataNodes(1, this.user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                }
                page = 1;
                pageNumberText.set(buildPageNumberText(page));
                findButtonEnabled.set(true);
            }

            @Override
            public void onError(String message) {
                repositories.set(null);
                page = 1;
                pageNumberText.set(buildPageNumberText(page));
                showToast(message);
                findButtonEnabled.set(true);
            }
        });
    }

    public void onNextButtonPress(View view) {
        nextButtonEnabled.set(false);
        int nextPage = this.page + 1;

        if (user == null) {
            showToast("Write a Valid Github Username and Press Find!");
            nextButtonEnabled.set(true);
            return;
        }


        GithubRepository.getInstance().loadDataNodes(nextPage, this.user, new GithubRepository.RepositoryCallback() {

            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                    page = nextPage;
                    pageNumberText.set(buildPageNumberText(page));
                } else {
                    this.onError("No more pages for " + user + "!");
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
        int previousPage = this.page - 1;

        if (user == null) {
            showToast("Write a Valid Github Username and Press Find!");
            previousButtonEnabled.set(true);
            return;
        }

        if (page <= 1) {
            showToast("This is the first page!");
            previousButtonEnabled.set(true);
            return;
        }


        GithubRepository.getInstance().loadDataNodes(previousPage, this.user, new GithubRepository.RepositoryCallback() {

            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                    page = previousPage;
                    pageNumberText.set(buildPageNumberText(page));
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
