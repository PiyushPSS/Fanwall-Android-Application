package com.lithium.fanwall.MainActivityRecHolders;

import androidx.annotation.Keep;

@Keep
public class ShowsListModel {

    String cover, title, tag, yearStarted, imdb, genre, description, getCollectionTag;
    int downloads;

    public String getGetCollectionTag() {
        return getCollectionTag;
    }

    public void setGetCollectionTag(String getCollectionTag) {
        this.getCollectionTag = getCollectionTag;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYearStarted() {
        return yearStarted;
    }

    public void setYearStarted(String yearStarted) {
        this.yearStarted = yearStarted;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
