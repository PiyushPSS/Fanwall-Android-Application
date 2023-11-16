package com.lithium.fanwall.MainActivityRecHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lithium.fanwall.AllCategory;
import com.lithium.fanwall.CategoryItem;
import com.lithium.fanwall.R;

import java.util.List;

public class MainRecyclerHolderCustomAdapter extends
        RecyclerView.Adapter<MainRecyclerHolderCustomAdapter.MyViewHolder> {

    Context context;
    List<AllCategory> allCategories;

    public MainRecyclerHolderCustomAdapter(Context context, List<AllCategory> allCategories) {
        this.context = context;
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_vertical_recycler_layout,
                parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.categoryTitle.setText(allCategories.get(position).getCategoryTitle());
        addCategories(holder.Recycler_ShowsHolder, allCategories.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle;
        RecyclerView Recycler_ShowsHolder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            Recycler_ShowsHolder = itemView.findViewById(R.id.Recycler_ShowsHolder);
        }
    }

    private void addCategories(RecyclerView recyclerView, List<CategoryItem> categoryItemList) {
        HorizontalRecDataHolderCustomAdapter customAdapter =
                new HorizontalRecDataHolderCustomAdapter(context, categoryItemList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }
}
