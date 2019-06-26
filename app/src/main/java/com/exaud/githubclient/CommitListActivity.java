package com.exaud.githubclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;


public class CommitListActivity extends AppCompatActivity implements GithubRepository.CommitCallback {
    CommitRecyclerViewAdapter commitRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        Button previousButton = findViewById(R.id.previous_commits);
        Button nextButton = findViewById(R.id.next_commits);

        Intent intent = getIntent();
        String url = intent.getStringExtra(GithubClientActivity.COMMITLIST);

        GithubRepository.getInstance().loadCommits(url,1, this);

        nextButton.setOnClickListener(v -> GithubRepository.getInstance().nextPageCommits());

        previousButton.setOnClickListener(v -> GithubRepository.getInstance().previousPageCommits());
    }

    @Override
    public void showCommit(ArrayList<Commit> commitList) {
        TextView pageNumber= findViewById(R.id.page_number_commits);
        RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
        recyclerView.setAdapter(commitRecyclerViewAdapter);

        commitRecyclerViewAdapter.updateCommitArray(commitList);

        pageNumber.setText(getString(R.string.page_number, GithubRepository.getInstance().currentPageCommits));
    }
}
