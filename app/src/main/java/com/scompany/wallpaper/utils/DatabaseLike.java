package com.scompany.wallpaper.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scompany.wallpaper.model.ImageFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/1/2018.
 */

public class DatabaseLike extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Wall_paper";
    private static final String TABLE_NAME = "Like1";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SRC = "src";

    public DatabaseLike(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT, " + KEY_SRC + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addImage(ImageFavorite imageFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, imageFavorite.getName());
        values.put(KEY_SRC, imageFavorite.getSrc());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ImageFavorite getLikeByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,
                        KEY_NAME, KEY_SRC}, KEY_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
        }
        ImageFavorite imageFavorite = new ImageFavorite(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return imageFavorite;
    }

    public void deleteImage(ImageFavorite imageFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(imageFavorite.getId())});
        db.close();
    }

    public List<ImageFavorite> getAllImage() {
        List<ImageFavorite> contactList = new ArrayList<ImageFavorite>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ImageFavorite style = new ImageFavorite();
                style.setId(Integer.parseInt(cursor.getString(0)));
                style.setName(cursor.getString(1));
                style.setSrc(cursor.getString(2));
                // Adding contact to list
                contactList.add(style);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

}
