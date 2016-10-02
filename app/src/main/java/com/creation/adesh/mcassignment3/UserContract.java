package com.creation.adesh.mcassignment3;

import android.provider.BaseColumns;

/**
 * Created by adesh on 10/1/16.
 */
final class UserContract {
    private UserContract() {}

    public static class User implements BaseColumns {
        public static final String tableName = "User";
        public static final String ColumnName = "Name";
        public static final String ColumnHighScore = "HighScore";
    }


}
