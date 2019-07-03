package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private static final String TAG = "VIEW_MODEL";
    private String user;
    private ObservableInt page;
    private ObservableField<List<Repository>> repositories;

    public RepositoryViewModel() {
        repositories = new ObservableField<>();
        page = new ObservableInt();
    }

    public ObservableInt getPage() {
        return page;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ObservableField<List<Repository>> getRepositories() {
        return repositories;
    }

    void onFindButtonPress() {

        GithubRepository.getInstance().loadDataNodes(this.page.get(), this.user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() != 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                }
            }

            @Override
            public void onError(String message) {
                RepositoryViewModel.this.repositories.set(null);
                page.set(1);
                showToast(message);
            }
        });
    }

    void onNextButtonPress() {
        int nextPage = this.page.get() + 1;
        GithubRepository.getInstance().loadDataNodes(nextPage, this.user, new GithubRepository.RepositoryCallback() {

            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() > 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                    page.set(nextPage);
                } else {
                    this.onError("No more pages for " + getUser() + "!");
                }
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    void onPreviousButtonPress(){
        int previousPage = this.page.get()-1;

        GithubRepository.getInstance().loadDataNodes(previousPage, this.user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if(repositories.size() > 0){
                    RepositoryViewModel.this.repositories.set(repositories);
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
