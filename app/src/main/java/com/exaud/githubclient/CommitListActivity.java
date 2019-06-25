package com.exaud.githubclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;


public class CommitListActivity extends AppCompatActivity implements GithubRepository.CommitCallback {

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
        TextView nameTextView = findViewById(R.id.name_view);
        TextView messageTextView = findViewById(R.id.message_view);
        TextView urlTextView = findViewById(R.id.url_view);

        Commit commit = commitList.get(1);
        //nameTextView.setText(commit.getAuthor().getName());
        messageTextView.setText(commit.getMessage());
        urlTextView.setText(commit.getUrl());
    }
}
