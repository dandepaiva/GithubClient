package com.exaud.githubclient;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.exaud.githubclient.models.Commit;


public class CommitListActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.commit_list_activity);

        RecyclerView commitsRecyclerView = findViewById(R.id.commit_recycler_view);

        commitsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commitsRecyclerView.setLayoutManager(layoutManager);


    }
}
