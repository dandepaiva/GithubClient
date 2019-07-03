package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private String user;
    private ObservableInt page;
    private ObservableField<List<Repository>> repositories;
    private ObservableBoolean nextButtonEnabled;
    private ObservableBoolean previousButtonEnabled;
    private ObservableBoolean findButtonEnabled;

    public RepositoryViewModel() {
        repositories = new ObservableField<>();
        page = new ObservableInt();
        nextButtonEnabled = new ObservableBoolean();
        previousButtonEnabled = new ObservableBoolean();
        findButtonEnabled = new ObservableBoolean();
    }

    public ObservableInt getPage() {
        return page;
    }

    public void setUser(String user) {
        this.user = user;
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

    void onFindButtonPress() {
        findButtonEnabled.set(false);

        GithubRepository.getInstance().loadDataNodes(1, this.user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                }
                /*TODO
                * else { USER HAS NO PUBLIC REPOSITORIES }
                */
                page.set(1);
                findButtonEnabled.set(true);
            }

            @Override
            public void onError(String message) {
                repositories.set(null);
                page.set(1);
                showToast(message);
                findButtonEnabled.set(true);
            }
        });
    }

    void onNextButtonPress() {
        nextButtonEnabled.set(false);
        int nextPage = this.page.get() + 1;

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
                    page.set(nextPage);
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

    void onPreviousButtonPress() {
        previousButtonEnabled.set(false);
        int previousPage = this.page.get() - 1;

        if (user == null) {
            showToast("Write a Valid Github Username and Press Find!");
            previousButtonEnabled.set(true);
            return;
        }

        if (page.get() <= 1) {
            showToast("This is the first page!");
            previousButtonEnabled.set(true);
            return;
        }


        GithubRepository.getInstance().loadDataNodes(previousPage, this.user, new GithubRepository.RepositoryCallback() {

            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
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
