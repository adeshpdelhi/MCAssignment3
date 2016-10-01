package com.creation.adesh.mcassignment3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static TextView mCheatView = null;
    private Boolean mAnswer = false;
    private Boolean mCheated = false;
    private static final String RCheated = "CHEATED";
    private static final String RAnswer = "ANSWER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mCheatView = (TextView)findViewById(R.id.cheatView);
        mAnswer = getIntent().getBooleanExtra(RAnswer,false);
        mCheated = getIntent().getBooleanExtra(RCheated,false);
        if(savedInstanceState!=null){
            mCheated = savedInstanceState.getBoolean(RCheated, false);
            mAnswer = savedInstanceState.getBoolean(RAnswer, false);
        }
        if(mCheated)
            cheat(new View(this));
    }

    @Override
    protected  void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(RCheated, mCheated);
        savedInstanceState.putBoolean(RAnswer, mAnswer);
    }


    private void setCheated(){
        Intent i = new Intent();
        i.putExtra(RCheated,true);
        setResult(RESULT_OK,i);
        mCheated = true;
    }

    private void showCheat(){
        if(mAnswer)
            mCheatView.setText(R.string.answerPrimeText);
        else
            mCheatView.setText(R.string.answerNonPrimeText);
    }
    public void cheat(View view){
        findViewById(R.id.showCheatButton).setVisibility(View.GONE);
        setCheated();
        showCheat();
    }
}
