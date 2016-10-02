package com.creation.adesh.mcassignment3;

import android.provider.BaseColumns;

/**
 * Created by adesh on 10/1/16.
 */
public final class UserContract {
    private UserContract() {}

    public static class User implements BaseColumns {
        public static String tableName = "User";
        public static String ColumnName = "Name";
        public static String ColumnHighScore = "HighScore";
    }


}
