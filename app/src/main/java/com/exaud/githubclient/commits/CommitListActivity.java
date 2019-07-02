package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Commit;
import com.exaud.githubclient.repositories.RepositorySearchActivity;

import java.util.List;

public class CommitListActivity extends AppCompatActivity {
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        CommitViewModel viewModel = ViewModelProviders.of(this).get(CommitViewModel.class);

        Button previousButton = findViewById(R.id.previous_commits);
        Button nextButton = findViewById(R.id.next_commits);
        TextView pageNumber = findViewById(R.id.page_number_commits);

        RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GithubClientApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);

        commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
        recyclerView.setAdapter(commitRecyclerViewAdapter);

        if (savedInstanceState == null) {
            viewModel.setPage(1);
            Intent intent = getIntent();
            viewModel.setRepositoryUrl(intent.getStringExtra(RepositorySearchActivity.REPOSITORY_URL));

            GithubRepository.getInstance().loadCommits(viewModel.getRepositoryUrl(), viewModel.getPage(), new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {

                    commitRecyclerViewAdapter.updateCommitArray(commitList);
                    viewModel.setCommitList(commitList);
                    pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));
                }

                @Override
                public void onError(String message) {

                }
            });
        } else {
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));
            commitRecyclerViewAdapter.updateCommitArray(viewModel.getCommitList());
        }

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);

            GithubRepository.getInstance().loadCommits(viewModel.getRepositoryUrl(), viewModel.getPage() + 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    if (commitList.size() != 0) {
                        commitRecyclerViewAdapter.updateCommitArray(commitList);
                        viewModel.setCommitList(commitList);

                        int page = viewModel.getPage() + 1;
                        pageNumber.setText(getString(R.string.page_number, page));
                        viewModel.setPage(page);
                    } else {
                        onError("This is the last page!");
                    }
                    nextButton.setEnabled(true);
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                    nextButton.setEnabled(true);
                }
            });
        });

        previousButton.setOnClickListener(v -> {
            previousButton.setEnabled(false);

            if (viewModel.getPage() <= 1) {
                showToast("This is the first page!");
                previousButton.setEnabled(true);
                return;
            }

            GithubRepository.getInstance().loadCommits(viewModel.getRepositoryUrl(), viewModel.getPage() - 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    if (commitList.size() != 0) {
                        commitRecyclerViewAdapter.updateCommitArray(commitList);
                        viewModel.setCommitList(commitList);

                        int page = viewModel.getPage() - 1;
                        viewModel.setPage(page);
                        pageNumber.setText(getString(R.string.page_number, page));
                    }
                    previousButton.setEnabled(true);
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                    previousButton.setEnabled(true);
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
