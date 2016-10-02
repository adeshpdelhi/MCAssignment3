package com.creation.adesh.mcassignment3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.creation.adesh.mcassignment3.UserContract.User;

/**
 * Created by adesh on 10/1/16.
 */
class UserDbHelper extends SQLiteOpenHelper{
    private static final int DatabaseVersion = 4;
    private static final String DatabaseName = "User.db";
    private final String createDbEntries = "create table "+ User.tableName+ "( "+
            User._ID+" integer primary key, "+
            User.ColumnName+" text, "+ User.ColumnHighScore+" integer);";

    private final String deleteDbEntries = "drop table if exists "+User.tableName+" ;";

    public UserDbHelper(Context context) {
        super(context,DatabaseName,null ,DatabaseVersion);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.v("Helper",createDbEntries);
        db.execSQL(createDbEntries);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteDbEntries);
        onCreate(db);
    }


}
