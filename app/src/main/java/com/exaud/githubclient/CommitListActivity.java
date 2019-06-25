package com.exaud.githubclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;


public class CommitListActivity extends AppCompatActivity implements GithubRepository.CommitCallback {
    CommitRecyclerViewAdapter commitRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_list_activity);

        Log.e("COMMIT", "onCreate: BEFORE COMMITS");

        Intent intent = getIntent();
        String url = intent.getStringExtra(GithubClientActivity.COMMITLIST);

        GithubRepository.getInstance().loadCommits(url, this);
    }

    @Override
    public void showCommit(ArrayList<Commit> commitList) {
        RecyclerView recyclerView = findViewById(R.id.commit_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
        recyclerView.setAdapter(commitRecyclerViewAdapter);

        commitRecyclerViewAdapter.updateCommitArray(commitList);

    }
}
