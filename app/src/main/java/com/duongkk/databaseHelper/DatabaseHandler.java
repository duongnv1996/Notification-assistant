package com.duongkk.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.duongkk.models.Application;

/**
 * Created by MyPC on 7/20/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public final static String DB_NAME = "db_notification";
    public final static int VERSION = 1;
    public final static String TABLE_APPLICATION = "t_application";
    public final static String KEY_NAME = "name_app";
    public final static String KEY_PACKAGE = "package_app";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_APPLICATION + " ( " + KEY_PACKAGE + "text PRIMARY KEY , "+ KEY_NAME + " text )";
        db.execSQL(query);
    }

    void addApp(Application app){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,app.getmName());
        values.put(KEY_PACKAGE,app.getmPackage());

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if EXISTS " + TABLE_APPLICATION  );
        onCreate(db);
    }
}
