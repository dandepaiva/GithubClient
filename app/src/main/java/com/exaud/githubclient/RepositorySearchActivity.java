package com.exaud.githubclient;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositorySearchActivity extends AppCompatActivity {
    public final static String COMMITLIST = "com.exaud.githubclient.commitList";
    private RepositoriesListAdapter githubAdapter;
    private TextView pageNumber;
    private int pageCount;
    private static String user="exaud";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories_list);

        GithubViewModel repositoryViewModel = ViewModelProviders.of(this).get(GithubViewModel.class);

        Button showButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);
        pageNumber = findViewById(R.id.page_number);

        TextView searchTextView = findViewById(R.id.search_text);
        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new RepositoriesListAdapter();
        recyclerView.setAdapter(githubAdapter);

        if (savedInstanceState!=null){
            if(repositoryViewModel.isPressed()) {
                pageCount = repositoryViewModel.getRepositoryPage();
                searchButtonPress(user, repositoryViewModel);
            }
        } else {
            repositoryViewModel.setPressed(false);
        }

        showButton.setOnClickListener(v -> {
            repositoryViewModel.setPressed(true);
            repositoryViewModel.setRepositoryPage(1);
            user = searchTextView.getText().toString();
            searchButtonPress(user, repositoryViewModel);
        });

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);
            GithubRepository.getInstance().loadDataNodes(pageCount + 1, user, new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            nextButton.setEnabled(githubAdapter.updateDataNodeArrayList(repositories));
                            pageCount++;
                            pageNumber.setText(getString(R.string.page_number, pageCount));
                            repositoryViewModel.setRepositoryPage(pageCount);

                        });
                    } else {
                        nextButton.setEnabled(true);
                        this.onError("No more pages for " + user + "!");
                    }
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                }
            });

        });


        previousButton.setOnClickListener(v -> {

            if(pageCount<=1) {
                showToast("This is the first page!\n[ONLCICK]");
                return;
            }

            GithubRepository.getInstance().loadDataNodes(pageCount - 1, user, new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            githubAdapter.updateDataNodeArrayList(repositories);
                            pageCount--;
                            pageNumber.setText(getString(R.string.page_number, pageCount));
                            repositoryViewModel.setRepositoryPage(pageCount);
                        });
                    }
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                    pageNumber.setText(getString(R.string.page_number, 1));
                }
            });
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    private void showToast(String text) {
        Toast.makeText(GithubClientApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    static void startCommitListActivity(String url) {
        Intent commitListActivity = new Intent(GithubClientApplication.getContext(), CommitListActivity.class);
        commitListActivity.putExtra(COMMITLIST, url);
        GithubClientApplication.getContext().startActivity(commitListActivity);
    }

    /**
     *
     * @param user String of the user to be searched for
     * @param repositoryViewModel GithubViewModel the VM used to control page number
     */
    void searchButtonPress(String user, GithubViewModel repositoryViewModel){
        int pageCount = repositoryViewModel.getRepositoryPage();
        GithubRepository.getInstance().loadDataNodes(pageCount, user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() != 0) {
                    runOnUiThread(() -> {
                        githubAdapter.updateDataNodeArrayList(repositories);
                        pageNumber.setText(getString(R.string.page_number, pageCount));
                        repositoryViewModel.setRepositoryPage(pageCount);
                    });
                } else {
                    showToast(GithubClientApplication.getContext().getString(R.string.no_public_repositories_message));
                }
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
        pageNumber.setText(getString(R.string.page_number, pageCount));
    }
}
