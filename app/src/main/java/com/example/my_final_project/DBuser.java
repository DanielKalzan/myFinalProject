package com.example.my_final_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

public class DBuser extends SQLiteOpenHelper {
    public DBuser(@Nullable Context context) {
        super(context, "userData.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS DATA(username TEXT PRIMARY KEY , phoneNum TEXT, password TEXT, userImage BLOG)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String name, String Num, String password, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("username", name);
        cv.put("phoneNum", Num);
        cv.put("password", password);
        cv.put("userImage", image);

        long result = db.insert("DATA", null, cv);
        return result != -1;
    }

    public boolean ChecksInformation(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from DATA where username=? and password=?", new String[]{name, password});
        return cursor.getCount() > 0;


    }

    public boolean IfUsernameExit(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from DATA where username=? ", new String[]{name});
        return cursor.getCount() > 0;


    }

    public Bitmap getImage(String username) {

        SQLiteDatabase db = getReadableDatabase();
        Bitmap bt = null;

        Cursor cursor = db.rawQuery("select * from DATA where username=? ", new String[]{username});
        if (cursor.moveToNext()) {

            byte[] image = cursor.getBlob(3);
            bt = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        return bt;
    }

    public String getPhoneNum(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String num = null;

        Cursor cursor = db.rawQuery("select * from DATA where username=? ", new String[]{name});
        if (cursor.moveToNext()) {

            num = cursor.getString(1);

        }

        return num;


    }

    public String getPassword(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String pass = null;

        Cursor cursor = db.rawQuery("select * from DATA where username=? ", new String[]{name});

        if (cursor.moveToNext()) {
            pass = cursor.getString(2);
        }
        return pass;
    }


    public boolean updateProfile(String oldName, String email, String password, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("phoneNum", email);
        cv.put("password", password);
        cv.put("userImage", image);

        long result = db.update("DATA", cv, "username=?", new String[]{oldName});
        return result != -1;

    }



    public boolean userExit(String name, String Num){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from DATA where username=? and phoneNum=?", new String[]{name, Num});
        return cursor.getCount() > 0;

    }

    public boolean updatePassword(String name, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("password", password);

        long result = db.update("DATA", cv,"username=?",new String[]{name});
        return result != -1;

    }


}
