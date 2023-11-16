package com.lithium.fanwall.ShowsListEssentials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ShowsCacheDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shows_cache.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_NAME = "list";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COVER = "cover";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TAG = "tag";
    private static final String COLUMN_DATE_OF_CREATED = "date_of_cache_created";
    private static final String COLUMN_MONTH_OF_CREATED = "month_of_cache_created";
    private static final String COLUMN_DOWNLOADS = "downloads";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_IMDB = "imdb";
    private static final String COLUMN_YEAR_STARTED = "year_started";
    private static final String COLUMN_COLLECTION_TAG = "collection_tag";

    public ShowsCacheDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TAG + " TEXT, " +
                COLUMN_COVER + " TEXT, " +
                COLUMN_DATE_OF_CREATED + " INTEGER, " +
                COLUMN_MONTH_OF_CREATED + " INTEGER, " +
                COLUMN_DOWNLOADS + " INTEGER, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_IMDB + " TEXT, " +
                COLUMN_YEAR_STARTED + " TEXT, " +
                COLUMN_COLLECTION_TAG + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addCache(String title, String tag, String cover,
                         int dateCreated, int monthCreated, int downloads,
                         String genre, String imdb, String yearStarted, String collectionTag) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_TAG, tag);
        contentValues.put(COLUMN_COVER, cover);
        contentValues.put(COLUMN_DATE_OF_CREATED, dateCreated);
        contentValues.put(COLUMN_MONTH_OF_CREATED, monthCreated);
        contentValues.put(COLUMN_DOWNLOADS, downloads);
        contentValues.put(COLUMN_GENRE, genre);
        contentValues.put(COLUMN_IMDB, imdb);
        contentValues.put(COLUMN_YEAR_STARTED, yearStarted);
        contentValues.put(COLUMN_COLLECTION_TAG, collectionTag);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.w("SQLITE", "Something went wrong while saving cache");
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
