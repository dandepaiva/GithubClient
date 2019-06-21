package com.exaud.githubclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exaud.githubclient.models.Repository;

import java.util.ArrayList;
import java.util.List;

public class GithubClientAdapter extends RecyclerView.Adapter<GithubClientAdapter.GithubClientViewHolder> {
    private List<Repository> repositoryList;

    public GithubClientAdapter() {
        this.repositoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public GithubClientAdapter.GithubClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View nodeView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.githubclient_recycler_view, viewGroup, false);
        return new GithubClientViewHolder(nodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull GithubClientAdapter.GithubClientViewHolder githubClientViewHolder, int position) {
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

    void updateDataNodeArrayList(List<Repository> repositoryList){
        this.repositoryList = repositoryList;
        notifyDataSetChanged();
    }

    public static class GithubClientViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        TextView descriptionView;
        View layoutClick;

        public GithubClientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            descriptionView = itemView.findViewById(R.id.description_view);
            layoutClick = itemView.findViewById(R.id.click_layout);
        }

        void onBind(Repository repository){
            nameView.setText(repository.getName());
            descriptionView.setText(repository.getDescription());

            layoutClick.setOnClickListener(v -> {
                Intent commitListActivity = new Intent(GithubClientApplication.getContext(), CommitListActivity.class);
                // commitListActivity.putParcelableArrayListExtra(GithubClientActivity.COMMITLIST, commitList);
                GithubClientApplication.getContext().startActivity(commitListActivity);
                // GithubRepository.getInstance().loadCommits(repository);
            });
        }
    }
}
