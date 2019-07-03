package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.Observable;
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

    private Observable.OnPropertyChangedCallback onRepositoriesChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }
    };

    private  Observable.OnPropertyChangedCallback onPageChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage().get()));
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
            pageNumber.setText(getString(R.string.page_number, viewModel.getPage().get()));
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }

        findButton.setOnClickListener(v -> {
            deactivateButtons();
            viewModel.getPage().set(1);
            viewModel.setUser(searchTextView.getText().toString());

            viewModel.onFindButtonPress();
            activateButtons();

        });

        nextButton.setOnClickListener(v -> {
            deactivateButtons();

            if (viewModel.getUser() == null) {
                showToast("Write a Valid Github Username and Press Find!");
                activateButtons();
                return;
            }

            viewModel.onNextButtonPress();
            activateButtons();
        });


        previousButton.setOnClickListener(v -> {
            deactivateButtons();

            if (viewModel.getUser() == null) {
                showToast("Write a Valid Github Username and Press Find!");
                activateButtons();
                return;
            }

            if (viewModel.getPage().get() <= 1) {
                showToast("This is the first page!");
                activateButtons();
                return;
            }

            viewModel.onPreviousButtonPress();
            activateButtons();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getRepositories().addOnPropertyChangedCallback(onRepositoriesChangedCallback);
        viewModel.getPage().addOnPropertyChangedCallback(onPageChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getRepositories().removeOnPropertyChangedCallback(onRepositoriesChangedCallback);
        viewModel.getPage().removeOnPropertyChangedCallback(onPageChangedCallback);
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

    void deactivateButtons(){
        Button findButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);

        findButton.setEnabled(false);
        nextButton.setEnabled(false);
        previousButton.setEnabled(false);

    }
    void activateButtons(){
        Button findButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);

        findButton.setEnabled(true);
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);

    }
}
