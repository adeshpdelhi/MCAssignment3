package com.creation.adesh.mcassignment3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HintActivity extends AppCompatActivity {
    private static TextView mHintText = null;
    private Boolean mHinted = false;
    private static final String RHinted = "HINT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        mHintText = (TextView) findViewById(R.id.hintTextView);
        mHinted = getIntent().getBooleanExtra(RHinted,false);
        if(savedInstanceState!=null){
            mHinted = savedInstanceState.getBoolean(RHinted,false);
        }
        if(mHinted)
            showHint(new View(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(RHinted,mHinted);
    }
    private void setHintShown(){
        Intent i = new Intent();
        i.putExtra(RHinted,true);
        setResult(Activity.RESULT_OK,i);
        mHinted = true;
    }
    public void showHint(View view){
        findViewById(R.id.showHintButton).setVisibility(View.GONE);
        setHintShown();
        mHintText.setText(R.string.hint);
    }
}
