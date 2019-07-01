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
import com.exaud.githubclient.repositories.RepositorySearchActivity;
import com.exaud.githubclient.models.Commit;

import java.util.List;


public class CommitListActivity extends AppCompatActivity implements CommitViewModel.ErrorListener {
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        CommitViewModel viewModel = ViewModelProviders.of(this).get(CommitViewModel.class);

        if(savedInstanceState == null){
            viewModel.setPage(1);
        }

        Button previousButton = findViewById(R.id.previous_commits);
        Button nextButton = findViewById(R.id.next_commits);
        TextView pageNumber = findViewById(R.id.page_number_commits);

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

                viewModel.setCurrentList(commitList);
                commitRecyclerViewAdapter.updateCommitArray(commitList);

                pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));
            }

            @Override
            public void onError(String message) {

            }
        });


        /* ----------STUPID*/
        GithubRepository.getInstance().loadCommits(
                viewModel.getUrl(), viewModel.getPage() + 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    if (commitList.size() != 0) {
                        viewModel.setNextList(commitList);
                    } else {
                        onError("This is the last page!");
                    }
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                }
            });
        /* ----------STUPID*/




        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);

            List<Commit> commitList = viewModel.onPressNext(this);
            commitRecyclerViewAdapter.updateCommitArray(commitList);
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));

            nextButton.setEnabled(true);


        });

        previousButton.setOnClickListener(v -> {
            if (viewModel.getPage() <= 1) {
                onError("This is the first page!");
                return;
            }

            previousButton.setEnabled(false);

            List<Commit> commitList = viewModel.onPressPrevious(this);
            commitRecyclerViewAdapter.updateCommitArray(commitList);
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));

            previousButton.setEnabled(true);
        });
    }

    private void showToast(String message) {
        Toast.makeText(GithubClientApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errorMessage) {
        showToast(errorMessage);
    }
}
