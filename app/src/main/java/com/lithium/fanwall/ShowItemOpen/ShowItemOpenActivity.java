package com.lithium.fanwall.ShowItemOpen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lithium.fanwall.R;
import com.lithium.fanwall.databinding.ActivityShowOpenBinding;

import java.util.ArrayList;
import java.util.List;

public class ShowItemOpenActivity extends AppCompatActivity {

    List<ShowItemModel> showItemModels = new ArrayList<>();
    ShowOpenCustomAdapter customAdapter;
    ActivityShowOpenBinding binding;
    FirebaseFirestore firebaseFirestore;

    String title, tag, collectionID;
    private static final String TAG = "ShowItemOpenActivity";
    int scrolledViews, currentViews, totalViews;
    boolean isScrolling = false;
    List<DocumentSnapshot> snapshotList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_show_open, null);
        setContentView(view);
        binding = ActivityShowOpenBinding.bind(view);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);

        Intent getData = getIntent();
        title = getData.getStringExtra("title");
        tag = getData.getStringExtra("tag");
        collectionID = getData.getStringExtra("collectionID");

        binding.showOpenTitle.setText(title);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        customAdapter = new ShowOpenCustomAdapter(this, showItemModels, title, tag, collectionID);
        binding.showOpenRecyclerView.setAdapter(customAdapter);
        binding.showOpenRecyclerView.setLayoutManager(manager);

        getDataFromFirebase("", true);

        binding.goBackShowOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.showOpenRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentViews = manager.getChildCount();
                scrolledViews = manager.findFirstVisibleItemPosition();
                totalViews = manager.getItemCount();

                if (isScrolling && ((currentViews + scrolledViews) == totalViews)) {
                    if (totalViews % 20 == 0) {
                        getDataFromFirebase(snapshotList.get(totalViews - 1), false);
                    } else {
                        binding.progressShowOpen.setVisibility(View.GONE);
                    }
                    isScrolling = false;
                }
            }
        });
    }

    private void getDataFromFirebase(Object object, boolean isNew) {

        Query selectedData;

        if (isNew) {

            selectedData = firebaseFirestore.collection("walldata")
                    .document("Shows")
                    .collection(tag)
                    .orderBy("downloads", Query.Direction.DESCENDING)
                    .limit(20);

        } else {
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) object;

            selectedData = firebaseFirestore.collection("walldata")
                    .document("Shows")
                    .collection(tag)
                    .orderBy("downloads", Query.Direction.DESCENDING)
                    .limit(20)
                    .startAfter(documentSnapshot);
        }

        selectedData.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (isNew) {
                            showItemModels.clear();
                        }

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                            ShowItemModel singularItem = snapshot.toObject(ShowItemModel.class);
                            singularItem.setId(snapshot.getId());
                            showItemModels.add(singularItem);
                            snapshotList.add(snapshot);
                        }
                        customAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.w(TAG, e.getMessage());
                        Toast.makeText(ShowItemOpenActivity.this, e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}