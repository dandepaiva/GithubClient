package com.exaud.githubclient;

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

import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;


public class CommitListActivity extends AppCompatActivity {
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;
    private int pageCount;
    private String urlPersistent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        CommitViewModel commitsViewModel = ViewModelProviders.of(this).get(CommitViewModel.class);
        if (savedInstanceState!=null){
            pageCount = commitsViewModel.getCommitsPage();
        } else {
            pageCount = 1;
        }

        Button previousButton = findViewById(R.id.previous_commits);
        Button nextButton = findViewById(R.id.next_commits);
        TextView pageNumber = findViewById(R.id.page_number_commits);

        Intent intent = getIntent();
        String url = intent.getStringExtra(RepositorySearchActivity.COMMITLIST);


        GithubRepository.getInstance().loadCommits(url, pageCount, new GithubRepository.CommitCallback() {
            @Override
            public void showCommit(ArrayList<Commit> commitList) {
                urlPersistent = url;

                commitsViewModel.setCommitsPage(pageCount);

                TextView pageNumber = findViewById(R.id.page_number_commits);
                RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GithubClientApplication.getContext());
                recyclerView.setLayoutManager(layoutManager);

                commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
                recyclerView.setAdapter(commitRecyclerViewAdapter);

                commitRecyclerViewAdapter.updateCommitArray(commitList);

                pageNumber.setText(getString(R.string.page_number, pageCount));
            }

            @Override
            public void onError(String message) {

            }
        });

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);
            GithubRepository.getInstance().loadCommits(urlPersistent, pageCount + 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommit(ArrayList<Commit> commitList) {
                    if (commitList.size() != 0) {
                        nextButton.setEnabled(commitRecyclerViewAdapter.updateCommitArray(commitList));
                        pageCount++;
                        pageNumber.setText(getString(R.string.page_number, pageCount));
                        commitsViewModel.setCommitsPage(pageCount);
                    } else {
                        nextButton.setEnabled(true);
                        onError("This is the last page!");
                    }
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                }
            });
        });

        previousButton.setOnClickListener(v -> {
            if (pageCount <= 0) {
                pageCount = 1;
                return;
            }

            GithubRepository.getInstance().loadCommits(urlPersistent, pageCount - 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommit(ArrayList<Commit> commitList) {
                    if (commitList.size() != 0) {
                        commitRecyclerViewAdapter.updateCommitArray(commitList);
                        pageCount--;
                        pageNumber.setText(getString(R.string.page_number, pageCount));
                        commitsViewModel.setCommitsPage(pageCount);
                    }
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
