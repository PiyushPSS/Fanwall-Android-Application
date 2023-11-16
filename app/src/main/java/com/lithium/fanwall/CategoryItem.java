package com.lithium.fanwall;

import com.lithium.fanwall.MainActivityRecHolders.ShowsListModel;

import java.util.List;

public class CategoryItem {

    ShowsListModel showsListModels;

    public CategoryItem(ShowsListModel showsListModels) {
        this.showsListModels = showsListModels;
    }

    public ShowsListModel getShowsListModels() {
        return showsListModels;
    }

    public void setShowsListModels(ShowsListModel showsListModels) {
        this.showsListModels = showsListModels;
    }
}
