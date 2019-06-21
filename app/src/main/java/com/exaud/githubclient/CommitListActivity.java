package com.exaud.githubclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;


public class CommitListActivity extends AppCompatActivity{
    private CommitRecyclerViewAdapter commitRecyclerViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.commit_list_activity);

        Intent intent = getIntent();
        ArrayList<Commit> commits = intent.getParcelableArrayListExtra(GithubClientActivity.COMMITLIST);
        Log.e("COMMIT", "onCreate: BEFORE COMMITS");

        for (Commit commit : commits) {
            Log.e("COMMIT", "onCreate: COMMITS: " + commit.getAuthor().getName());
        }

        RecyclerView commitsRecyclerView = findViewById(R.id.commit_recycler_view);

        commitsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commitsRecyclerView.setLayoutManager(layoutManager);
        commitRecyclerViewAdapter = new CommitRecyclerViewAdapter();
        commitsRecyclerView.setAdapter(commitRecyclerViewAdapter);

        commitRecyclerViewAdapter.updateCommitArray(commits);

    }
}
