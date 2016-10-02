package com.creation.adesh.mcassignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.creation.adesh.mcassignment3.AddUserFragment;
import com.creation.adesh.mcassignment3.UserContract;
import com.creation.adesh.mcassignment3.UserDbHelper;
import com.creation.adesh.mcassignment3.ViewLogsActivity;
import com.creation.adesh.mcassignment3.User;
import com.creation.adesh.mcassignment3.Question;
import com.creation.adesh.mcassignment3.SwitchUserFragment;
import com.creation.adesh.mcassignment3.UpdateProgressTask;

/**
 * Created by adesh on 10/2/16.
 */
public class UpdateProgressTask extends AsyncTask<User,Void,Void > {
    Context context;
    public UpdateProgressTask(Context context){
        this.context = context;
    }
    @Override
        protected Void doInBackground(User... users){
            User user = users[0];
            UserDbHelper mUserDbHelper = new UserDbHelper(context);
            SQLiteDatabase userDb = mUserDbHelper.getReadableDatabase();
            ContentValues newUserValues = new ContentValues();
            newUserValues.put(UserContract.User.ColumnHighScore,user.getHighScore());
            String columns = UserContract.User._ID+" =?";
            String args[]= {user._ID.toString()};
            int count = userDb.update(
            UserContract.User.tableName,
            newUserValues,
            columns, args);
            if(count!=1)
                Log.e("UpdateProgressTask","More than one row updated!");
        return null;
        }
}
