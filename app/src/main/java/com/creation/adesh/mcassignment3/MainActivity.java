package com.creation.adesh.mcassignment3;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

//Reference: developer.android.com

public class MainActivity extends AppCompatActivity implements AddUserFragment.AddUser,SwitchUserFragment.SwitchUserListener{
    private static final String RCheated = "CHEATED";
    private static final String RHinted = "HINT";
    private static final String RAnswer = "ANSWER";
    private static final String RFirstAttempt = "FIRSTATTEMPT";
    private static final String RScore = "SCORE";
    private static final String logfileName = "quizlogs.txt";
    private static final String R_Id = "_ID";
    private static final String LOG_TAG = "MainActivity";
    private Long _ID = Long.valueOf(0);
    private User user = null;
    private UserDbHelper mUserDbHelper = null;
    private static TextView mQuestionText = null;
    private static TextView mScore = null;
    private static TextView mUsername = null;
    private static TextView mHighScore = null;
    private Integer number = null;
    private Question presentQuestion = null;
    private static final int cheatRequestCode = 0;
    private static final int hintRequestCode = 1;
    private Boolean mCheated = false;
    private Boolean mHintShown = false;
    private Boolean firstAttempt = true;
    private Integer score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuestionText = (TextView) findViewById(R.id.questionText);
        mScore = (TextView) findViewById(R.id.score);
        mUsername = (TextView) findViewById(R.id.username);
        mHighScore = (TextView) findViewById(R.id.highscore);
        mUserDbHelper = new UserDbHelper(getApplicationContext());
        SharedPreferences sharedPref = this.getSharedPreferences("score", Context.MODE_PRIVATE);

        _ID = sharedPref.getLong(R_Id,0);
        if(_ID!=0)
            updateUserFromId();
        else
            addUser("default");
        score = sharedPref.getInt(RScore,0);
        mScore.setText(score.toString());
        if(savedInstanceState == null) {
            presentQuestion= new Question();
            number = presentQuestion.getNumber();
            score=0;
        }
        else
        {
            number = savedInstanceState.getInt("number");
            presentQuestion = new Question(number);
            mCheated = savedInstanceState.getBoolean(RCheated, false);
            mHintShown = savedInstanceState.getBoolean(RHinted, false);
            firstAttempt = savedInstanceState.getBoolean(RFirstAttempt, true);
            score = savedInstanceState.getInt(RScore, 0);
        }
        Log.v(LOG_TAG,"Score is "+number.toString());
        Log.v(LOG_TAG,mQuestionText.toString());
        mQuestionText.setText(getString(R.string.questionText,number));
        addToLog();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.viewprivatelogs:
                startActivity(new Intent(this, ViewLogsActivity.class).putExtra("type","private"));
                return true;
            case R.id.viewpubliclogs:
                startActivity(new Intent(this, ViewLogsActivity.class).putExtra("type","public"));
                return true;
            case R.id.adduser:
                FragmentManager fragMan = getSupportFragmentManager();
                DialogFragment fragment = new AddUserFragment();
                fragment.show(fragMan,"Add a new user");
                return true;
            case R.id.switchuser:
                fragMan = getSupportFragmentManager();
                fragment = new SwitchUserFragment();
                fragment.show(fragMan,"Switch user");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateUserFromId(){
    SQLiteDatabase userDb = mUserDbHelper.getReadableDatabase();
    String columns[] = {UserContract.User._ID,UserContract.User.ColumnName,UserContract.User.ColumnHighScore};
    String selection = UserContract.User._ID + " = ?";
    String[] args = {_ID.toString() };
    String sort = UserContract.User._ID + " DESC";
    Cursor c = userDb.query(UserContract.User.tableName, columns, selection, args,null,null,sort );
    if( c != null && c.moveToFirst() ) {

        String name = c.getString(
                c.getColumnIndexOrThrow(UserContract.User.ColumnName)
        );
        Integer highScore = c.getInt(
                c.getColumnIndexOrThrow(UserContract.User.ColumnHighScore)
        );

        user = new User(_ID, name, highScore);

        mUsername.setText(user.getName());
        mHighScore.setText(user.getHighScore().toString());
        score = 0;
        mScore.setText(score.toString());

    }
    else
        Log.e(LOG_TAG, "UpdateUserFromId Updation Error!");
    }

    public void switchUser(String name){
        updateProgress();
        SQLiteDatabase userDb = mUserDbHelper.getReadableDatabase();
        String columns[] = {UserContract.User._ID,UserContract.User.ColumnName,UserContract.User.ColumnHighScore};
        String selection = UserContract.User.ColumnName + " = ?";
        String[] args = { name };
        String sort = UserContract.User._ID + " DESC";
        Cursor c = userDb.query(UserContract.User.tableName, columns, selection, args,null,null,sort );
        if( c != null && c.moveToFirst() ) {

            _ID = c.getLong(
                    c.getColumnIndexOrThrow(UserContract.User._ID)
            );
            Integer highScore = c.getInt(
                    c.getColumnIndexOrThrow(UserContract.User.ColumnHighScore)
            );

            user = new User(_ID,name, highScore);

            mUsername.setText(user.getName());
            mHighScore.setText(user.getHighScore().toString());
            score = 0;
            mScore.setText(score.toString());
        }
        else
            Log.e(LOG_TAG, "SwitchUser Updation Error!");
        next(null);
    }

