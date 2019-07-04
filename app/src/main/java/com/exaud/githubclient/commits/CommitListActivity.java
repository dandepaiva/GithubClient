package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.R;
import com.exaud.githubclient.databinding.CommitListActivityBinding;
import com.exaud.githubclient.repositories.RepositorySearchActivity;

public class CommitListActivity extends AppCompatActivity {
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;
    private CommitViewModel viewModel;

    private Observable.OnPropertyChangedCallback onCommitListChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            commitRecyclerViewAdapter.updateCommitArray(viewModel.getCommitList().get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommitListActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.commit_list_activity);
        viewModel = ViewModelProviders.of(this).get(CommitViewModel.class);
        binding.setViewmodel(viewModel);

        RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GithubClientApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);

        commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
        recyclerView.setAdapter(commitRecyclerViewAdapter);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            viewModel.setRepositoryUrl(intent.getStringExtra(RepositorySearchActivity.REPOSITORY_URL));
            viewModel.onOpenCommitListActivity();
        } else {
            commitRecyclerViewAdapter.updateCommitArray(viewModel.getCommitList().get());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getCommitList().addOnPropertyChangedCallback(onCommitListChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getCommitList().removeOnPropertyChangedCallback(onCommitListChangedCallback);

        super.onStop();
    }

    private void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    void disableButtons() {
        Button nextButton = findViewById(R.id.next_commits);
        Button previousButton = findViewById(R.id.previous_commits);

        nextButton.setEnabled(false);
        previousButton.setEnabled(false);
    }

    void enableButtons() {
        Button nextButton = findViewById(R.id.next_commits);
        Button previousButton = findViewById(R.id.previous_commits);

        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
    }
}
