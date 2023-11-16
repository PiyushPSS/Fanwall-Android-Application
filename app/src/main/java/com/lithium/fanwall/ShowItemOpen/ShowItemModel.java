package com.lithium.fanwall.ShowItemOpen;

import androidx.annotation.Keep;

@Keep
public class ShowItemModel {

    String imageUrl, id;
    int downloads;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }
}
