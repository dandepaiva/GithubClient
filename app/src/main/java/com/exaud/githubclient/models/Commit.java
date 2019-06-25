package com.exaud.githubclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Commit extends BaseModel {
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("committer")
    @Expose
    private Committer committer;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;

    protected Commit(Parcel in) {
        message = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            commentCount = null;
        } else {
            commentCount = in.readInt();
        }
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Committer getCommitter() {
        return committer;
    }

    public void setCommitter(Committer committer) {
        this.committer = committer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}

