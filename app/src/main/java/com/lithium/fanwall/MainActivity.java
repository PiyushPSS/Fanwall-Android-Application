package com.lithium.fanwall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.lithium.fanwall.MainActivityRecHolders.MainRecyclerHolderCustomAdapter;
import com.lithium.fanwall.MainActivityRecHolders.ShowsListModel;
import com.lithium.fanwall.ShowsListEssentials.ShowsCacheDatabaseHelper;
import com.lithium.fanwall.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ShowsListModel> showsListModels = new ArrayList<>();
    MainRecyclerHolderCustomAdapter customAdapter;
    ActivityMainBinding binding;
    FirebaseFirestore firebaseFirestore;
    List<AllCategory> allCategories = new ArrayList<>();

    ShowsCacheDatabaseHelper databaseHelper;

    private RewardedAd mRewardedAd;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_main, null, false);
        setContentView(view);
        binding = ActivityMainBinding.bind(view);

        binding.progressList.setVisibility(View.GONE);

        databaseHelper = new ShowsCacheDatabaseHelper(this);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(settings);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        customAdapter = new MainRecyclerHolderCustomAdapter(this, allCategories);
        binding.MainRecyclerHolder.setAdapter(customAdapter);
        binding.MainRecyclerHolder.setLayoutManager(manager);

        //Ad Initialize.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                RewardedAdMethod();
            }
        });

        //Populate the list according to the strategy.
        listUpdateStrategy();

        binding.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpOptionsMethod(view);
            }
        });

        binding.supportUsByWatchingAdIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSupportUsWithAdDialog();
            }
        });

    }

    private void setCategories() {


        //Sorted A to Z.
        {
            List<CategoryItem> sortedAToZ = new ArrayList<>();
            for (ShowsListModel singleShow : showsListModels) {
                if (!singleShow.getGenre().contains("UP")) {
                    sortedAToZ.add(new CategoryItem(singleShow));
                }
            }
            allCategories.add(new AllCategory("Sorted A To Z", sortedAToZ));
        }

        // UPCOMING SHOWS.
        {
            List<CategoryItem> upcomingShows = new ArrayList<>();
            for (ShowsListModel singleShow : showsListModels) {
                if (singleShow.getGenre().contains("UP")) {
                    upcomingShows.add(new CategoryItem(singleShow));
                }
            }
            if (!upcomingShows.isEmpty()) {
                allCategories.add(new AllCategory("Upcoming Series", upcomingShows));
            }
        }

        //Action TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Action")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Action Tv Shows", categoryItemList));
        }


        //Drama TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Drama")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Drama Shows", categoryItemList));
        }


        //Mystery TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Mystery")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Mystery Series", categoryItemList));
        }

        //Thriller TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Thriller")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Binge-Worthy Thriller Shows", categoryItemList));
        }


        //Anime TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Anime")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("All Favourite Anime", categoryItemList));
        }


        //Crime TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Crime")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Crime Tv Shows", categoryItemList));
        }


        //Family TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Family")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Family-Friendly Shows", categoryItemList));
        }


        //Comedy TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Comedy")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Comedy Shows", categoryItemList));
        }


        //Romance TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Romance")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Heartwarming Romance Shows", categoryItemList));
        }


        //History TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("History")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("History Tv Series", categoryItemList));
        }


        //Fantasy TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Fantasy")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Fantasy", categoryItemList));
        }


        //Adventure TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Adventure")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Adventure Shows", categoryItemList));
        }


        //Horror TV Shows.
        {
            List<CategoryItem> categoryItemList = new ArrayList<>();
            for (ShowsListModel showsListModel : showsListModels) {
                if (showsListModel.getGenre().contains("Horror")) {
                    categoryItemList.add(new CategoryItem(showsListModel));
                }
            }
            allCategories.add(new AllCategory("Horror Tv Series", categoryItemList));
        }

    }

    private void openSupportUsWithAdDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(true)
                .setTitle("Support Us")
                .setMessage("You can support us by watching an ad. This would mean a lot to us and will give us motivation to generate beautiful content. Thank You!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Watch Ad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showRewardedAd();
                    }
                })
                .create();

        dialog.show();
    }

    private void RewardedAdMethod() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-6078310243369312/6214680360",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                                RewardedAdMethod();
                            }
                        });
                    }
                });
    }

    private void showRewardedAd() {
        if (mRewardedAd != null) {
            Activity activityContext = MainActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    private void popUpOptionsMethod(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.main_activity_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.requestShow:
                        requestAShow();
                        break;
                    case R.id.supportUs:

                        supportUsMethod();
                        break;
                    case R.id.help:

                        helpdeskMethod();
                        break;
                    case R.id.checkForUpdates:

                        checkForUpdateMethod();
                        break;
                    case R.id.InfoDetails:

                        startActivity(new Intent(MainActivity.this, DevInfoActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    private void requestAShow() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + getString(R.string.dev_mail))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "REQUEST A SHOW");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void listUpdateStrategy() {
        //Check if the data should be retrieved from cache or firebase.
        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);

        Cursor cursor = databaseHelper.readAllData();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            int dateFromDatabase = cursor.getInt(4);
            int monthFromDatabase = cursor.getInt(5);

            if (currentDate == dateFromDatabase) {

                if (currentMonth == monthFromDatabase) {
                    //Same day, same month,
                    Log.w(TAG, "Get Cached Data");
                    getDataFromCache();
                } else {
                    //Same day, different month.
                    Log.w(TAG, "Get New Data");
                    getShowsListFromFirebase();
                }

            } else {
                //Different Day.
                Log.w(TAG, "Get New Data");
                getShowsListFromFirebase();
            }

        } else {
            Log.w(TAG, "Get New Data");
            getShowsListFromFirebase();
        }
    }

    private void supportUsMethod() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Support Us")
                .setMessage(getString(R.string.SupportUsMessage))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Go to Play Store", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPlayStore();
                    }
                })
                .create();

        dialog.show();

    }

    private void helpdeskMethod() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Help & Feedback")
                .setMessage(getString(R.string.HelpAndFeedbackMessage))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Go to Play Store", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPlayStore();
                    }
                })
                .create();

        dialog.show();

    }

    private void checkForUpdateMethod() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking for Updates");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        firebaseFirestore.collection("CurrentVersion")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String versionFromServer = snapshot.getString("version");

                        if (versionFromServer != null) {

                            progressDialog.dismiss();

                            if (versionFromServer.equals(getCurrentVersionName())) {
                                dialog.setMessage("You are up to date.");
                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

                                dialog.show();

                            } else {

                                dialog.setTitle("Update Available!");
                                dialog.setMessage("A newer version is available. For experiencing" +
                                        " better performance please update your application.");
                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Update Now",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                openPlayStore();
                                            }
                                        });
                                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Remind me later",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                dialog.show();
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        dialog.setTitle("Oops!");
                        dialog.setMessage("Something went wrong. Try again after some time.");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        dialog.show();
                    }
                });
    }

    private String getCurrentVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                    + appPackageName)));
        }
    }

    private void getShowsListFromFirebase() {

        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);

        if (databaseHelper.readAllData().getCount() > 0) {
            databaseHelper.deleteAll();
        }

        binding.progressList.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("ShowsList")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            ShowsListModel singularItem = snapshot.toObject(ShowsListModel.class);

                            if (singularItem != null) {
                                databaseHelper.addCache(singularItem.getTitle(),
                                        singularItem.getTag(), singularItem.getCover(),
                                        currentDate, currentMonth, singularItem.getDownloads(),
                                        singularItem.getGenre(), singularItem.getImdb(),
                                        singularItem.getYearStarted(), snapshot.getId());
                            }
                        }

                        //Add the UPCOMING SHOWS SECTION
                        firebaseFirestore.collection("UpcomingShows")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                            ShowsListModel upcomingShows = snapshot.toObject(ShowsListModel.class);

                                            if (upcomingShows != null) {
                                                databaseHelper.addCache(upcomingShows.getTitle(),
                                                        upcomingShows.getTag(),
                                                        upcomingShows.getCover(),
                                                        currentDate, currentMonth,
                                                        upcomingShows.getDownloads(), upcomingShows.getGenre(),
                                                        upcomingShows.getImdb(), upcomingShows.getYearStarted(),
                                                        upcomingShows.getGetCollectionTag());
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "UPCOMING SHOWS CAN'T BE ADDED");
                                    }
                                });
                        getDataFromCache();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressList.setVisibility(View.GONE);
                        Log.w(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDataFromCache() {

        showsListModels.clear();
        Cursor cursor = databaseHelper.readAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ShowsListModel singularData = new ShowsListModel();
                singularData.setTitle(cursor.getString(1));
                singularData.setTag(cursor.getString(2));
                singularData.setCover(cursor.getString(3));
                singularData.setDownloads(cursor.getInt(6));
                singularData.setGenre(cursor.getString(7));
                singularData.setImdb(cursor.getString(8));
                singularData.setYearStarted(cursor.getString(9));
                singularData.setGetCollectionTag(cursor.getString(10));

                showsListModels.add(singularData);
            }
        }

        setCategories();
        customAdapter.notifyDataSetChanged();
        binding.progressList.setVisibility(View.GONE);

    }
}