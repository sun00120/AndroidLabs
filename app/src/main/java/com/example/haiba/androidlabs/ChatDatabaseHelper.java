package com.example.haiba.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    protected  static String ACTIVITY_NAME = "ChatDatabaseHelper";

    public static final String DATABASE_NAME = "Messages.db";
    public static final  int VERSION_NUM = 9;
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";
    public static final String[] ALL_COLUMNS = new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
    public static final String TABLE_NAME = "messages";
    public static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE_NAME+ "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  KEY_MESSAGE + " TEXT not null);";
    public static final String DATABASE_DROP = "drop table if exists "+ TABLE_NAME +";" ;


    public ChatDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper","Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(DATABASE_DROP);
        onCreate(db);
        Log.i("ChatDatabaseHelper","Calling onUpgrade,oldVer="+oldVersion+"newVersion="+newVersion);
    }




}
