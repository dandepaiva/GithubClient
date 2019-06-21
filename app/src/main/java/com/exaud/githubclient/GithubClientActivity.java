package com.exaud.githubclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.exaud.githubclient.models.Commit;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class GithubClientActivity extends AppCompatActivity implements GithubRepository.RepositoryCallback {
    public final static String COMMITLIST = "com.exauc.githubclient.COMMITLIST";
    private GithubClientAdapter githubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showButton = findViewById(R.id.button_show);

        TextView searchTextView = findViewById(R.id.search_text);
        CheckBox userCheckBox = findViewById(R.id.user_check_box);
        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new GithubClientAdapter();
        recyclerView.setAdapter(githubAdapter);

        showButton.setOnClickListener(v -> {

            String searchString = searchTextView.getText().toString();
            // if the client want to search for a user
            if (userCheckBox.isChecked()) {
                showToast("CHECKBOX TRUE");
            }

            GithubRepository.getInstance().loadDataNodes(GithubClientActivity.this, searchString);
        });

        GithubRepository.getInstance().loadDataNodes(GithubClientActivity.this, searchTextView.getText().toString());
    }

    @Override
    public void showDataNodes(List<Repository> repositories) {
        if (repositories.size() == 0) {
            showToast(GithubClientApplication.getContext().getString(R.string.no_public_repositories_message));
        }
        runOnUiThread(() -> githubAdapter.updateDataNodeArrayList(repositories));
    }

    @Override
    public void onError(String message) {
        showToast(message);
    }

    void showToast(String text) {
        Toast.makeText(GithubClientApplication.getContext(), text, Toast.LENGTH_SHORT).show();

    }
}
