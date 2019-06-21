package com.exaud.githubclient;

import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exaud.githubclient.models.Commit;
import com.exaud.githubclient.models.Repository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.support.v4.content.ContextCompat.startActivity;


public class GithubRepository {
    private static final String TAG = "REPOSITORY";
    Executor executor = Executors.newFixedThreadPool(3);
    String user;
    //private Set<RepositoryCallback> callbacks = new HashSet<>();

    private GithubRepository() {
    }

    static GithubRepository getInstance() {
        return Singleton.INSTANCE;
    }

    void loadDataNodes(RepositoryCallback callback, String user) {
        //addToSet(callback);

        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        String url = "https://api.github.com/users/%1$s/repos";
        this.user = user;

        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url, user),
                    response -> {
                        // Type repositoryType = new TypeToken<ArrayList<Repository>>(){}.getType();
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


    void loadCommits(Repository repository) {
        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        String url = "https://api.github.com/repos/%1$s/%2$s/commits";

        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url, user, repository.getName()),
                    response -> {
                        Gson gson = new Gson();

                        Commit[] commits = gson.fromJson(response, Commit[].class);
                        ArrayList<Commit> commitList = new ArrayList<>();
                        for (Commit commit : commits) {
                            commitList.add(commit);
                        }

                        Intent commitListActivity = new Intent(GithubClientApplication.getContext(), CommitListActivity.class);
                        commitListActivity.putParcelableArrayListExtra(GithubClientActivity.COMMITLIST, commitList);
                        GithubClientApplication.getContext().startActivity(commitListActivity);
                    },
                    error -> {
                        String errorFormat = error.getLocalizedMessage();
                        if (errorFormat == null) {
                            errorFormat = user + " might not exist.";
                        }
                    }
            );
            queue.add(stringRequest);
        };
        executor.execute(runnable);
    }

    public interface RepositoryCallback {
        void showDataNodes(List<Repository> repositories);
        void onError(String message);
    }

    private static class Singleton {
        private static final GithubRepository INSTANCE = new GithubRepository();
    }

    /*private void lsdask(Class baseModelClass) {
        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url,user),
                    response -> {
                        // Type repositoryType = new TypeToken<ArrayList<Repository>>(){}.getType();
                        Gson gson = new Gson();

                        BaseModel repos = new BaseModel<>(Repository.class);
                        BaseModel[] baseModels = gson.fromJson(response, BaseModel[].class);

                        volleyCallback.onBaseModelLoaded(baseModels);
                    },
                    error -> {
                        String errorFormat = error.getLocalizedMessage();
                        if (errorFormat==null) {
                            errorFormat = user + " might not exist.";
                        }
                        callback.onError(errorFormat);
                    }
            );
            queue.add(stringRequest);
        };
        executor.execute(runnable);
    }*/

}
