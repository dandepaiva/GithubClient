package com.exaud.githubclient.commits;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.exaud.githubclient.GithubRepository;
import com.exaud.githubclient.models.Commit;

import java.util.List;

public class CommitViewModel extends ViewModel {
    List<Commit> currentList;
    List<Commit> previousList;
    List<Commit> nextList;
    private String url;
    private int page;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    void setCurrentList(List<Commit> currentList) {
        this.currentList = currentList;
    }

    void setNextList(List<Commit> nextList) {
        this.nextList = nextList;
    }

    public void setPreviousList(List<Commit> previousList) {
        this.previousList = previousList;
    }

    /*TODO*/
    List<Commit> onOpenActivity(){

        return currentList;
    }

    List<Commit> onPressNext(ErrorListener errorListener) {
        if (!nextList.isEmpty()) {
            previousList = currentList;
            currentList = nextList;
            page++;
            GithubRepository.getInstance().loadCommits(url, page + 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    nextList = commitList;
                }

                @Override
                public void onError(String message) {
                }
            });
        } else {
            errorListener.onError("This is the last page!");
        }

        return currentList;
    }

    List<Commit> onPressPrevious(ErrorListener errorListener) {
        if (!previousList.isEmpty()) {
            nextList = currentList;
            currentList = previousList;
            page--;
            GithubRepository.getInstance().loadCommits(url, page - 1, new GithubRepository.CommitCallback() {
                @Override
                public void showCommits(List<Commit> commitList) {
                    previousList = commitList;
                }

                @Override
                public void onError(String message) {
                }
            });
        } else {
            errorListener.onError("This is the first page!");
        }

        return currentList;
    }








    interface ErrorListener {
        void onError(String errorMessage);
    }
}
