package com.lithium.fanwall.MainActivityRecHolders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lithium.fanwall.CategoryItem;
import com.lithium.fanwall.R;
import com.lithium.fanwall.ShowItemOpen.ShowItemOpenActivity;

import java.util.List;

public class HorizontalRecDataHolderCustomAdapter extends
        RecyclerView.Adapter<HorizontalRecDataHolderCustomAdapter.MyViewHolder> {

    Context context;
    List<CategoryItem> categoryItem;

    public HorizontalRecDataHolderCustomAdapter(Context context, List<CategoryItem> categoryItem) {
        this.context = context;
        this.categoryItem = categoryItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_recycler_list_holder,
                viewGroup, false);

        return new HorizontalRecDataHolderCustomAdapter.MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        ShowsListModel singleModel = categoryItem.get(position).getShowsListModels();

        myViewHolder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(singleModel.getCover())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        myViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(myViewHolder.categoryItemImage);

        myViewHolder.showName.setText(singleModel.getTitle());
        myViewHolder.showYearStartedAndIMDB.setText(singleModel.getYearStarted() + " | " + singleModel.getImdb()
                + " ⭐ ");

        myViewHolder.categoryItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!singleModel.getGenre().equals("UP")) {
                    Intent intent = new Intent(context, ShowItemOpenActivity.class);
                    intent.putExtra("tag", singleModel.getTag());
                    intent.putExtra("title", singleModel.getTitle());
                    intent.putExtra("collectionID", singleModel.getGetCollectionTag());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "COMING SOON :)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryItemImage;
        ProgressBar progressBar;
        TextView showName, showYearStartedAndIMDB;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressLoadShow);
            categoryItemImage = itemView.findViewById(R.id.categoryItemImage);
            showName = itemView.findViewById(R.id.showName);
            showYearStartedAndIMDB = itemView.findViewById(R.id.showYearStartedAndIMDB);
        }
    }
}
