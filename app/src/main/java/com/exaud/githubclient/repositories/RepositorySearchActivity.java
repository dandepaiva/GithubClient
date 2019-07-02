package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    public final static String COMMITLIST = "com.exaud.githubclient.commitList";
    private RepositoriesListAdapter githubAdapter;
    private TextView pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories_list);

        RepositoryViewModel viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);

        Button showButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);


        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null && viewModel.getUser() != null) {
            searchButtonPress(viewModel);
            if (!viewModel.isLastPage()) {
                nextButton.setVisibility(View.VISIBLE);
            }
            if (viewModel.getPage() > 1) {
                previousButton.setVisibility(View.VISIBLE);
            }
        }

        TextView searchTextView = findViewById(R.id.search_text);
        pageNumber = findViewById(R.id.page_number);

        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new RepositoriesListAdapter();
        recyclerView.setAdapter(githubAdapter);

        showButton.setOnClickListener(v -> {
            showButton.setEnabled(false);
            viewModel.setPage(1);
            viewModel.setUser(searchTextView.getText().toString());
            searchButtonPress(viewModel);
            nextButton.setVisibility(View.VISIBLE);
            showButton.setEnabled(true);
        });

        nextButton.setOnClickListener(v -> {
            nextButton.setEnabled(false);

            if (viewModel.getUser() == null) {
                showToast("Write a Valid Github Username and Press Find!");
                return;
            }

            GithubRepository.getInstance().loadDataNodes(viewModel.getPage() + 1, viewModel.getUser(), new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            githubAdapter.updateDataNodeArrayList(repositories);
                            int page = viewModel.getPage() + 1;
                            pageNumber.setText(getString(R.string.page_number, page));
                            viewModel.setPage(page);
                            previousButton.setVisibility(View.VISIBLE);

                            if (repositories.size() < 30) {
                                viewModel.setLastPage(true);
                                nextButton.setVisibility(View.INVISIBLE);
                            }

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
                return;
            }

            if (viewModel.getPage() <= 1) {
                showToast("This is the first page!");
                return;
            }

            GithubRepository.getInstance().loadDataNodes(viewModel.getPage() - 1, viewModel.getUser(), new GithubRepository.RepositoryCallback() {
                @Override
                public void showDataNodes(List<Repository> repositories) {
                    if (repositories.size() != 0) {
                        runOnUiThread(() -> {
                            githubAdapter.updateDataNodeArrayList(repositories);
                            int page = viewModel.getPage() - 1;
                            pageNumber.setText(getString(R.string.page_number, page));
                            viewModel.setPage(page);
                            if (page <= 1) {
                                previousButton.setVisibility(View.INVISIBLE);
                            }
                            nextButton.setVisibility(View.VISIBLE);
                            viewModel.setLastPage(false);
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
     * @param repositoryViewModel GithubViewModel the VM used to control page number
     */
    void searchButtonPress(RepositoryViewModel repositoryViewModel) {
        int page = repositoryViewModel.getPage();
        String user = repositoryViewModel.getUser();

        GithubRepository.getInstance().loadDataNodes(page, user, new GithubRepository.RepositoryCallback() {
            @Override
            public void showDataNodes(List<Repository> repositories) {
                if (repositories.size() != 0) {
                    pageNumber.setText(getString(R.string.page_number, page));
                } else {
                    pageNumber.setText("");
                    showToast(GithubClientApplication.getContext().getString(R.string.no_public_repositories_message));
                }
                githubAdapter.updateDataNodeArrayList(repositories);
                repositoryViewModel.setPage(page);
            }

            @Override
            public void onError(String message) {
                githubAdapter.updateDataNodeArrayList(null);
                pageNumber.setText(null);
                showToast(message);
            }
        });
    }
}
