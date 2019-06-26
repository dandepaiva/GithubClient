package com.exaud.githubclient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exaud.githubclient.models.Commit;
import com.exaud.githubclient.models.CommitParent;
import com.exaud.githubclient.models.Repository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GithubRepository {
    Executor executor = Executors.newFixedThreadPool(3);
    RepositoryCallback callback;
    String user;
    CommitCallback commitCallback;
    String url;

    int currentPage = 1;
    int currentPageCommits = 1;

    private GithubRepository() {
    }

    static GithubRepository getInstance() {
        return Singleton.INSTANCE;
    }

    void loadDataNodes(int currentPage, String user, RepositoryCallback callback) {

        if (currentPage <= 0) {
            callback.onError("This is the first page!");
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        String url = "https://api.github.com/users/%1$s/repos?page=%2$d";
        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url, user, currentPage),
                    response -> {
                        Gson gson = new Gson();

                        Repository[] repositories = gson.fromJson(response, Repository[].class);
                        callback.showDataNodes(Arrays.asList(repositories));

                    },
                    error -> {
                        String errorFormat = error.getLocalizedMessage();


                        if (errorFormat == null) {
                            errorFormat = user + " might not exist.";
                        }
                        callback.onError(errorFormat);
                    }
            );
            queue.add(stringRequest);
        };
        executor.execute(runnable);
    }


    void loadCommits(String url, int currentPageCommits, CommitCallback commitCallback) {
        this.currentPageCommits = currentPageCommits;
        this.commitCallback = commitCallback;
        this.url = url;

        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "/commits?page=" + currentPageCommits,
                    response -> {
                        Gson gson = new Gson();

                        CommitParent[] commits = gson.fromJson(response, CommitParent[].class);

                        if (this.currentPageCommits > 1 && commits.length == 0) {
                            this.currentPageCommits--;
                            callback.onError("No more pages!");
                            return;
                        }

                        ArrayList<Commit> commitList = new ArrayList<>();
                        for (CommitParent commit : commits) {
                            commitList.add(commit.getCommit());
                        }
                        commitCallback.showCommit(commitList);
                    },
                    error -> {
                        String errorFormat = error.getLocalizedMessage();
                        if (errorFormat == null) {
                            errorFormat = user + " might not exist.";
                        }
                        //commitCallback.onError(errorFormat);
                    }
            );
            queue.add(stringRequest);
        };
        executor.execute(runnable);


    }

    void nextPageCommits() {
        currentPageCommits++;
        loadCommits(url, currentPageCommits, commitCallback);
    }

    void previousPageCommits() {
        if (currentPageCommits > 1) {
            currentPageCommits--;
            loadCommits(url, currentPageCommits, commitCallback);
        }
    }

    public interface RepositoryCallback {
        void showDataNodes(List<Repository> repositories);
        void onError(String message);
    }

    public interface CommitCallback {
        void showCommit(ArrayList<Commit> commitList);
        //void onError(String message);
    }

    private static class Singleton {
        private static final GithubRepository INSTANCE = new GithubRepository();
    }
}
