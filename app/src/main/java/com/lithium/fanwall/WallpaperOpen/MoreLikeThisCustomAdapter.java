package com.lithium.fanwall.WallpaperOpen;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lithium.fanwall.R;
import com.lithium.fanwall.ShowItemOpen.ShowItemModel;
import com.lithium.fanwall.databinding.MoreLikeThisItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class MoreLikeThisCustomAdapter extends RecyclerView.Adapter<MoreLikeThisCustomAdapter.MyViewHolder> {

    Activity activity;
    List<ShowItemModel> showItemModels;
    List<ShowItemModel> selectedList;
    String title, tag, collectionID;

    ArrayList<String> imageUrlArray = new ArrayList<>();
    ArrayList<Integer> downloadsArray = new ArrayList<>();
    ArrayList<String> idArray = new ArrayList<>();

    public MoreLikeThisCustomAdapter(Activity activity, List<ShowItemModel> showItemModels,
                                     List<ShowItemModel> selectedList, String title, String tag, String collectionID) {
        this.activity = activity;
        this.showItemModels = showItemModels;
        this.selectedList = selectedList;
        this.title = title;
        this.tag = tag;
        this.collectionID = collectionID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.more_like_this_item_layout, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ShowItemModel singleItem = selectedList.get(position);

        holder.binding.downloadsCount.setText(String.valueOf(singleItem.getDownloads()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
        Glide.with(activity)
                .load(singleItem.getImageUrl())
                .apply(requestOptions)
                .into(holder.binding.showImageMoreLikeThis);

        holder.binding.moreLikeThisRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadsArray.clear();
                idArray.clear();
                imageUrlArray.clear();
                for (int i = 0; i < showItemModels.size(); i++) {
                    imageUrlArray.add(showItemModels.get(i).getImageUrl());
                    downloadsArray.add(showItemModels.get(i).getDownloads());
                    idArray.add(showItemModels.get(i).getId());
                }

                Intent openImage = new Intent(activity, WallpaperOpenActivity.class);
                openImage.putExtra("imageUrl", singleItem.getImageUrl());
                openImage.putExtra("downloads", singleItem.getDownloads());
                openImage.putExtra("title", title);
                openImage.putExtra("id", singleItem.getId());
                openImage.putExtra("tag", tag);
                openImage.putExtra("collectionID", collectionID);
                openImage.putStringArrayListExtra("idArray", idArray);
                openImage.putIntegerArrayListExtra("downloadsArray", downloadsArray);
                openImage.putStringArrayListExtra("imageUrlArray", imageUrlArray);
                activity.finish();
                activity.startActivity(openImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        MoreLikeThisItemLayoutBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = MoreLikeThisItemLayoutBinding.bind(itemView);
        }
    }
}
