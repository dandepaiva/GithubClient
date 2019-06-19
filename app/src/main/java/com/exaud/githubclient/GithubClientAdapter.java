package com.exaud.githubclient;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exaud.githubclient.models.Repository;

import java.util.ArrayList;

public class GithubClientAdapter extends RecyclerView.Adapter<GithubClientAdapter.GithubClientViewHolder> {
    private ArrayList<Repository> repositoryArrayList;

    public GithubClientAdapter() {
        this.repositoryArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public GithubClientAdapter.GithubClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View nodeView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.githubclient_recycler_view, viewGroup, false);
        return new GithubClientViewHolder(nodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubClientAdapter.GithubClientViewHolder githubClientViewHolder, int position) {
        githubClientViewHolder.onBind(repositoryArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (repositoryArrayList != null) {
            return repositoryArrayList.size();
        } else {
            return 0;
        }
    }

    void updateDataNodeArrayList(ArrayList<Repository> repositoryArrayList){
        this.repositoryArrayList = repositoryArrayList;
        notifyDataSetChanged();
    }

    public static class GithubClientViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        TextView descriptionView;

        public GithubClientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            descriptionView = itemView.findViewById(R.id.description_view);
        }

        void onBind(Repository repository){
            nameView.setText(repository.getName());
            descriptionView.setText(repository.getDescription());
        }
    }
}
