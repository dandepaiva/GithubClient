package com.exaud.githubclient.repositories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Repository;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesListAdapter extends RecyclerView.Adapter<RepositoriesListAdapter.GithubClientViewHolder> {
    private List<Repository> repositoryList;

    public RepositoriesListAdapter() {
        this.repositoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RepositoriesListAdapter.GithubClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View nodeView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.githubclient_recycler_view, viewGroup, false);
        return new GithubClientViewHolder(nodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoriesListAdapter.GithubClientViewHolder githubClientViewHolder, int position) {
        githubClientViewHolder.onBind(repositoryList.get(position));
    }

    @Override
    public int getItemCount() {
        if (repositoryList != null) {
            return repositoryList.size();
        } else {
            return 0;
        }
    }

    void updateDataNodeArrayList(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
        notifyDataSetChanged();
    }

    public static class GithubClientViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        final TextView descriptionView;
        final View layoutClick;

        GithubClientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            descriptionView = itemView.findViewById(R.id.message_view);
            layoutClick = itemView.findViewById(R.id.click_layout);
        }

        void onBind(Repository repository) {
            nameView.setText(repository.getName());
            descriptionView.setText(repository.getDescription());

            layoutClick.setOnClickListener(v ->
                    RepositorySearchActivity.startCommitListActivity(repository.getUrl()));
        }
    }
}
