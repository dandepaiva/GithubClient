package com.exaud.githubclient;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exaud.githubclient.models.Commit;

public class CommitRecyclerViewAdapter extends RecyclerView.Adapter<CommitRecyclerViewAdapter.CommitRecyclerViewHolder> {
    private Commit[] commits;

    @NonNull
    @Override
    public CommitRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View commitView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.githubclient_recycler_view, viewGroup, false);
        return new CommitRecyclerViewHolder(commitView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitRecyclerViewAdapter.CommitRecyclerViewHolder commitRecyclerViewHolder, int position) {
        //CommitRecyclerViewHolder.onBind(commitRecyclerViewHolder);
    }

    @Override
    public int getItemCount() {
        return commits.length;
    }

    public static class CommitRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView descriptionView;

        public CommitRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            descriptionView = itemView.findViewById(R.id.description_view);
        }

        void onBind(Commit commit){
            nameView.setText(commit.getAuthor().getName());
            descriptionView.setText(commit.describeContents());
        }
    }
}
