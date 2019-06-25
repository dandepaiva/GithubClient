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
    int currentPage = 1;
    //private Set<RepositoryCallback> callbacks = new HashSet<>();

    private GithubRepository() {
    }

    static GithubRepository getInstance() {
        return Singleton.INSTANCE;
    }

    void loadDataNodes(RepositoryCallback callback, String user) {
        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        String url = "https://api.github.com/users/%1$s/repos?page=%2$d";
        this.callback = callback;
        this.user = user;

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


    void loadCommits(String url, CommitCallback commitCallback) {
        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        Runnable runnable = () -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"/commits",
                    response -> {
                        Gson gson = new Gson();

                        CommitParent[] commits = gson.fromJson(response, CommitParent[].class);

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
                    }
            );
            queue.add(stringRequest);
        };
        executor.execute(runnable);


    }

    void nextPage(){
        currentPage++;
        loadDataNodes(callback, user);
    }

    void previousPage(){
        if(currentPage > 1) {
            currentPage--;
            loadDataNodes(callback, user);
        }
    }


    public interface RepositoryCallback {
        void showDataNodes(List<Repository> repositories);
        void onError(String message);
    }

    public interface CommitCallback{
        void showCommit(ArrayList<Commit> commitList);
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
