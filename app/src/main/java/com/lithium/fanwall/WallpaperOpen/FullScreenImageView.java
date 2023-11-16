package com.lithium.fanwall.WallpaperOpen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lithium.fanwall.R;
import com.lithium.fanwall.databinding.ImageFullScreenBinding;

public class FullScreenImageView extends AppCompatActivity {

    ImageFullScreenBinding binding;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.image_full_screen, null);
        setContentView(view);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");

        binding = ImageFullScreenBinding.bind(view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adViewFullScreen_Banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        binding.goBackFullImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Glide.with(this)
                .load(imageUrl)
                .into(binding.fullImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}