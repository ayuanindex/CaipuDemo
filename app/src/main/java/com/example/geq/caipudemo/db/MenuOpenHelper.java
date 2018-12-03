package com.example.geq.caipudemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MenuOpenHelper extends SQLiteOpenHelper {

    public MenuOpenHelper(Context context) {
        super(context, MenuConstants.DB_NAME, null, MenuConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MenuConstants.DB_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
