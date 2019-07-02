package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private int page;
    private String user;
    private ObservableField<List<Repository>> repositories;

    public RepositoryViewModel() {
        repositories = new ObservableField<>();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

        GithubRepository.getInstance().loadDataNodes(this.page, this.user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() != 0) {
                    RepositoryViewModel.this.repositories.set(repositories);
                }
            }

            @Override
            public void onError(String message) {
                RepositoryViewModel.this.repositories.set(null);
                setPage(1);
                Log.e("ERROR_SHOW", message);
            }
        });
    }
}
