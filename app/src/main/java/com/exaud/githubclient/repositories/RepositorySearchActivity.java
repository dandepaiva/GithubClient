package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.R;
import com.exaud.githubclient.commits.CommitListActivity;
import com.exaud.githubclient.databinding.ActivityRepositoriesListBinding;
import com.exaud.githubclient.models.Repository;

public class RepositorySearchActivity extends AppCompatActivity implements RepositoriesListAdapter.OnRepositorySelectedListener {
    public final static String REPOSITORY_URL = "repository_url";
    private RepositoriesListAdapter githubAdapter;
    private RepositoryViewModel viewModel;

    private Observable.OnPropertyChangedCallback onRepositoriesChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRepositoriesListBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_repositories_list);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);
        binding.setViewmodel(viewModel);

        githubAdapter = new RepositoriesListAdapter(this);
        binding.setAdapter(githubAdapter);

        if (savedInstanceState != null && viewModel.getRepositories() != null) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getRepositories().addOnPropertyChangedCallback(onRepositoriesChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getRepositories().removeOnPropertyChangedCallback(onRepositoriesChangedCallback);

        super.onStop();
    }

    @Override
    public void onRepositorySelected(Repository repository) {
        Intent commitListActivity = new Intent(GithubClientApplication.getContext(), CommitListActivity.class);
        commitListActivity.putExtra(REPOSITORY_URL, repository.getUrl());
        startActivity(commitListActivity);
    }
}
