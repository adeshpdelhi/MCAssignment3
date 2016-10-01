package com.creation.adesh.mcassignment3;

import android.app.Activity;
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

import com.creation.adesh.mcassignment3.ViewLogsActivity;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class MainActivity extends AppCompatActivity {
    private static final String RCheated = "CHEATED";
    private static final String RHinted = "HINT";
    private static final String RAnswer = "ANSWER";
    private static final String RFirstAttempt = "FIRSTATTEMPT";
    private static final String RScore = "SCORE";
    private static final String logfileName = "quizlogs.txt";
    private static TextView mQuestionText = null;
    private static TextView mScore = null;
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
        SharedPreferences sharedPref = this.getSharedPreferences("score", Context.MODE_PRIVATE);
        score = sharedPref.getInt(RScore,0);
        Log.v("MainActivity",score.toString());
        mScore.setText(score.toString());
        if(savedInstanceState == null) {
            presentQuestion= new Question();
            number = presentQuestion.getNumber();
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

        mQuestionText.setText(getString(R.string.questionText,number));
        addToLog();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.viewprivatelogs:
                startActivity(new Intent(this, ViewLogsActivity.class).putExtra("type","private"));
                return true;
            case R.id.viewpubliclogs:
                startActivity(new Intent(this, ViewLogsActivity.class).putExtra("type","public"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Log.v("Main Activity", "Added succesfully");
        }
        catch (Exception e) {
            Log.e("Main Activity", "Error saving private log");
        }
    }
    private void addToPublicLog(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), logfileName);
//            if (!file.mkdirs()) {
//                Log.e("MainActivity", "Directory cannot be created");
//            }
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if(presentQuestion.getAnswer())
                bw.write(presentQuestion.getNumber() + " is a prime number.\n");
            else
                bw.write(presentQuestion.getNumber() + " is not a prime number.\n");
            bw.close();
            Log.v("Main Activity", "Added succesfully");
        }
        catch (Exception e) {
            Log.e("Main Activity", "Error saving public log "+e.getStackTrace()+" "+e.getMessage()+" "+e.getCause());
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
            if(firstAttempt)
                score++;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = this.getSharedPreferences("score", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        sharedEditor.putInt(RScore,score);
        sharedEditor.commit();
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
