package com.exaud.githubclient;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exaud.githubclient.models.Repository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class GithubRepository {
    Executor executor = Executors.newFixedThreadPool(3);
    //private Set<RepositoryCallback> callbacks = new HashSet<>();

    private GithubRepository(){}
    static GithubRepository getInstance(){return Singleton.INSTANCE;}

    void loadDataNodes(RepositoryCallback callback, String user){
        //addToSet(callback);

        RequestQueue queue = Volley.newRequestQueue(GithubClientApplication.getContext());
        String url = "https://api.github.com/users/%1$s/repos";

        Runnable runnable = () -> {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, String.format(url,user),
                    response -> {
                        Type repositoryType = new TypeToken<ArrayList<Repository>>(){}.getType();
                        Gson gson = new Gson();
                        ArrayList<Repository> repositoryArrayList = gson.fromJson(response, repositoryType);
                        callback.showDataNodes(repositoryArrayList);
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

            //String dataNodeFile = loadAsset(user);


        };

        executor.execute(runnable);
    }

    /*
    void addToSet(RepositoryCallback dataNodeShower){
        callbacks.add(dataNodeShower);
    }*/
    /*
    void removeFromSet(RepositoryCallback showDataNode){
        showDataNodeList.remove(showDataNode);
    }*/



    /**
     * Read a file containing JSON data
     * @param file the file to be read
     * @return String with the content of file
     */
    private String loadAsset(String file) {
        String asset = null;
        try(InputStream inputStream
                    = GithubClientApplication.getContext().getAssets().open(file)){
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            asset = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return asset;
    }

    public interface RepositoryCallback {
        void showDataNodes(ArrayList<Repository> repositories);
        void onError(String message);
    }


    private static class Singleton{
        private static final GithubRepository INSTANCE = new GithubRepository();
    }
}
