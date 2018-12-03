package com.example.geq.caipudemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeOpenhelp extends SQLiteOpenHelper {

    public RecipeOpenhelp(Context context) {
        super(context, "recipe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表types（菜谱分类）
        db.execSQL("create table types (typeid integer PRIMARY KEY AUTOINCREMENT NOT NULL,typename varchar(100),description varchar(100),typeic varchar(100));");
        //创建表collect(收藏的菜品详情)
        db.execSQL("create table collect (menuid integer PRIMARY KEY AUTOINCREMENT NOT NULL,typeid intger NOT NULL,spic varchar(100),assistmaterial varchar(100),notlikes varchar(20),menuname varchar(20),abstracts varchar(100),mainmaterial varchar(100),likes varchar(10));");
        //创建表collect(收藏的菜品做法步骤)
        db.execSQL("create table step (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,menuid integer,stepid varchar(100),description varchar(100),pic varchar(100));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
