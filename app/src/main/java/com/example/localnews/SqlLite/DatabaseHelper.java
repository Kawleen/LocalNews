package com.example.localnews.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "favourite_table";
    public static final String LINK = "LINK";
    public static final String PUBDATE = "PUBDATE";
    public static final String CONETNT = "CONETNT";
    public static final String TITLE = "TITLE";
    public static final String CHECKBOX = "CHECKBOX";



    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LINK TEXT, PUBDATE TEXT, CONETNT TEXT, TITLE TEXT, CHECKBOX TEXT DEFAULT 'uncheck')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String link,String title,String pubDate, String content, String checkBox){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LINK,link);
        contentValues.put(TITLE,title);
        contentValues.put(PUBDATE,pubDate);
        contentValues.put(CONETNT,content);
        contentValues.put(CHECKBOX,checkBox);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select * from "+TABLE_NAME,null);
        return result;
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public void deleteData(String link){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("DELETE FROM "+TABLE_NAME+" WHERE LINK = "+link,null);
    }
}
