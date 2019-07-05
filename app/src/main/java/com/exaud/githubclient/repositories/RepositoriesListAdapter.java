package com.exaud.githubclient.repositories;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exaud.githubclient.R;
import com.exaud.githubclient.RecyclerViewItemViewModel;
import com.exaud.githubclient.databinding.GithubclientRecyclerViewBinding;
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
        GithubclientRecyclerViewBinding recyclerViewBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        R.layout.githubclient_recycler_view, viewGroup, false);
        return new GithubClientViewHolder(recyclerViewBinding);
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
        public GithubclientRecyclerViewBinding recyclerViewBinding;
        final View layoutClick;


        GithubClientViewHolder(GithubclientRecyclerViewBinding recyclerViewBinding) {
            super(recyclerViewBinding.getRoot());
            this.recyclerViewBinding = recyclerViewBinding;
            layoutClick = itemView.findViewById(R.id.click_layout);
        }

        void onBind(Repository repository) {
            // Log.e("TAG", "onBind: " + repository.getName());
            recyclerViewBinding.setViewmodel(
                    new RecyclerViewItemViewModel(repository.getName(), repository.getDescription()));

            layoutClick.setOnClickListener(v ->
                    RepositorySearchActivity.startCommitListActivity(repository.getUrl()));
        }
    }
}
