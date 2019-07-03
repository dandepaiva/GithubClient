package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.R;
import com.exaud.githubclient.repositories.RepositorySearchActivity;

public class CommitListActivity extends AppCompatActivity {
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;
    private CommitViewModel viewModel;
    private TextView pageNumber;

    private Observable.OnPropertyChangedCallback onCommitListChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            commitRecyclerViewAdapter.updateCommitArray(viewModel.getCommitList().get());
        }
    };

    Observable.OnPropertyChangedCallback onPageChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage().get()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        viewModel = ViewModelProviders.of(this).get(CommitViewModel.class);

        Button previousButton = findViewById(R.id.previous_commits);
        Button nextButton = findViewById(R.id.next_commits);
        pageNumber = findViewById(R.id.page_number_commits);

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
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage().get()));
            commitRecyclerViewAdapter.updateCommitArray(viewModel.getCommitList().get());
        }

        nextButton.setOnClickListener(v -> {
            disableButtons();

            viewModel.onNextButtonPress();

            enableButtons();
        });

        previousButton.setOnClickListener(v -> {
            disableButtons();

            if (viewModel.getPage().get() <= 1) {
                showToast("This is the first page!");
                enableButtons();
                return;
            }
            viewModel.onPreviousButtonPress();
            enableButtons();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getCommitList().addOnPropertyChangedCallback(onCommitListChangedCallback);
        viewModel.getPage().addOnPropertyChangedCallback(onPageChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getCommitList().removeOnPropertyChangedCallback(onCommitListChangedCallback);
        viewModel.getPage().removeOnPropertyChangedCallback(onPageChangedCallback);
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
