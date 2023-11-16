package com.lithium.fanwall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lithium.fanwall.databinding.ActivityDevInfoBinding;

public class DevInfoActivity extends AppCompatActivity {

    ActivityDevInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_dev_info, null);
        setContentView(view);

        binding = ActivityDevInfoBinding.bind(view);

        binding.goBackInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (currentVersionName() != null) {
            binding.currentVersion.setText(currentVersionName());
        }

        binding.openEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + getString(R.string.dev_mail))); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Query About Application");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private String currentVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}