    public void addUser(String name){
        SQLiteDatabase userDb = mUserDbHelper.getWritableDatabase();
        ContentValues newUserValues = new ContentValues();
        newUserValues.put(UserContract.User.ColumnName,name);
        newUserValues.put(UserContract.User.ColumnHighScore,0);
        _ID = userDb.insert(UserContract.User.tableName,null, newUserValues);
        updateUserFromId();
        next(null);
    }
    public void reset(View view){
        score = 0;
        next(null);
        mScore.setText(score.toString());
    }
    private void addToLog(){
        addToPrivateLog();
        addToPublicLog();
    }
    private void addToPrivateLog(){
        try {
            File file = new File(getApplicationContext().getFilesDir(), logfileName);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(presentQuestion.getNumber() + ": " + presentQuestion.getAnswer() + "\n");
            bw.close();
            Log.v(LOG_TAG, "Added private log successfully");
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error saving private log");
        }
    }
    private void addToPublicLog(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), logfileName);
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if(presentQuestion.getAnswer())
                bw.write(presentQuestion.getNumber() + " is a prime number.\n");
            else
                bw.write(presentQuestion.getNumber() + " is not a prime number.\n");
            bw.close();
            Log.v(LOG_TAG, "Added public log successfully");
        }
        catch (Exception e) {
            Log.e(LOG_TAG, "Error saving public log "+e.getMessage()+" "+e.getCause());
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("number",number);
        savedInstanceState.putBoolean(RCheated,mCheated);
        savedInstanceState.putBoolean(RHinted,mHintShown);
        savedInstanceState.putBoolean(RFirstAttempt,firstAttempt);
        savedInstanceState.putInt(RScore,score);
    }
    public void answerTrue(View view){
        if(!checkIntegrity()) return;
        verifyAnswer(true);
    }
    public void answerFalse(View view){
        if(!checkIntegrity()) return;
        verifyAnswer(false);
    }
    private Boolean checkIntegrity(){
        checkCheating();
        return !mCheated;
    }
    private void verifyAnswer(Boolean answer){
        if(presentQuestion.checkAnswer(answer)) {
            Toast.makeText(getApplicationContext(), "Correct Response!", Toast.LENGTH_SHORT).show();
            if(firstAttempt) {
                score++;
                updateProgress();
            }
            mScore.setText(score.toString());
        }
        else
            Toast.makeText(getApplicationContext(),"Incorrect Response!",Toast.LENGTH_SHORT).show();
        firstAttempt = false;

    }


    private void checkCheating(){
        if(mCheated)
            Toast.makeText(getApplicationContext(),"You already cheated!",Toast.LENGTH_SHORT).show();
    }

    public void next(View view){
        presentQuestion= new Question();
        number = presentQuestion.getNumber();
        mQuestionText.setText(getString(R.string.questionText,number));
        mCheated = false;
        mHintShown = false;
        firstAttempt = true;
        addToLog();
    }

    public void showHint(View view){
        Intent i = new Intent(MainActivity.this, com.creation.adesh.mcassignment3.HintActivity.class);
        i.putExtra(RHinted,mHintShown);
        startActivityForResult(i,hintRequestCode);
    }

    public void showCheat(View view){
        Intent i = new Intent(MainActivity.this, com.creation.adesh.mcassignment3.CheatActivity.class);
        i.putExtra(RAnswer,presentQuestion.getAnswer());
        i.putExtra(RCheated,mCheated);
        startActivityForResult(i,cheatRequestCode);
    }
    private void updateProgress(){
        if(score>user.getHighScore())
            user.setHighScore(score);
        mHighScore.setText(user.getHighScore().toString());
        new UpdateProgressTask(getApplicationContext()).execute(user,null,null);
    }
    @Override
    protected void onDestroy() {

        SharedPreferences sharedPref = this.getSharedPreferences("score", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        sharedEditor.putInt(RScore,score);
        sharedEditor.putLong(R_Id,_ID);
        sharedEditor.commit();
        updateProgress();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){

        if(requestCode==cheatRequestCode){
            if(resultCode == Activity.RESULT_OK && i!=null){
                Boolean oldmCheated = mCheated;
                mCheated = i.getBooleanExtra(RCheated,false);
                if(!oldmCheated && mCheated)
                    Toast.makeText(getApplicationContext(),"You cheated!",Toast.LENGTH_SHORT).show();
            }
        }


        if(requestCode==hintRequestCode){
            if(resultCode == Activity.RESULT_OK && i!=null){
                Boolean oldmHintShown = mHintShown;
                mHintShown = i.getBooleanExtra(RHinted,false);
                if(!oldmHintShown && mHintShown)
                    Toast.makeText(getApplicationContext(),"You received hint!",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
