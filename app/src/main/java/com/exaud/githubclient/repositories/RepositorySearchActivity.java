package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableList;
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
import com.exaud.githubclient.commits.CommitListActivity;
import com.exaud.githubclient.models.Repository;

import java.util.List;

public class RepositorySearchActivity extends AppCompatActivity {
    public final static String REPOSITORY_URL = "repository_url";
    private RepositoriesListAdapter githubAdapter;
    private TextView pageNumber;
    private RepositoryViewModel viewModel;

    private Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories_list);

        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        Button findButton = findViewById(R.id.button_show);
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

        if (savedInstanceState != null && viewModel.getRepositories() != null) {
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage()));
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }

        findButton.setOnClickListener(v -> {
            findButton.setEnabled(false);
            viewModel.setPage(1);
            viewModel.setUser(searchTextView.getText().toString());

            viewModel.onFindButtonPress();

            //showToast(GithubClientApplication.getContext().getString(R.string.no_public_repositories_message));

            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());

            findButton.setEnabled(true);
        });

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);

            if (viewModel.getUser() == null) {
                showToast("Write a Valid Github Username and Press Find!");
                nextButton.setEnabled(true);
                return;
            }

            GithubRepository.getInstance().loadDataNodes(viewModel.getPage() + 1, viewModel.getUser(), new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            //viewModel.setRepositories(repositories);
                            githubAdapter.updateDataNodeArrayList(repositories);
                            int page = viewModel.getPage() + 1;
                            pageNumber.setText(getString(R.string.page_number, page));
                            viewModel.setPage(page);
                        });
                    } else {
                        this.onError("No more pages for " + viewModel.getUser() + "!");
                    }
                    nextButton.setEnabled(true);

                }

                @Override
                public void onError(String message) {
                    showToast(message);
                    nextButton.setEnabled(true);
                }
            });

        });


        previousButton.setOnClickListener(v -> {
            previousButton.setEnabled(false);

            if (viewModel.getUser() == null) {
                showToast("Write a Valid Github Username and Press Find!");
                previousButton.setEnabled(true);
                return;
            }

            if (viewModel.getPage() <= 1) {
                showToast("This is the first page!");
                previousButton.setEnabled(true);
                return;
            }

            GithubRepository.getInstance().loadDataNodes(viewModel.getPage() - 1, viewModel.getUser(), new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            //viewModel.setRepositories(repositories);

                            githubAdapter.updateDataNodeArrayList(repositories);
                            int page = viewModel.getPage() - 1;
                            pageNumber.setText(getString(R.string.page_number, page));
                            viewModel.setPage(page);
                        });
                    }
                    previousButton.setEnabled(true);
                }

                @Override
                public void onError(String message) {
                    showToast(message);
                    previousButton.setEnabled(true);
                }
            });
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getRepositories().addOnPropertyChangedCallback(onPropertyChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getRepositories().removeOnPropertyChangedCallback(onPropertyChangedCallback);

        super.onStop();
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
        commitListActivity.putExtra(REPOSITORY_URL, url);
        GithubClientApplication.getContext().startActivity(commitListActivity);
    }
}
