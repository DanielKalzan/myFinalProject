package com.example.my_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class OrdersDB extends SQLiteOpenHelper {

    public OrdersDB(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Orders( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT , price TEXT, unit TEXT, image BLOG);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertOrdersData(String name, String price, String unit, byte[] image){


        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("unit", unit);
        cv.put("image",image);

        long result = db.insert( "Orders", null , cv);

        return result != -1;

    }

    public boolean itemExists(String itemName){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from Orders where name=?", new String[] {itemName});

        return cursor.getCount() > 0;

    }

    public Cursor getData(String sql){

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(sql,null);
    }



    public void deleteProduct (String id)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL (" DELETE FROM Orders WHERE  id=? ",new String[]{id} );
    }


}
