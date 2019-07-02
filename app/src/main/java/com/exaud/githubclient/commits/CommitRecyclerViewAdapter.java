package com.exaud.githubclient.commits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exaud.githubclient.R;
import com.exaud.githubclient.models.Commit;

import java.util.ArrayList;
import java.util.List;

public class CommitRecyclerViewAdapter extends RecyclerView.Adapter<CommitRecyclerViewAdapter.CommitRecyclerViewHolder> {
    private List<Commit> commits;

    public CommitRecyclerViewAdapter() {
        this.commits = new ArrayList<>();
    }

    @NonNull
    @Override
    public CommitRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View commitView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.githubclient_recycler_view, viewGroup, false);
        return new CommitRecyclerViewHolder(commitView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitRecyclerViewAdapter.CommitRecyclerViewHolder commitRecyclerViewHolder, int position) {
        commitRecyclerViewHolder.onBind(commits.get(position));
    }

    @Override
    public int getItemCount() {
        if (commits != null) {
            return this.commits.size();
        } else {
            return 0;
        }
    }

    void updateCommitArray(List<Commit> commits) {
        this.commits = commits;
        notifyDataSetChanged();
    }

    public static class CommitRecyclerViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        final TextView messageView;

        CommitRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            messageView = itemView.findViewById(R.id.message_view);
        }

        void onBind(Commit commit) {
            nameView.setText(commit.getAuthor().getName().replace("Ã©", "é"));
            messageView.setText(commit.getMessage());
        }
    }
}
