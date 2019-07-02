package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

        if (savedInstanceState == null) {
            viewModel.setPage(1);
        }

        if (viewModel.isLastPage()) {
            nextButton.setVisibility(View.INVISIBLE);
        }

        if (viewModel.getPage() <= 1) {
            previousButton.setVisibility(View.INVISIBLE);
        }


        Intent intent = getIntent();
        viewModel.setUrl(intent.getStringExtra(RepositorySearchActivity.COMMITLIST));

        GithubRepository.getInstance().loadCommits(viewModel.getUrl(), viewModel.getPage(), new GithubRepository.CommitCallback() {
            @Override
            public void showCommits(List<Commit> commitList) {
                TextView pageNumber = findViewById(R.id.page_number_commits);
                RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GithubClientApplication.getContext());
                recyclerView.setLayoutManager(layoutManager);

                commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
                recyclerView.setAdapter(commitRecyclerViewAdapter);

                commitRecyclerViewAdapter.updateCommitArray(commitList);

                if (commitList.size() < 30) {
                    viewModel.setLastPage(true);
                    nextButton.setVisibility(View.INVISIBLE);
                }

                pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));
            }

            @Override
            public void onError(String message) {

            }
        });

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);

            GithubRepository.getInstance().loadCommits(viewModel.getUrl(), viewModel.getPage() + 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    if (commitList.size() != 0) {
                        commitRecyclerViewAdapter.updateCommitArray(commitList);
                        int page = viewModel.getPage() + 1;
                        pageNumber.setText(getString(R.string.page_number, page));
                        viewModel.setPage(page);
                        previousButton.setVisibility(View.VISIBLE);

                        if (commitList.size() < 30) {
                            viewModel.setLastPage(true);
                            nextButton.setVisibility(View.INVISIBLE);
                        }
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
                return;
            }

            GithubRepository.getInstance().loadCommits(viewModel.getUrl(), viewModel.getPage() - 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    if (commitList.size() != 0) {
                        commitRecyclerViewAdapter.updateCommitArray(commitList);
                        int page = viewModel.getPage() - 1;
                        viewModel.setPage(page);
                        pageNumber.setText(getString(R.string.page_number, page));

                        if (page <= 1) {
                            previousButton.setVisibility(View.INVISIBLE);
                        }

                        nextButton.setVisibility(View.VISIBLE);
                        viewModel.setLastPage(false);
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
