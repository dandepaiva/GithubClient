package com.exaud.githubclient;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exaud.githubclient.models.Repository;

import java.util.List;

public class GithubClientActivity extends AppCompatActivity implements GithubRepository.RepositoryCallback {
    public final static String COMMITLIST = "com.exauc.githubclient.COMMITLIST";
    private GithubClientAdapter githubAdapter;
    private static Context context;
    TextView pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        context = this;
        Button showButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);
        pageNumber = findViewById(R.id.page_number);

        TextView searchTextView = findViewById(R.id.search_text);
        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new GithubClientAdapter();
        recyclerView.setAdapter(githubAdapter);

        showButton.setOnClickListener(v -> {

            String searchString = searchTextView.getText().toString();

            GithubRepository.getInstance().loadDataNodes(GithubClientActivity.this, 1, searchString);
            pageNumber.setText(getString(R.string.page_number, 1));
        });

        nextButton.setOnClickListener(v -> {
            GithubRepository.getInstance().nextPage();
        });

        previousButton.setOnClickListener(v -> {
            GithubRepository.getInstance().previousPage();
        });
    }

    @Override
    public void showDataNodes(List<Repository> repositories) {
        if (repositories.size() == 0) {
            showToast(GithubClientApplication.getContext().getString(R.string.no_public_repositories_message));
        }
        runOnUiThread(() ->{
            githubAdapter.updateDataNodeArrayList(repositories);
            pageNumber.setText(getString(R.string.page_number, GithubRepository.getInstance().currentPage));
        });
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    void showToast(String text) {
        Toast.makeText(GithubClientApplication.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    static void startCommitListActivity(String url) {
        Intent commitListActivity = new Intent(context, CommitListActivity.class);
        commitListActivity.putExtra(COMMITLIST, url);
        context.startActivity(commitListActivity);
    }
}
