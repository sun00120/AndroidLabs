package com.example.apple.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    protected static String ACTIVITY_NAME="ChatDatabaseHelper";
    private static String DATABASE_NAME="Messages.db";
    private static final int VERSION_NUM = 5;

    public static final String KEY_ID="id";
    public static final String KEY_MESSAGE="message";
    public static final String TABLE_NAME="chatTable";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("Create table " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_MESSAGE + " text);");
        Log.i(ACTIVITY_NAME,"Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME +";");
        onCreate(db);
        Log.i(ACTIVITY_NAME,"Calling onUpgrade, oldVersion: " + oldVer + " newVersion: " + newVer);
    }
}
