package com.example.my_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OrderHistory extends SQLiteOpenHelper {

    public OrderHistory(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Orderhistory ( id INTEGER PRIMARY KEY AUTOINCREMENT, Money TEXT, date TEXT, time TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String price, String data, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Money", price);
        cv.put("date", data);
        cv.put("time", time);

        long result = db.insert("Orderhistory", null, cv);

        return result != -1;

    }


    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("SELECT * FROM Orderhistory",null);

    }


    public boolean isEmpty ()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Orderhistory",null);
        return cursor.getCount() <= 0;
    }
}
