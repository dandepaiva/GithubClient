package com.exaud.githubclient;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    private String user="exaud";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories_list);

        RepositoryViewModel repositoryViewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        Button showButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);

        TextView searchTextView = findViewById(R.id.search_text);
        pageNumber = findViewById(R.id.page_number);


        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new RepositoriesListAdapter();
        recyclerView.setAdapter(githubAdapter);

        pageCount = repositoryViewModel.getRepositoryPage();
        user = repositoryViewModel.getUser();

        if (savedInstanceState!=null && repositoryViewModel.getRepositoryPage()>0) {
            searchButtonPress(repositoryViewModel);

        }

        showButton.setOnClickListener(v -> {
            repositoryViewModel.setRepositoryPage(pageCount= 1);
            repositoryViewModel.setUser(user = searchTextView.getText().toString());
            searchButtonPress(repositoryViewModel);
        });

        nextButton.setOnClickListener(v -> {

            if(repositoryViewModel.getRepositoryPage()<0) {
                showToast("Write a Github Username and Press Find!");
                return;
            }

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
                        this.onError("No more pages for " + user + "!");
                        nextButton.setEnabled(true);
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
                showToast("This is the first page!");
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
     * @param repositoryViewModel GithubViewModel the VM used to control page number
     */
    void searchButtonPress(RepositoryViewModel repositoryViewModel){
        int pageCount = repositoryViewModel.getRepositoryPage();
        String user = repositoryViewModel.getUser();
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

    public void search(View view) {
    }
}
