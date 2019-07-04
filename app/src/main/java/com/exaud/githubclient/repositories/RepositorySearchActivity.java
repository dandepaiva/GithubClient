package com.exaud.githubclient.repositories;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.exaud.githubclient.GithubClientApplication;
import com.exaud.githubclient.R;
import com.exaud.githubclient.commits.CommitListActivity;
import com.exaud.githubclient.databinding.ActivityRepositoriesListBinding;

public class RepositorySearchActivity extends AppCompatActivity {
    public final static String REPOSITORY_URL = "repository_url";
    private RepositoriesListAdapter githubAdapter;
    private RepositoryViewModel viewModel;
    private Button findButton;

    private Observable.OnPropertyChangedCallback onRepositoriesChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }
    };

    private Observable.OnPropertyChangedCallback onFindFinishedChangedCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            findButton.setEnabled(viewModel.getFindButtonEnabled().get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRepositoriesListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_repositories_list);
        viewModel = ViewModelProviders.of(this).get(RepositoryViewModel.class);
        binding.setViewmodel(viewModel);

        findButton = findViewById(R.id.button_show);


        EditText searchText = findViewById(R.id.search_text);

        RecyclerView recyclerView = findViewById(R.id.repository_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        githubAdapter = new RepositoriesListAdapter();
        recyclerView.setAdapter(githubAdapter);

        if (savedInstanceState != null && viewModel.getRepositories() != null) {
            githubAdapter.updateDataNodeArrayList(viewModel.getRepositories().get());
        }

        findButton.setOnClickListener(v -> {
            viewModel.setUser(searchText.getText().toString());
            viewModel.onFindButtonPress();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getRepositories().addOnPropertyChangedCallback(onRepositoriesChangedCallback);
        viewModel.getFindButtonEnabled().addOnPropertyChangedCallback(onFindFinishedChangedCallback);
    }

    @Override
    protected void onStop() {
        viewModel.getRepositories().removeOnPropertyChangedCallback(onRepositoriesChangedCallback);
        viewModel.getFindButtonEnabled().removeOnPropertyChangedCallback(onFindFinishedChangedCallback);

        super.onStop();
    }

    static void startCommitListActivity(String url) {
        Intent commitListActivity = new Intent(GithubClientApplication.getContext(), CommitListActivity.class);
        commitListActivity.putExtra(REPOSITORY_URL, url);
        GithubClientApplication.getContext().startActivity(commitListActivity);
    }

    // UNUSED
    /*
    private void showToast(String text) {
        Toast.makeText(GithubClientApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    void disableButtons() {
        Button findButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);

        findButton.setEnabled(false);
        nextButton.setEnabled(false);
        previousButton.setEnabled(false);
    }

    void enableButtons() {
        Button findButton = findViewById(R.id.button_show);
        Button nextButton = findViewById(R.id.next);
        Button previousButton = findViewById(R.id.previous);

        findButton.setEnabled(true);
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
    }
    */
}
