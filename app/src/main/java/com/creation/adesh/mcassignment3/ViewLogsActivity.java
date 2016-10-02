package com.creation.adesh.mcassignment3;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ViewLogsActivity extends AppCompatActivity {


    private static final String logfileName = "quizlogs.txt";
    TextView mLogView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);
        mLogView = (TextView) findViewById(R.id.logview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getStringExtra("type").equals("private"))
            getPrivateLogs();
        else
            getPublicLogs();

    }

    private void getPrivateLogs(){
            File file = new File(getApplicationContext().getFilesDir(), logfileName);
            display(file);
    }
    private void getPublicLogs(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), logfileName);
        display(file);
    }
    public void display(File file) {
        try{
        if (!file.exists())
            file.createNewFile();
        Log.v("Here", "here");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String fileText = "";
        if (br != null) {
            String line;
            while ((line = br.readLine()) != null)
                fileText = fileText + "\n" + line;
//            Log.v("File Text: ", fileText);
        } else
            Log.v("View logs", "NUll elemetn");
        mLogView.setText(fileText);
        br.close();
    }
        catch (Exception e) {

            Log.e("View Logs Activity", "Error viewing log: "+e.getMessage()+" "+e.getCause()+" "+e.getStackTrace());
        }
    }

}
